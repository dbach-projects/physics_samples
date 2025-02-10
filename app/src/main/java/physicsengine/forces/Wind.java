package physicsengine.forces;

import physicsengine.Vector2D;

public class Wind implements Force {
    Vector2D wind;

    public Wind(float x, float y) {
        this.wind = new Vector2D(x, y);
    }

    @Override
    public Vector2D getForce() {
        return this.wind;
    }
    
}
