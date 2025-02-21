package physicsengine.simulation;

import javafx.scene.Node;
import physicsengine.Vector2D;
import physicsengine.forces.Force;
import physicsengine.shapes.WrapperShape;

public class Particle extends BaseBody implements Body{
    private WrapperShape shape;

    public Particle(float posX, float posY, float maxspeed, float minspeed,  int lifespan, WrapperShape shape) {
        super.setLifespan(lifespan);
        super.setMaxspeed(maxspeed);
        super.setMinspeed(minspeed);
        super.setPosition(new Vector2D(posX, posY));
        super.setVelocity(new Vector2D((float) ((Math.random() * (1 - -1)) + -1) , 0));
        super.setAcceleration(new Vector2D(0, 0));
        super.setMass(1);

        this.shape = shape;
    }

    public void run() {
        this.update();
        this.draw();
    }
    
    public void draw() {
        // adjust javaFX shape properties
        this.shape.getNode().setLayoutX(super.getPosition().getX());
        this.shape.getNode().setLayoutY(super.getPosition().getY());
        this.shape.getNode().setRotate(super.getAngleInDegrees());
        this.shape.getNode().setOpacity(super.getLifespan() / 200f);
    }

    public void applyForce(Force force) {
        Vector2D f = force.getForce().copy();
        f.div(super.getMass());
        super.getAcceleration().add(f);
    }

    public WrapperShape getShape() {
        return this.shape;
    }

    @Override
    public Node getNode() {
        return shape.getNode();
    }

    @Override
    public boolean contactEdge(float paneWidth, float paneHeight) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contactEdge'");
    }

    @Override
    public void bounceEdges(float paneWidth, float paneHeight) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bounceEdges'");
    }

    @Override
    public boolean contains(Body body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

}
