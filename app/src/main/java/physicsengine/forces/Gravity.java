package physicsengine.forces;

import physicsengine.Vector2D;

public class Gravity implements Force {
    Vector2D gravity;

    public Gravity(float x, float y) {
        this.gravity = new Vector2D(x, y);
    }
    
    public Gravity(float x, float y, float mass) {
        this.gravity = new Vector2D(x, y);
        this.gravity.mult(mass);
    }

    @Override
    public Vector2D getForce() {
        return this.gravity;
    }
    
}
