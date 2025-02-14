package physicsengine.shapes;

import javafx.scene.Node;
import javafx.scene.shape.Shape;

public interface WrapperShape {
    Node getNode();

    Shape getShape();
}

abstract class BaseShape {

}
