package physicsengine.forces;

import physicsengine.Vector2D;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;

public class Pendulum implements Force {
    Vector2D bob, pivot;
    int armLength;
    float angle, angleVelocity, angleAcceleration, damping;

    public Pendulum(int armLength) {
        this.armLength = armLength;
        this.angle = (float) Math.PI / 4;
        this.damping = 0.995f;
        this.bob = new Vector2D();
    }
    
    public void connect(Body bob, Body pivot, Vector2D mouse) {
        if (!((SolidBody) bob).getIsDragged() && !((SolidBody) bob).getIsDragged()) {
            double gravity = 0.4;
 
            // angular acceleration
            this.angleAcceleration = (float) (-1 * gravity * Math.sin(this.angle) / this.armLength);
            // angular motion
            this.angleVelocity += this.angleAcceleration;
            this.angle += this.angleVelocity;

            this.angleVelocity *= this.damping;
        } else {
            Vector2D diff = Vector2D.sub(pivot.getPosition(), mouse);
            this.angle = (float) (Math.atan2(-1 * diff.getY(), diff.getX()) - Math.toRadians(90));
        }

        this.pivot = pivot.getPosition();
        //Polar-to-Cartesian conversion
        this.bob.set(this.armLength * Math.sin(this.angle), this.armLength * Math.cos(this.angle));
        this.bob.add(this.pivot);

        bob.setPosition(this.bob);
    }

    @Override
    public Vector2D getForce() {
        return this.bob;
    }
    
}
