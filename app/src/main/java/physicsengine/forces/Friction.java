package physicsengine.forces;

import physicsengine.Vector2D;

public class Friction implements Force {
    float coefficient, normal, frictionMag;
    Vector2D friction;

    public Friction(Vector2D velocity, float coefficient) {
        this.coefficient = coefficient;
        this.normal = 1;
        this.frictionMag = coefficient * normal;


        this.friction = velocity.copy();
        this.friction.mult(-1);
        this.friction.setMag(coefficient);
    }

    @Override
    public Vector2D getForce() {
        return this.friction;
    }
}
