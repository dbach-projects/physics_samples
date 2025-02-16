package physicsengine.simulation;

import java.util.ArrayList;
import java.util.List;

import physicsengine.Common;

public class BinLatticeSubdivision {
    private List<Body>[][] subDivGrid; 

    public BinLatticeSubdivision(int width, int height, int subdivisionResolution, List<Body> objects) {
        int rows = (int) (height / subdivisionResolution);
        int cols = (int) (width / subdivisionResolution);

        // create subdivision grid array
        this.subDivGrid = new ArrayList[rows][cols];

        for (int x = 0; x < this.subDivGrid.length; x++) {
            for (int y = 0; y < this.subDivGrid[x].length; y++) {
                this.subDivGrid[x][y] = new ArrayList<Body>();
            }
        }

        for (Body body : objects) {
            int row = (int) (body.getPosition().getX() / subdivisionResolution);
            int col = (int) (body.getPosition().getY() / subdivisionResolution);

            col = (int)Common.constrain(col, 0, cols - 1);
            row = (int)Common.constrain(row, 0, rows - 1);

            subDivGrid[row][col].add(body);
        }
    }

    public List<Body>[][] getSubDivGrid() {
        return subDivGrid;
    }
}
