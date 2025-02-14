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
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;

public class ArriveAtTargetSim implements Sim {
    private Pane pane;
    private SolidBody target, vehicle;
    private List<Body> bodyItems = new ArrayList<Body>();
    private float posX1, posY1;
    private Paint baseColor = Color.GREY;
    private Paint arriveColor = Color.GREEN;


    public ArriveAtTargetSim(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));


        WrapperShape circle1 = new CircleWrapper(0, 0, 15, baseColor);
            posX1 = (float) Math.random() * (width - 50);
            posY1 = (float) Math.random() * (height - 50);
        this.pane.setOnMouseClicked((event) -> {
            posX1 = (float) Math.random() * (width - 50);
            posY1 = (float) Math.random() * (height - 50);
            Vector2D v = new Vector2D(posX1, posY1);
            this.target.setPosition(v);
        });
        this.target = new SolidBody(posX1, posY1, 3, 0, 1, circle1);

        WrapperShape circle2 = new CircleWrapper(0, 0, 5, baseColor);
        float posX2 = (float) Math.random() * (width - 50);
        float posY2 = (float) Math.random() * (height - 50);
        this.vehicle = new SolidBody(posX2, posY2, 3,0, 1, circle2);

        this.bodyItems.add(this.target);
        this.bodyItems.add(this.vehicle);
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
            this.vehicle.seek(this.target.getPosition());
            boolean hasArrived = this.vehicle.hasArrived(this.target.getPosition(), 10);
            if (hasArrived) {
                this.target.getShape().setFill((Color) this.arriveColor);
            } else {
                this.target.getShape().setFill((Color) this.baseColor);
            }
            this.target.run();
            this.vehicle.run();
        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
