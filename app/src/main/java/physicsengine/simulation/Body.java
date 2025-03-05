package physicsengine.simulation;

import javafx.scene.Node;
import javafx.scene.shape.Shape;
import physicsengine.Common;
import physicsengine.Vector2D;
import physicsengine.forces.Force;
import physicsengine.shapes.WrapperShape;


public abstract class Body {
    private Vector2D position, velocity, acceleration;
    private float angleInDegrees, angleVelocity, angleAcceleration;
    private float maxspeed, minspeed;
    private float maxforce = 0.5f;
    private int lifespan = -1;
    private float mass;
    private boolean applyForces = true;
    private float damping = 1f;
    private boolean isDragged = false;

    public void update() {
        this.velocity.add(acceleration);
        this.velocity.mult(this.damping);
        this.velocity.rangeLimit(maxspeed, minspeed);
        this.position.add(this.velocity);

        this.angleInDegrees = (float) Math.toDegrees(this.velocity.heading());

        if (this.lifespan >= 0) {
            this.lifespan -= 1;
        }

        this.acceleration.mult(0);
    }

    public void draw(WrapperShape shape) {
        shape.getNode().setLayoutX(this.getPosition().getX());
        shape.getNode().setLayoutY(this.getPosition().getY());
        shape.getNode().setRotate(this.getAngleInDegrees());
    }

    public void applyForce(Force force) {
        if (this.applyForces) {
            Vector2D f = force.getForce().copy();
            f.div(this.mass);
            this.acceleration.add(f);
        }
    }

    public void applyForce(Vector2D force) {
        if (this.applyForces) {
            Vector2D f = force.copy();
            f.div(this.mass);
            this.acceleration.add(f);
        }
    }

    public void bounceEdges(float width, float height, float shapeWidth) {
        float bounce = -0.9f;

        if (this.getPosition().getX() > width - shapeWidth) {
            this.getPosition().setX((float) (width - shapeWidth));
            this.getVelocity().setX(this.getVelocity().getX() * bounce);
        } else if (this.getPosition().getX() < shapeWidth) {
            this.getPosition().setX((float) shapeWidth);
            this.getVelocity().setX(this.getVelocity().getX() * bounce);
        }
        if (this.getPosition().getY() > height - shapeWidth) {
            this.getPosition().setY((float) (height - shapeWidth));
            this.getVelocity().setY(this.getVelocity().getY() * bounce);
        }
    }

    public void attract(Body body) {
        int G = 1; //global gravitational constant
        Vector2D force = Vector2D.sub(this.position, body.getPosition());
        float distance = Common.constrain(force.mag(), 5f, 25f);
        float strength = (G * (this.mass * body.getMass())) / (distance * distance);
        force.setMag(strength);
        body.applyForce(force);
    }

    public void repell(Body body, int power) {
        Vector2D force = Vector2D.sub(this.position, body.getPosition());
        float distance = force.mag();
        float strength = -1 * power / (distance * distance);
        force.setMag(strength);
        body.applyForce(force);
    }

    public void seek(Vector2D position) {
        Vector2D desired = Vector2D.sub(position, this.getPosition());
        float d = desired.mag();

        if (d < 100) {
            float m = (float) Common.normalize(d, 0, 100, 0, this.maxspeed);
            desired.setMag(m);

        } else {
            desired.setMag(this.maxspeed);
        }

        Vector2D steer = Vector2D.sub(desired, this.getVelocity());
        steer.maxLimit(this.maxforce);
        this.applyForce(steer);
    }

    public boolean hasArrived(Vector2D position, int range) {
        if (Vector2D.euclideanDistance(position, this.getPosition()) < range) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDead() {
        return (this.lifespan < 0);
    }

    /* ABSTRACT FUNCTIONS */
    public abstract void run();

    public abstract Node getNode();

    public abstract Shape getShape();

    public abstract boolean contactEdge(float width, float height);

    /* GETTERS AND SETTERS */
    public boolean getApplyForces() {
        return this.applyForces;
    }

    public void setApplyForces(boolean applyForces) {
        this.applyForces = applyForces;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector2D v) {
        this.velocity = v;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public void setPosition(Vector2D v) {
        this.position = v;
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(float maxspeed) {
        this.maxspeed = maxspeed;
    }

    public float getMinspeed() {
        return minspeed;
    }

    public void setMinspeed(float minspeed) {
        this.minspeed = minspeed;
    }

    public float getMaxforce() {
        return maxforce;
    }

    public void setMaxforce(float maxforce) {
        this.maxforce = maxforce;
    }

    public float getAngleInDegrees() {
        return this.angleInDegrees;
    }

    public void setAngleInDegrees(float angleInDegrees) {
        this.angleInDegrees = angleInDegrees;
    }

    public float getAngleVelocity() {
        return angleVelocity;
    }

    public void setAngleVelocity(float angleVelocity) {
        this.angleVelocity = angleVelocity;
    }

    public float getAngleAcceleration() {
        return angleAcceleration;
    }

    public void setAngleAcceleration(float angleAcceleration) {
        this.angleAcceleration = angleAcceleration;
    }

    public boolean getIsDragged() {
        return this.isDragged;
    }

    public void setIsDragged(boolean isDragged) {
        this.isDragged = isDragged;
    }
}
