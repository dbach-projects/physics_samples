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
import javafx.scene.shape.Circle;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.PolylineWrapper;
import physicsengine.shapes.WrapperShape;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;
import physicsengine.Vector2D;
import physicsengine.forces.*;

public class PendulumSim implements Sim {
    private Pane pane;
    private Body anchor, bob, line;
    private List<Body> bodyItems;
    private Paint color;
    private Force pendulum;
    private Vector2D mousePos = new Vector2D();


    public PendulumSim(int width, int height) {
        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.color = Color.GREY;
        this.bodyItems = new ArrayList<Body>();
        WrapperShape circle1 = new CircleWrapper(0, 0, 15, color);
        WrapperShape circle2 = new CircleWrapper(0, 0, 30, color);
        this.anchor = new SolidBody(300, 100, 3,0,10, -1, circle1,false);
        SolidBody solidBob = new SolidBody(300, 375, 3,0,10, -1, circle2);
        this.bob = (Body) solidBob;
        WrapperShape line = new PolylineWrapper(300, 100, 300, 475, 3f, color);
        this.line = new SolidBody(0, 0, 3,0,0, -1, line, false);
        this.pendulum = new Pendulum(150);

        //callback functions
        solidBob.setOnMouseDragged((event) -> {
            solidBob.setIsDragged(true);
            float x1 = (float) event.getSceneX();
            float y1 = (float) event.getSceneY();
            this.mousePos.setX(x1);
            this.mousePos.setY(y1);
        });
        solidBob.setOnMouseReleased((event) -> {
            solidBob.setIsDragged(false);
        });

        this.bodyItems.add(this.line);
        this.bodyItems.add(this.bob);
        this.bodyItems.add(this.anchor);
    }

    @Override
    public void stageSim() {
        for (Body body : this.bodyItems) {
            this.pane.getChildren().add(body.getNode());
        }
    }

    @Override
    public Runnable getRendererCallback() {
        float paneWidth = 600;
        float paneHeight = 575;

        return () -> {
            //link end of polyline to the bob
            Vector2D pos = this.bob.getPosition();
            WrapperShape plws = ((SolidBody) this.line).getWrappedShape();
            ((PolylineWrapper)plws).updateEndPos(pos.getX(), pos.getY());

            //apply the pendulum force
            ((Pendulum) this.pendulum).connect(this.bob, this.anchor, this.mousePos);

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
                float radius = 1;
                if(body.getShape() instanceof Circle){
                    radius = (float)((Circle)body.getShape()).getRadius();
                }
                body.bounceEdges((float)paneWidth, (float)paneHeight, radius);
                body.run();
            }
        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
