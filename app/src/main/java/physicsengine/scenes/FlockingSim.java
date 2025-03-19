package physicsengine.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import physicsengine.Common;
import physicsengine.Vector2D;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.WrapperShape;
import physicsengine.simulation.BinLatticeSubdivision;
import physicsengine.simulation.Body;
import physicsengine.simulation.Flock;
import physicsengine.simulation.SolidBody;

public class FlockingSim implements Sim {
    private Pane pane;
    private List<Body> bodyItems = new ArrayList<Body>();
    private Vector2D mousePos = new Vector2D();
    private int screenWidth, screenHeight;
    private int pxPerGrid = 100;
    private BinLatticeSubdivision bin;
    private ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();


    public FlockingSim(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

        bin = new BinLatticeSubdivision(this.screenWidth, this.screenHeight, this.pxPerGrid, this.bodyItems);

        this.pane = new Pane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        //get mouse position
        this.pane.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            this.mousePos.set((float)x, (float)y);
        });

        // create vehicles
        for (int i = 0; i < 300; i++) {
            WrapperShape vehicle = new CircleWrapper(0, 0, Math.random() * 10, Color.BLUE);
            Body vehicleBody = new SolidBody((float)(width / 2 + Math.random() * 100), (float)(height / 2 + Math.random() * 100), 3, 0.25f, 10, -1, vehicle);
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

            // BinLattice to lookup neighbours and improve performance
            bin.compileSubdivision(screenWidth, screenHeight, pxPerGrid, bodyItems);
            List<Body>[][] subGrid = bin.getSubDivGrid();
                
            for (Body body : this.bodyItems) {
                int row = (int) (body.getPosition().getX() / this.pxPerGrid);
                int col = (int) (body.getPosition().getY() / this.pxPerGrid);
                int rows = (int) (this.screenWidth / this.pxPerGrid);
                int cols = (int) (this.screenHeight / this.pxPerGrid);
                col = (int) Common.constrain(col, 0, cols - 1);
                row = (int) Common.constrain(row, 0, rows - 1);

                List<Body> neighbours = subGrid[row][col];

                // execute and monitor mulithreaded flocks 
                Flock flock = new Flock(body, neighbours, this.mousePos);
                pool.execute(flock);
                int activeCount = pool.getActiveCount();
                System.out.println("Currently active threads:" + activeCount);

                flock.wrapping(body, (float) this.pane.getWidth(), (float) this.pane.getHeight());

                double brightness = flock.associationCount(body, neighbours, 50);

                ((SolidBody) body).getShape().setFill(Color.hsb(290, .5, brightness));

                body.run();
            }

        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
