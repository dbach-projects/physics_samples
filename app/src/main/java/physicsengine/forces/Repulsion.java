package physicsengine.forces;

import physicsengine.Vector2D;

public class Repulsion implements Force {
    Vector2D repulsion;

    public Repulsion(Vector2D v) {
        this.repulsion = v;
    }

    @Override
    public Vector2D getForce() {
        return this.repulsion;
    }

}
