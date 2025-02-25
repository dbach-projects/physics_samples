package physicsengine.simulation;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import physicsengine.Vector2D;
import physicsengine.shapes.CircleWrapper;
import physicsengine.shapes.RectangleWrapper;
import physicsengine.shapes.WrapperShape;

public class LiquidBody extends Body {
    private int x, y, w, h;
    private WrapperShape shape;
    
    public LiquidBody(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.shape = new RectangleWrapper(w, h, Color.rgb(0, 153, 255, 0.39));
        this.shape.getNode().setLayoutX(this.x);
        this.shape.getNode().setLayoutY(this.y);
    }

    public boolean contains(Body body) {
        Vector2D pos = body.getPosition();

        return (pos.getX() > this.x && pos.getX() < this.x + this.w &&
                pos.getY() > this.y && pos.getY() < this.y + this.h);
    }

    @Override
    public boolean contactEdge(float width, float height) {
        //TODO: check this implementation
        if (this.shape instanceof CircleWrapper) {
            double radius = ((CircleWrapper) shape).getRadius();
            return (super.getPosition().getY() > height - radius - 1);
        }
        return false;
    }

    @Override
    public void run() {
        this.update();
        this.draw(this.shape);
    }

    @Override
    public Node getNode() {
        return this.shape.getNode();
    }

    public Shape getShape() {
        return this.shape.getShape();
    }
}
