package physicsengine.scenes;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import physicsengine.Vector2D;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;
import physicsengine.simulation.Body;
import physicsengine.simulation.Flock;
import physicsengine.simulation.SolidBody;

public class FlockingSim implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    private Vector2D mousePos = new Vector2D();
    private Flock flock = new Flock();

    public FlockingSim(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        //get mouse position
        this.pane.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            this.mousePos.set((float)x, (float)y);
        });

        // create vehicles
        for (int i = 0; i < 100; i++) {
            WrapperShape vehicle = new CircleWrapper(0, 0, 10, Color.BLUE);
            Body vehicleBody = new SolidBody((float)(width / 2 + Math.random() * 100), (float)(height / 2 + Math.random() * 100), 3, 0.25f, 10, vehicle);
            vehicleBody.setVelocity(new Vector2D((float)Math.random() * 100, (float)Math.random() * 100));
            this.bodyItems.add(vehicleBody);

            ((SolidBody) vehicleBody).setOnMouseClicked((event) -> {
                System.out.println("Boid: " + vehicleBody.toString());
            });
        }

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
            for (Body body : this.bodyItems) {

                flock.run(body, this.bodyItems, this.mousePos);

                flock.wrapping(body, (float) this.pane.getWidth(), (float) this.pane.getHeight());
                
                float count = flock.associationCount(body, bodyItems, 200);

                ((SolidBody)body).getShape().setFill(Color.hsb(290, .5, (count) / this.bodyItems.size()));

                body.run();
            }

        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
