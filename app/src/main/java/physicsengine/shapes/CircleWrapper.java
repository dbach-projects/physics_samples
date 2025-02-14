package physicsengine.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class CircleWrapper extends BaseShape implements WrapperShape {
    private Group group;
    private Circle circle;
    private Line line;
    
    public CircleWrapper(double x, double y, double radius, Paint color) {
        this.circle = new Circle(x, y, radius, color);
        this.circle.setStroke(Color.BLACK);
        this.circle.setStrokeWidth(radius / 10);
        this.circle.setStrokeType(StrokeType.CENTERED);
        this.line = new Line(x, y, x, y + radius);
        this.group = new Group(this.circle, this.line);
    }

    public double getRadius() {
        return this.circle.getRadius();
    }

    public void setFill(Color color) {
        this.circle.setFill(color);
    }

    public Group getGroup() {
        return this.group;
    }

    @Override
    public Node getNode() {
        return this.group;
    }

    @Override
    public Shape getShape() {
        return this.circle;
    }
}
