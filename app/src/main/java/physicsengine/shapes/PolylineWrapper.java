package physicsengine.shapes;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;

public class PolylineWrapper extends BaseShape implements Shape {
    private Polyline line;

    public PolylineWrapper(List<Double> points, Paint color) {
        super.setColor(color);
        this.line = new Polyline();
        this.line.getPoints().addAll(points);
        this.line.setStrokeWidth(3);
        this.line.setStroke(color);
    }
    
    public PolylineWrapper(double startX, double startY, double endX, double endY, Paint color) {
        super.setColor(color);
        double points[] = { startX, startY, endX, endY };
        this.line = new Polyline(points);
        this.line.setStrokeWidth(3);
        this.line.setStroke(color);
    }

    public void updateEndPos(double endX, double endY) {
        this.line.getPoints().addAll(endX, endY);
        this.line.getPoints().remove(2, 4);
    }

    public void setFill(Color color) {
        this.line.setFill(color);
    }

    public PolylineWrapper duplicate() {
        List<Double> points = this.line.getPoints();
        return new PolylineWrapper(points, super.getColor());
    }

    public Polyline getShape() {
        return this.line;
    }

    public Node getNode() {
        return this.line;
    }
}
