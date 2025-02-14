package physicsengine.scenes;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import physicsengine.simulation.Body;
import physicsengine.noise.PerlinNoise;

public class PerlinNoise2D implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();


    public PerlinNoise2D(int width, int height) {
        // Create a canvas to draw on, and get its GraphicsContext.
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        this.pane = new StackPane(canvas);
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        long startTime = System.nanoTime();
        PerlinNoise pn = new PerlinNoise();
        WritableImage img = pn.perlinNoise2DWritableImage(width, height, 50932, 50, 1.2);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Method execution time: " + executionTime + "ns");
        
        // Draw the generated image on the canvas.
        gc.drawImage(img, 0, 0);
    }

    @Override
    public void stageSim() {
        for (Body body : this.bodyItems) {
            this.pane.getChildren().add(body.getNode());
        }
    }

    @Override
    public Runnable getRendererCallback() {
        return () -> {

            for (Body body : this.bodyItems) {
                body.run();
            }

        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
