package physicsengine.forces;

import physicsengine.Vector2D;
import physicsengine.simulation.Body;

public class Spring implements Force {
    Vector2D anchor;
    int restLength = 100;
    float constant = 0.2f;

    public Spring(Vector2D vector) {
        this.anchor = vector;
    }

    public Spring(Body anchor) {
        this.anchor = anchor.getPosition();
    }
    
    public Spring(float anchorX, float anchorY) {
        this.anchor = new Vector2D(anchorX, anchorY);
    }
    
    public void connect(Body body){
        Vector2D force = Vector2D.sub(body.getPosition(), this.anchor);
        float currentLength = force.mag();
        float x = currentLength - this.restLength;
        force.setMag(-1 * this.constant * x);
        Force f = new Spring(force);
        body.applyForce(f);
    }

    @Override
    public Vector2D getForce() {
        return this.anchor;
    }
    
}
