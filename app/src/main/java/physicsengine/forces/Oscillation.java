package physicsengine.forces;

import physicsengine.Vector2D;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;

public class Oscillation implements Force {
    private Vector2D oscillation;
    private double angle;
    private double angleVelocity = 0.02;
    private double amplitude = 200;
    

    public Oscillation(Vector2D v) {
        this.oscillation = v;
    }

    public void connect(Body body, Vector2D mousePos) {
        if (!((SolidBody) body).getIsDragged()) {
            angle += angleVelocity;
            float x = (float) (amplitude * Math.sin(angle));
            //TODO: move this constant from here to the solidbody class to allow for offestting the object
            body.getPosition().setX(x + 300);
            body.getPosition().setY(this.oscillation.getY());
        } else {
            body.getPosition().setX(mousePos.getX());
            body.getPosition().setY(mousePos.getY());
            
        }
    }

    @Override
    public Vector2D getForce() {
        return this.oscillation;
    }

}
