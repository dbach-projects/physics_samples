package physicsengine.scenes;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;
import physicsengine.forces.*;

public class GravitySim implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    int paneWidth;
    int paneHeight;

    public GravitySim(int width, int height) {
        this.paneWidth = width;
        this.paneHeight = height;
        
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @Override
    public void stageSim() {
        System.out.println("Pane Width: " + this.pane.getWidth());
        System.out.println("Pane Height: " + this.pane.getHeight());
        
        for (Body body : bodyItems) {
            this.pane.getChildren().add(body.getNode());
        }
    }

    @Override
    public Runnable getRendererCallback() {
        return () -> {
            //remove if exeeded lifespan
            this.bodyItems.removeIf(b -> {
                if (b.isDead()) {
                    pane.getChildren().remove(b.getNode());
                    return true;
                } else {
                    return false;
                }
            });

            //add new bodies
            if (Math.random() < .25) {
                int x = (int) (Math.random() * this.paneWidth);
                float rand = (float) Math.random();
                float mass = (float) (rand * 10);
                float radius = mass + 5;
                Paint color = Color.hsb(mass * 36, Math.random(), Math.random());
                WrapperShape circle = new CircleWrapper(0, 0, radius, color);
                Body solidBodyCircle = new SolidBody(x, 20, 3, 0, mass, 500, circle);
                bodyItems.add(solidBodyCircle);
                this.pane.getChildren().add(solidBodyCircle.getNode());
            }

            // apply logic and forces
            for (Body body : this.bodyItems) {
                Force gravity = new Gravity(0f, 0.1f, body.getMass());
                body.applyForce(gravity);

                for (Body body2 : bodyItems) {
                    boolean collide = ((SolidBody) body).circleCircleCollision((SolidBody) body, (SolidBody) body2);
                    if (collide) {
                        ((SolidBody) body).bounceCircle((SolidBody)body2);
                    }
                }

                // apply force if in contact with the edge of the pane
                if (body.contactEdge(paneWidth, paneHeight)) {
                    Force friction = new Friction(body.getVelocity(), 0.1f);
                    body.applyForce(friction);
                }

                // update object
                body.bounceEdges((float)paneWidth, (float)paneHeight, (float)((Circle)body.getShape()).getRadius());
                body.run();
            }
        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
