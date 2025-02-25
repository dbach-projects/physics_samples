package physicsengine.simulation;

import java.util.List;

import physicsengine.Vector2D;

public class Flock {

    public Flock() {

    }

    public void run(Body source, List<Body> boids, Vector2D mousePos) {
        Vector2D separation = this.separate(source, boids, 35);
        Vector2D alignment = this.align(source, boids, 50);
        Vector2D cohesion = this.cohere(source, boids, 50);
        // Vector2D seekForce = this.seek(source, mousePos);

        separation.setMag(1.5f);
        alignment.setMag(1.0f);
        cohesion.setMag(1.0f);
        // seekForce.setMag(.5f);

        source.applyForce(separation);
        source.applyForce(alignment);
        source.applyForce(cohesion);
        // source.applyForce(seekForce);
    }
    
    private Vector2D separate(Body source, List<Body> boids, int desiredSeparation) {
        int count = 0;
        Vector2D sum = new Vector2D();

        for (Body boid : boids) {
            double dist = Vector2D.euclideanDistance(source.getPosition(), boid.getPosition());

            if (source != boid && dist < desiredSeparation) {
                Vector2D diff = Vector2D.sub(source.getPosition(), boid.getPosition());
                diff.setMag((float) (1 / dist));
                sum.add(diff);
                count++;
            }
        }
        
        if (count > 0) {
            sum.div(boids.size());
            sum.setMag(source.getMaxspeed());
            Vector2D steer = Vector2D.sub(sum, source.getVelocity());
            steer.maxLimit(source.getMaxforce());
            return steer;
        } else {
            return new Vector2D();
        }
    }

    private Vector2D align(Body source, List<Body> boids, int neighbourDistance) {
        Vector2D sum = new Vector2D();
        int count = 0;

        for (Body boid : boids) {
            double dist = Vector2D.euclideanDistance(source.getPosition(), boid.getPosition());
            if (source != boid && dist < neighbourDistance) {
                sum.add(boid.getVelocity());
                count++;
            }
        }

        if (count > 0) {
            sum.div(boids.size());
            sum.setMag(source.getMaxspeed());
            Vector2D steer = Vector2D.sub(sum, source.getVelocity());
            steer.maxLimit(source.getMaxforce());
            return steer;
        } else {
            return new Vector2D();
        }
    }
    
    private Vector2D cohere(Body source, List<Body> boids, int neighbourDistance) {
        Vector2D sum = new Vector2D();
        int count = 0;
        for (Body boid : boids) {
            double dist = Vector2D.euclideanDistance(source.getPosition(), boid.getPosition());
            if ((source != boid) && (dist < neighbourDistance)) {
                sum.add(boid.getPosition());
                count++;
            }
        }
        if (count > 0) {
            sum.div(count);
            return this.seek(source, sum);
            // Use the seek() function from Example 5.10. The target to seek is the average position of your neighbors.
        } else {
            return new Vector2D(0, 0);
        }
    }

    private Vector2D seek(Body source, Vector2D target) {
        Vector2D desired = Vector2D.sub(target, source.getPosition());

        desired.normalize();
        desired.setMag(source.getMaxspeed());

        Vector2D steer = Vector2D.sub(desired, source.getVelocity());
        steer.maxLimit(source.getMaxforce());

        return steer;
    }

    public void wrapping(Body source, float screenWidth, float screenHeight) {
        float x = source.getPosition().getX();
        float y = source.getPosition().getY();
        if (x <= 0) x = screenWidth;
        if (y <= 0) y = screenHeight;
        if (x >= screenWidth) x = 0;
        if (y >= screenHeight) y = 0;
        source.setPosition(new Vector2D(x, y));
    }
      
    public float associationCount(Body source, List<Body> boids, int desiredSeparation) {
        int count = 0;
        for (Body boid : boids) {
            double dist = Vector2D.euclideanDistance(source.getPosition(), boid.getPosition());

            if (source != boid && dist < desiredSeparation) {
                count++;
            }
        }

        return (float) count / boids.size(); //range [0,1]
    }
}
