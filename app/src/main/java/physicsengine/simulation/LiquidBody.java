package physicsengine.simulation;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import physicsengine.Vector2D;
import physicsengine.shapes.RectangleWrapper;
import physicsengine.shapes.Shape;

public class LiquidBody extends BaseBody implements Body {
    private int x, y, w, h;
    private double c;
    private Shape shape;
    
    public LiquidBody(int x, int y, int w, int h, double c) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.c = c;

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
    public void run() {
        this.update();
        this.draw(this.shape);
    }

    @Override
    public boolean contactEdge(float paneWidth, float paneHeight) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contactEdge'");
    }

    @Override
    public void bounceEdges(float paneWidth, float paneHeight) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bounceEdges'");
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public Node getNode() {
        return this.shape.getNode();
    }

    @Override
    public Shape getShape() {
        return this.shape;
    }
}
