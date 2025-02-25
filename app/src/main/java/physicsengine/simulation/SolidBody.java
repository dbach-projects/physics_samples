package physicsengine.simulation;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.event.EventHandler;
import physicsengine.Common;
import physicsengine.Vector2D;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;

public class SolidBody extends Body {
    private WrapperShape shape;

    public SolidBody(float posX, float posY, float maxspeed, float minspeed, float mass, int lifespan, WrapperShape shape) {
        super.setLifespan(lifespan);
        super.setMass(mass);
        super.setMaxspeed(maxspeed);
        super.setMinspeed(minspeed);
        super.setPosition(new Vector2D(posX, posY));
        super.setVelocity(new Vector2D(0, 0));
        super.setAcceleration(new Vector2D(0, 0));

        super.setAngleInDegrees(0);
        super.setAngleVelocity(0);
        super.setAngleAcceleration(0);

        this.shape = shape;
    }

    public SolidBody(float posX, float posY, float maxspeed, float minspeed, float mass, int lifespan, WrapperShape shape, boolean applyForces) {
        super.setLifespan(lifespan);
        super.setMass(mass);
        super.setMaxspeed(maxspeed);
        super.setMinspeed(minspeed);
        super.setApplyForces(applyForces);
        super.setPosition(new Vector2D(posX, posY));
        super.setVelocity(new Vector2D(0, 0));
        super.setAcceleration(new Vector2D(0, 0));

        super.setAngleInDegrees(0);
        super.setAngleVelocity(0);
        super.setAngleAcceleration(0);

        this.shape = shape;
    }


    public void run() {
        this.update();
        this.draw(this.shape);
    }

    public void setOnMouseClicked(EventHandler<? super MouseEvent> callback) {
        this.shape.getNode().setOnMouseClicked(callback);
    }

    public void setOnMouseDragged(EventHandler<? super MouseEvent> callback) {
        this.shape.getNode().setOnMouseDragged(callback);
    }

    public void setOnMouseReleased(EventHandler<? super MouseEvent> callback) {
        this.shape.getNode().setOnMouseReleased(callback);
    }

    @Override
    public boolean contactEdge(float width, float height) {
        if (this.shape instanceof CircleWrapper) {
            double radius = ((CircleWrapper) shape).getRadius();
            return (super.getPosition().getY() > height - radius - 1);
        }
        return false;
    }

    public boolean circleCircleCollision(SolidBody c1, SolidBody c2) {
        if (c1 != c2) {
            double r1 = ((Circle) c1.getShape()).getRadius();
            double r2 = ((Circle) c2.getShape()).getRadius();
            double dist = Vector2D.euclideanDistance(c1.getPosition(), c2.getPosition());

            if (r1 + r2 >= dist) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void bounceCircle(SolidBody c2) {
        // get the mtd
        Vector2D delta = Vector2D.sub(super.getPosition(), c2.getPosition());
        float dist = Vector2D.euclideanDistance(this.getPosition(), c2.getPosition());
        // minimum translation distance to push balls apart after intersecting
        float c1Rad = (float)((Circle)this.shape.getShape()).getRadius();
        float c2Rad = (float)((Circle)c2.shape.getShape()).getRadius();
        delta.mult( ((c1Rad + c2Rad) - dist) / dist); 

        // resolve intersection --
        // inverse mass quantities
        float im1 = 1 / super.getMass(); 
        float im2 = 1 / c2.getMass();

        // push-pull them apart based off their mass
        this.getPosition().add(Vector2D.mult(delta, im1 / (im1 + im2)));
        c2.getPosition().sub(Vector2D.mult(delta, im2 / (im1 + im2)));

        // impact speed
        Vector2D v = Vector2D.sub(super.getVelocity(), c2.getVelocity());
        float vn = v.dot(Vector2D.normalize(delta));

        // sphere intersecting but moving away from each other already
        if (vn > 0.0f) return;

        // collision impulse
        float i = (-(1.0f + Common.RESTITUTION) * vn) / (im1 + im2);
        Vector2D impulse = Vector2D.normalize(delta);
        impulse.mult(i);

        // change in momentum
        this.getVelocity().add(Vector2D.mult(impulse, im1));
        c2.getVelocity().sub(Vector2D.mult(impulse, im2));
    }

    @Override
    public Node getNode() {
        return this.shape.getNode();
    }

    public Shape getShape() {
        return this.shape.getShape();
    }

    public WrapperShape getWrappedShape() {
        return this.shape;
    }

    @Override
    public String toString() {
        return "ShapeId: " + this.shape.toString() +
                " Position: " + super.getPosition().toString() +
                " Velocity: " + super.getVelocity().toString();
    }

}
