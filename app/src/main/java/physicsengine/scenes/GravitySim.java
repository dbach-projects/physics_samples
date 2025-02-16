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
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;
import physicsengine.forces.*;

public class GravitySim implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    private Paint color;
    int paneWidth = 600;
    int paneHeight = 574;

    public GravitySim(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.color = Color.PURPLE;
    }

    @Override
    public void stageSim() {        
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
            if (Math.random() < .1) {
                int x = (int) (Math.random() * this.paneWidth);
                int y = this.paneHeight / 2;
                float mass = (float) Math.random();
                WrapperShape circle = new CircleWrapper(0, 0, (mass * 20) + 10, color);
                Body solidBodyCircle = new SolidBody(x, y, 3, 0, mass, 200, circle);
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
                        ((SolidBody) body).getShape().setFill(Color.RED);
                        ((SolidBody) body).bounceCircle((SolidBody)body2);
                    }
                }

                // apply force if in contact with the edge of the pane
                if (body.contactEdge(paneWidth, paneHeight)) {
                    Force friction = new Friction(body.getVelocity(), 0.1f);
                    body.applyForce(friction);
                }

                // update object
                body.bounceEdges(paneWidth, paneHeight);
                body.run();
            }
        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
