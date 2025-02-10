package physicsengine.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;

public class CircleWrapper extends BaseShape implements Shape {
    private Group group;
    private Circle circle;
    private Line line;
    
    public CircleWrapper(double x, double y, double radius, Paint color) {
        super.setColor(color);
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

    public Group getShape() {
        return this.group;
    }

    public void setFill(Color color) {
        circle.setFill(color);
    }

    public CircleWrapper duplicate() {
        return new CircleWrapper(this.circle.getCenterX(), this.circle.getCenterY(), this.circle.getRadius(), super.getColor());
    }

    public Node getNode() {
        return this.group;
    }
}
