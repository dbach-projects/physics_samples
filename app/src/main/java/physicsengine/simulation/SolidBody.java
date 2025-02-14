package physicsengine.simulation;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.event.EventHandler;
import physicsengine.Vector2D;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;

public class SolidBody extends BaseBody implements Body {
    private WrapperShape shape;

    public SolidBody(float posX, float posY, float maxspeed, float minspeed, float mass, WrapperShape shape) {
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

    public SolidBody(float posX, float posY,float maxspeed, float minspeed, float mass,  WrapperShape shape, boolean applyForces) {
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
    public boolean contains(Body body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    @Override
    public boolean contactEdge(float width, float height) {
        if (this.shape instanceof CircleWrapper) {
            double radius = ((CircleWrapper) shape).getRadius();
            return (super.getPosition().getY() > height - radius - 1);
        }
        return false;
    }

    @Override
    public void bounceEdges(float width, float height) {
        float bounce = -0.9f;
        double radius = 0;
        if (this.shape instanceof CircleWrapper) {
            radius = ((CircleWrapper) shape).getRadius();
        }

        if (this.getPosition().getX() > width - radius) {
            this.getPosition().setX((float) (width - radius));
            this.getVelocity().setX(this.getVelocity().getX() * bounce);
        } else if (this.getPosition().getX() < radius) {
            this.getPosition().setX((float) radius);
            this.getVelocity().setX(this.getVelocity().getX() * bounce);
        }
        if (this.getPosition().getY() > height - radius) {
            this.getPosition().setY((float) (height - radius));
            this.getVelocity().setY(this.getVelocity().getY() * bounce);
        }
    }

    @Override
    public boolean isDead() {
        return false;
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
