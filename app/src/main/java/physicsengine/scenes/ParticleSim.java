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
import physicsengine.Vector2D;
import physicsengine.forces.Force;
import physicsengine.forces.Friction;
import physicsengine.forces.Gravity;
import physicsengine.forces.Oscillation;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;
import physicsengine.simulation.Body;
import physicsengine.simulation.Emitter;
import physicsengine.simulation.SolidBody;

public class ParticleSim implements Sim {
    private int NUMBER_OF_EMITTERS = 4;
    private Pane pane;
    private List<Body> bodies;
    private Body repeller;
    private Oscillation oscillation;
    private Vector2D mousePos = new Vector2D();

    List<Emitter> emitters;

    public ParticleSim(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Paint color = Color.rgb(200, 153, 255, .5);
        this.bodies = new ArrayList<Body>();
        this.emitters = new ArrayList<Emitter>();
        WrapperShape circle = new CircleWrapper(0, 0, 20, color);
        SolidBody solidRepeller = new SolidBody(0, 0,3,0, 10, circle);
        this.repeller = (Body) solidRepeller;
        this.oscillation = new Oscillation(new Vector2D(300, 200));

        // callback function
        solidRepeller.setOnMouseDragged((event) -> {
            solidRepeller.setIsDragged(true);
            float x1 = (float) event.getSceneX();
            float y1 = (float) event.getSceneY();
            this.mousePos.setX(x1);
            this.mousePos.setY(y1);
        });
        solidRepeller.setOnMouseReleased((event) -> {
            solidRepeller.setIsDragged(false);
        });
    }

    @Override
    public void stageSim() {
        float paneWidth = (float) this.pane.getWidth();
        System.out.println("Pane Width: " + paneWidth);

        for (int i = 0; i < NUMBER_OF_EMITTERS; i++) {
            int x = (int) (600 / NUMBER_OF_EMITTERS) * i + 75;
            int y = 100;
            this.emitters.add(new Emitter(x, y));
        }

        this.pane.getChildren().add(this.repeller.getNode());
    }

    @Override
    public Pane getPane() {
        return this.pane;
    }

    @Override
    public Runnable getRendererCallback() {
        float paneWidth = (float) this.pane.getWidth();
        float paneHeight = (float) this.pane.getHeight();

        return () -> {
            this.oscillation.connect(repeller, this.mousePos);
            repeller.run();
            
            // Loop for Emitters
            for (Emitter emitter : emitters) {
                Force gravity = new Gravity(0f, 0.1f);

                emitter.applyRepeller(repeller, 150);
                emitter.addParticle(pane);
                emitter.applyForce(gravity);
                emitter.run(pane);
            }

            // Loop for Body Items
            for (Body body : this.bodies) {
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

}
