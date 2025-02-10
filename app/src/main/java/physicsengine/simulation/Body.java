package physicsengine.simulation;

import javafx.scene.Node;
import physicsengine.Vector2D;
import physicsengine.forces.Attraction;
import physicsengine.forces.Force;
import physicsengine.forces.Repulsion;
import physicsengine.shapes.Shape;

public interface Body {

    Shape getShape();

    Node getNode();

    Vector2D getPosition();

    float getMass();

    public void applyForce(Force force);

    void followMouse(Vector2D mousePosition);

    void run();

    void attract(Body body1);

    boolean contactEdge(float paneWidth, float paneHeight);

    Vector2D getVelocity();

    void bounceEdges(float paneWidth, float paneHeight);

    boolean contains(Body body);

    public void repell(Body body, int power);

    boolean isDead();

    void setPosition(Vector2D bob);
}


abstract class BaseBody {
    private Vector2D position, velocity, acceleration;
    private float angleInDegrees, angleVelocity, angleAcceleration;
    private int maxSpeed;
    private float maxforce = 2f;
    private int lifespan = -1;
    private float mass;
    private boolean applyForces = true;
    private float damping = 0.98f;
    private boolean isDragged = false;

    public void update() {
        this.velocity.add(acceleration);
        this.velocity.mult(this.damping);
        this.velocity.limit(this.maxSpeed);
        this.position.add(this.velocity);

        this.angleInDegrees = (float) Math.toDegrees(this.velocity.heading());

        if (this.lifespan >= 0) {
            this.lifespan -= 1;
        }

        this.acceleration.mult(0);
    }

    public void draw(Shape shape) {
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

    public void attract(Body body) {
        int G = 1; //global gravitational constant
        Vector2D force = Vector2D.sub(this.position, body.getPosition());
        float distance = constrain(force.mag(), 5, 25);
        float strength = (G * (this.mass * body.getMass())) / (distance * distance);
        force.setMag(strength);
        Force attract = new Attraction(force);
        body.applyForce(attract);
    }

    public void repell(Body body, int power) {
        Vector2D force = Vector2D.sub(this.position, body.getPosition());
        float distance = force.mag();
        float strength = -1 * power / (distance * distance);
        force.setMag(strength);
        Force repulse = new Repulsion(force);
        body.applyForce(repulse);
    }

    public boolean arrive(Body target) {
        Vector2D desired = Vector2D.sub(target.getPosition(), this.getPosition());
        float d = desired.mag();

        if (d < 100) {
            float m = (float) this.norm(d, 0, 100, 0, this.maxSpeed);
            desired.setMag(m);

        } else {
            desired.setMag(this.maxSpeed);
        }

        Vector2D steer = Vector2D.sub(desired, this.getVelocity());
        steer.limit(this.maxforce);
        this.applyForce(steer);
        //TODO: change this so it isnt limited to circle radius
        if (Vector2D.euclideanDistance(target.getPosition(), this.getPosition()) < 10) {
            return true;
        } else {
            return false;
        }
    }
    
    private double norm(double value, double minA, double maxA, double minB, double maxB) {
        double newval = (value - minA) / (maxA - minA) * (maxB - minB) + minB;

        if (minB < maxB) {
            return this.constrain(newval, minB, maxB);
        } else {
            return this.constrain(newval, maxB, minB);
        }
    }

    private double constrain(double val, double min, double max) {
        return Math.max(Math.min(val, max), min);
    }

    public boolean isDead() {
        return (this.lifespan < 0);
    }

    public void followMouse(Vector2D mousePos) {
        this.setPosition(mousePos);
    }

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

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
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

    private float constrain(float dist, float min, float max) {
        return Math.max(Math.min(dist, max), min);

    }

    public boolean getIsDragged() {
        return this.isDragged;
    }

    public void setIsDragged(boolean isDragged) {
        this.isDragged = isDragged;
    }
}
