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
import physicsengine.Vector2D;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.PolylineWrapper;
import physicsengine.shapes.Shape;
import physicsengine.noise.PerlinNoise;

public class FlowFieldSim implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    private Vector2D[][] flowField;
    private Body[][] flowFieldItems;
    private int gridResolution = 20;
    private int cols, rows;


    public FlowFieldSim(int width, int height) {
        cols = (int) (height / gridResolution);
        rows = (int) (width / gridResolution);

        this.flowField = new Vector2D[cols][rows];
        this.flowFieldItems = new Body[cols][rows];

        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        long startTime = System.nanoTime();

        PerlinNoise pl = new PerlinNoise();
        List<List<Double>> plNoise = pl.perlinNoise2D(width, height, 5000l, gridResolution);
        List<List<Double>> angles = new ArrayList<List<Double>>();
        for (List<Double> list : plNoise) {
            angles.add(this.remapArray(list, 0, 1, 0, Math.PI * 2));
        }

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {

                double xPos = x * gridResolution + 10;
                double yPos = y * gridResolution + 10 + 10;

                this.flowField[x][y] = Vector2D.fromAngle(angles.get(x).get(y).floatValue(), 1);
                System.out.println("FlowField From Angle: " + this.flowField[x][y].toString());

                //flowfield - lines and all
                this.flowField[x][y].setMag((float) (gridResolution * .5));
                // System.out.println("FlowField with Mag: " + this.flowField[x][y].toString());

                Shape line = new PolylineWrapper(xPos, yPos, xPos + this.flowField[x][y].getX(),
                        yPos + this.flowField[x][y].getY(), Color.GREY);
                Body solidLine = new SolidBody(1, line);
                this.flowFieldItems[x][y] = solidLine;

                //bodies to move through flow field
                Shape circle = new CircleWrapper(0,0, 5,
                        Color.GREY);
                Body solidCircle = new SolidBody((float)(xPos + Math.random() * 100),(float) (yPos + Math.random() * 100), 2, circle);
                bodyItems.add(solidCircle);

            }
        }

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Flow Field execution time: " + executionTime + "ns");
    }
    
    private Vector2D lookup(Vector2D position, int gridResolution) {
        int column = this.constrain((int) (position.getY() / gridResolution), 0,  this.cols - 1);
        int row = this.constrain((int) (position.getX() / gridResolution), 0, this.rows - 1);
        System.out.println("FlowField Lookup: " + this.flowField[row][column].copy().toString());
        return this.flowField[row][column].copy();
    }

    private int constrain(int val, int min_val, int max_val) {
        return Math.min(max_val, Math.max(min_val, val));
    }

    private List<Double> remapArray(List<Double> value, double start1, double stop1, double start2, double stop2) {
        List<Double> remapped = new ArrayList<>();
        for (int x = 0; x < value.size(); x++) {
            remapped.add(((value.get(x) - start1) / (stop1 - start1)) * (stop2 - start2) + start2); 
        }
        return remapped;
    }


    @Override
    public void stageSim() {

        for (Body body : bodyItems) {
            this.pane.getChildren().add(body.getNode());
        }

        for (int x = 0; x < this.flowFieldItems.length; x++) {
            for (int y = 0; y < this.flowFieldItems[x].length; y++) {
                this.pane.getChildren().add(this.flowFieldItems[x][y].getNode());
            }
        }
    }

    @Override
    public Runnable getRendererCallback() {
        return () -> {
            //bodies to move through flow field
            // Shape circle = new CircleWrapper((float)Math.random() * 600, (float) Math.random() * 600, 10, Color.GREY);
            // Body solidCircle = new SolidBody(1, circle);
            // bodyItems.add(solidCircle);
            // this.pane.getChildren().add(solidCircle.getNode());

            for (Body body : this.bodyItems) {
                System.out.println("Body Position: " + body.getPosition().toString());
                Vector2D desired = this.lookup(body.getPosition(), this.gridResolution);
                // desired.setMag(10);

                Vector2D steer = Vector2D.sub(desired, body.getVelocity());
                System.out.println("Steer: " + steer.toString());
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
