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
import physicsengine.simulation.FlowField;
import physicsengine.simulation.SolidBody;
import physicsengine.Vector2D;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.Shape;
import physicsengine.noise.PerlinNoise;
import physicsengine.Common;

public class FlowFieldSim implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    private int cols, rows;
    private FlowField ff;
    private int gridResolution = 20;

    public FlowFieldSim(int width, int height) {
        rows = (int) (width / gridResolution);
        cols = (int) (height / gridResolution);

        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // generate perlin noise to orient the flow field 
        PerlinNoise pl = new PerlinNoise();
        List<List<Double>> plNoise = pl.perlinNoise2D(width, height, 5000l, gridResolution);
        List<List<Double>> angles = new ArrayList<List<Double>>();
        for (List<Double> list : plNoise) {
            angles.add(Common.remapArray(list, 0, 1, 0, Math.PI * 2));
        }

        // instanciate a flow field
        ff = new FlowField(width, height, this.gridResolution, angles);

        // create bodies to move through the flow field
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                double xPos = (x * gridResolution) + (gridResolution / 2);
                double yPos = (y * gridResolution) + (gridResolution / 2);
                
                //bodies to move through flow field
                Shape circle = new CircleWrapper(0,0, 5, Color.GREY);
                Body solidCircle = new SolidBody((float)(xPos + Math.random() * 100),(float) (yPos + Math.random() * 100), 2, circle);
                bodyItems.add(solidCircle);
            }
        }
    }

    @Override
    public void stageSim() {
        for (Body body : bodyItems) {
            System.out.println("Fooooo " + body.getNode());

            this.pane.getChildren().add(body.getNode());
        }

        for (int x = 0; x < this.ff.getFlowFieldLines().length; x++) {
            for (int y = 0; y < this.ff.getFlowFieldLines()[x].length; y++) {
                this.pane.getChildren().add(this.ff.getFlowFieldLines()[x][y].getNode());
            }
        }
    }

    @Override
    public Runnable getRendererCallback() {
        return () -> {
            for (Body body : this.bodyItems) {
                Vector2D desired = this.ff.lookup(body.getPosition(), this.gridResolution);

                Vector2D steer = Vector2D.sub(desired, body.getVelocity());
                body.applyForce(steer);
                
                body.run();
            }

        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
