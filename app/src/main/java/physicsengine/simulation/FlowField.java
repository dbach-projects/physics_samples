package physicsengine.simulation;

import java.util.List;

import javafx.scene.paint.Color;
import physicsengine.Common;
import physicsengine.Vector2D;
import physicsengine.shapes.PolylineWrapper;
import physicsengine.shapes.WrapperShape;

public class FlowField {
    private Vector2D[][] flowField;
    private Body[][] flowFieldLines;
    int rows, cols;


    public FlowField(int windowWidth, int windowHeight, int gridResolution, float vecMagnatude, List<List<Double>> angles) {
        rows = (int) (windowWidth / gridResolution);
        cols = (int) (windowHeight / gridResolution);

        this.flowField = new Vector2D[rows][cols];
        this.flowFieldLines = new Body[rows][cols];

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                double xPos = (x * gridResolution) + (gridResolution / 2);
                double yPos = (y * gridResolution) + (gridResolution / 2);

                this.flowField[x][y] = Vector2D.fromAngle(angles.get(x).get(y).floatValue(), 1);

                //flowfield - lines and all
                this.flowField[x][y].setMag(vecMagnatude);

                double endXpos = xPos + this.flowField[x][y].getX();
                double endYpos = yPos + this.flowField[x][y].getY();
                WrapperShape line = new PolylineWrapper(xPos, yPos, endXpos,endYpos, 1f, Color.hsb(0, 0, .5, .3));
                Body solidLine = new SolidBody(0f,0f,3f,0f,0f, -1, line);
                this.flowFieldLines[x][y] = solidLine;
            }
        }
    }
    
    public Vector2D lookup(Vector2D position, int gridResolution) {
        int column = (int)Common.constrain(position.getY() / gridResolution, 0, this.cols - 1);
        int row = (int)Common.constrain(position.getX() / gridResolution, 0, this.rows - 1);
        return this.flowField[row][column].copy();
    }

    public boolean onField(Vector2D position, int gridResolution) {
        int yCell = (int) (position.getY() / gridResolution);
        int xCell = (int) (position.getX() / gridResolution);

        if (yCell > this.cols - 1 || yCell < 0) {
            return false;
        } else if (xCell > this.rows - 1 || xCell < 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public Vector2D[][] getFlowField() {
        return flowField;
    }

    public Body[][] getFlowFieldLines() {
        return flowFieldLines;
    }
    
}
