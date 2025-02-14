package physicsengine.shapes;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;

public class PolylineWrapper extends BaseShape implements WrapperShape {
    private Polyline line;

    public PolylineWrapper(List<Double> points, float strokeWidth, Paint color) {
        this.line = new Polyline();
        this.line.getPoints().addAll(points);
        this.line.setStrokeWidth(strokeWidth);
        this.line.setStroke(color);
    }
    
    public PolylineWrapper(double startX, double startY, double endX, double endY, float strokeWidth, Paint color) {
        double points[] = { startX, startY, endX, endY };
        this.line = new Polyline(points);
        this.line.setStrokeWidth(strokeWidth);
        this.line.setStroke(color);
    }

    public void updateEndPos(double endX, double endY) {
        this.line.getPoints().addAll(endX, endY);
        this.line.getPoints().remove(2, 4);
    }

    public void setFill(Color color) {
        this.line.setFill(color);
    }

    @Override
    public Polyline getShape() {
        return this.line;
    }

    @Override
    public Node getNode() {
        return this.line;
    }
}
