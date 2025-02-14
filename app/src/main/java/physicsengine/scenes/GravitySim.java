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
    private int NUMBER_OF_BODIES = 4;
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    private Paint color;

    public GravitySim(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.color = Color.rgb(0, 155, 255, 0.4);
    }

    @Override
    public void stageSim() {
        //TODO: Race condition here? (float) this.pane.getWidth() doesnt seem to work
        System.out.println("Pane Width: " + this.pane.getWidth());
        float paneWidth = 600;
        
        for (int i = 0; i < NUMBER_OF_BODIES; i++) {
            int x = (int) (paneWidth / NUMBER_OF_BODIES) * i + 75;
            int y = 150;
            float mass = (float) Math.random();
            WrapperShape solidBodyCircle = new CircleWrapper(0, 0, (mass * 20) + 10, color);
            bodyItems.add(new SolidBody(x, y,3,0, mass, solidBodyCircle));
        }

        for (Body body : bodyItems) {
            this.pane.getChildren().add(body.getNode());
        }

    }

    @Override
    public Runnable getRendererCallback() {
        float paneWidth = 600;
        float paneHeight = 575;

        return () -> {
            // Loop for Body Items
            for (Body body : this.bodyItems) {
                Force gravity = new Gravity(0f, 0.1f, body.getMass());
                body.applyForce(gravity);

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
