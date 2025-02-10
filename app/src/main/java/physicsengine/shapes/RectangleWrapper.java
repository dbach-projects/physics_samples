package physicsengine.shapes;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class RectangleWrapper extends BaseShape implements Shape {
    private Rectangle rectangle;
    private Paint color;

    
    public RectangleWrapper(double width, double height, Paint color) {
        this.color = color;
        this.rectangle = new Rectangle(width, height, color);
    }

    public void setFill(Color color) {
        this.rectangle.setFill(color);
    }

    public RectangleWrapper duplicate() {
        return new RectangleWrapper(this.rectangle.getWidth(), this.rectangle.getHeight(), this.color);
    }

    public Node getNode() {
        return this.rectangle;
    }
}
