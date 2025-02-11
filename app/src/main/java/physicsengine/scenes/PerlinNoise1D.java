package physicsengine.scenes;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;
import physicsengine.noise.PerlinNoise;
import physicsengine.shapes.PolylineWrapper;

public class PerlinNoise1D implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();


    public PerlinNoise1D(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        PerlinNoise pn = new PerlinNoise();
        List<Double> points = pn.perlinNoise1D(128, 128, 8, 2, width);
        List<Double> result = new ArrayList<Double>();

        //perlin noise is outputed but it still needs to be spread out along the x axis effectivly turning them into points
        for(var i = 0; i < points.size(); i++){
            double xpos = i;
           // subtract amplitude to reorient to center of Y axis
           double ypos = (height / 2) + points.get(i) - 128;
           result.add(xpos);
           result.add(ypos);
        }
 
        PolylineWrapper line = new PolylineWrapper(result, Color.GREEN);
        Body lineBody = new SolidBody(1, line);

        this.bodyItems.add(lineBody);
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
