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
import physicsengine.noise.PerlinNoise;
import physicsengine.Common;
import physicsengine.shapes.WrapperShape;

public class FlowFieldSim implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    private FlowField ff;
    private int gridResolution = 30;

    public FlowFieldSim(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.hsb(180, 0.17, 0.97, 1), CornerRadii.EMPTY, Insets.EMPTY)));

        // generate perlin noise to orient the flow field 
        PerlinNoise pl = new PerlinNoise();
        List<List<Double>> plNoise = pl.perlinNoise2D(width, height, 5000l, gridResolution, 1.2);
        List<List<Double>> angles = new ArrayList<List<Double>>();
        for (List<Double> list : plNoise) {
            angles.add(Common.remapArray(list, 0, 1, 0, Math.PI * 2));
        }

        // instanciate a flow field
        ff = new FlowField(width, height, this.gridResolution, (float) (Math.random() * this.gridResolution), angles);
    }

    @Override
    public void stageSim() {
        for (Body body : bodyItems) {
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
            //bodies to move through flow field
            int radius = (int)(Math.random() * 10) + 1;
            WrapperShape circle = new CircleWrapper(0,0, radius , Color.hsb(radius * 36, Math.random(), Math.random(), 1));
            Body solidCircle = new SolidBody((float)(600),(float) (Math.random() * 600), 3, 0, 2, circle);
            bodyItems.add(solidCircle);
            this.pane.getChildren().add(solidCircle.getNode());

            //remove if outside of pane
            this.bodyItems.removeIf(b -> {
                double x = b.getPosition().getX();
                double y = b.getPosition().getY();

                if (x > this.pane.getWidth() || x < 0) {
                     pane.getChildren().remove(b.getNode());
                    return true;
                } else if (y > this.pane.getHeight() || y < 0) {
                     pane.getChildren().remove(b.getNode());
                    return true;
                } else {
                    return false;
                }
            });

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
