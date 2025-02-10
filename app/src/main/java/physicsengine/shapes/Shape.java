package physicsengine.shapes;

import javafx.scene.Node;
import javafx.scene.paint.Paint;

public interface Shape {
    Node getNode();

    Shape duplicate();
}


abstract class BaseShape {
    private Paint color;

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }
}
