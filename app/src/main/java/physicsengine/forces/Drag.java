package physicsengine.forces;

import physicsengine.Vector2D;

public class Drag implements Force {
    Vector2D drag;
    float constant, speed, dragMagnitude;

    public Drag(Vector2D velocity, float constant) {
        this.constant = constant;
        this.speed = velocity.mag();
        this.dragMagnitude = this.constant * this.speed * this.speed;
        this.drag = velocity.copy();
        drag.mult(-1);
        drag.setMag(this.dragMagnitude);
    }

    @Override
    public Vector2D getForce() {
        return this.drag;
    }
}
