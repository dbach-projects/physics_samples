package physicsengine.simulation;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import physicsengine.Vector2D;
import physicsengine.forces.Force;
import physicsengine.shapes.Shape;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.RectangleWrapper;

public class Emitter {
    Vector2D origin;
    List<Body> particles = new ArrayList<>();

    public Emitter(float x, float y) {
        this.origin = new Vector2D(x, y);
    }

    public void addParticle(Pane pane) {
        Body p;
        Paint color = Color.rgb(200, 153, 255, .5);
        double r = Math.random();

        if (r < 0.5) {
            Shape circle = new CircleWrapper(0, 0, 10, color);
            p = new Particle(this.origin.getX(), this.origin.getY(), circle);
            this.particles.add(p);
        } else {
            Shape rectangle = new RectangleWrapper(15, 15, color);
            p = new Particle(this.origin.getX(), this.origin.getY(), rectangle);
            this.particles.add(p);
        }

        pane.getChildren().add(p.getNode());
        this.particles.add(p);

    }
    
    public void run(Pane pane) {
        for (int i = this.particles.size() - 1; i >= 0; i--) {
            Body particle = this.particles.get(i);
            particle.run();
            if (particle.isDead()) {
                pane.getChildren().remove(particle.getNode());
                this.particles.remove(i);
            }
        }
    }

    public void applyForce(Force force) {
        for (Body particle : this.particles) {
            particle.applyForce(force);
        }
    }

    public void applyRepeller(Body repeller, int power) {
        for (Body particle : this.particles) {
            repeller.repell(particle, power);
        }
    }

    public int getParticleCount(){
        return this.particles.size();
    }
    
}
