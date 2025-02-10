package physicsengine.forces;

import physicsengine.Vector2D;

public class Attraction implements Force {
    Vector2D attraction;

    public Attraction(Vector2D v) {
        this.attraction = v;
    }

    @Override
    public Vector2D getForce() {
        return this.attraction;
    }
    
}
