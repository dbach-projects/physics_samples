package physicsengine.simulation;

import java.util.List;

import javafx.scene.paint.Color;
import physicsengine.Common;
import physicsengine.Vector2D;
import physicsengine.shapes.PolylineWrapper;
import physicsengine.shapes.Shape;

public class FlowField {
    private Vector2D[][] flowField;
    private Body[][] flowFieldLines;
    int rows, cols;


    public FlowField(int windowWidth, int windowHeight, int gridResolution, List<List<Double>> angles) {
        rows = (int) (windowWidth / gridResolution);
        cols = (int) (windowHeight / gridResolution);

        this.flowField = new Vector2D[cols][rows];
        this.flowFieldLines = new Body[cols][rows];

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                double xPos = (x * gridResolution) + (gridResolution / 2);
                double yPos = (y * gridResolution) + (gridResolution / 2);

                this.flowField[x][y] = Vector2D.fromAngle(angles.get(x).get(y).floatValue(), 1);

                //flowfield - lines and all
                this.flowField[x][y].setMag((float) (gridResolution * .5));

                Shape line = new PolylineWrapper(xPos, yPos, xPos + this.flowField[x][y].getX(),
                        yPos + this.flowField[x][y].getY(), Color.GREY);
                Body solidLine = new SolidBody(1, line);
                this.flowFieldLines[x][y] = solidLine;
            }
        }
    }
    
    public Vector2D lookup(Vector2D position, int gridResolution) {
        int column = Common.constrain((int) (position.getY() / gridResolution), 0, this.cols - 1);
        int row = Common.constrain((int) (position.getX() / gridResolution), 0, this.rows - 1);
        return this.flowField[row][column].copy();
    }
    
    public Vector2D[][] getFlowField() {
        return flowField;
    }

    public Body[][] getFlowFieldLines() {
        return flowFieldLines;
    }
    
}
