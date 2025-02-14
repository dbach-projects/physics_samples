package physicsengine.shapes;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RectangleWrapper extends BaseShape implements WrapperShape {
    private Rectangle rectangle;

    
    public RectangleWrapper(double width, double height, Paint color) {
        this.rectangle = new Rectangle(width, height, color);
    }

    public void setFill(Color color) {
        this.rectangle.setFill(color);
    }

    @Override
    public Node getNode() {
        return this.rectangle;
    }

    @Override
    public Shape getShape() {
        return this.rectangle;
    }
}
