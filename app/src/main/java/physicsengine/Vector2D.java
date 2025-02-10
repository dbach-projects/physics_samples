package physicsengine;

public class Vector2D {
    private float x, y;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2D(double x, double y) {
        this.x = (float) x;
        this.y = (float) y; 
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }
    
    public void add(Vector2D v) {
        this.x = this.x + v.getX();
        this.y = this.y + v.getY();
    }
    
    public void sub(Vector2D v) {
        this.x = this.x - v.getX();
        this.y = this.y - v.getY();
    }

    public void mult(float n) {
        this.x = this.x * n;
        this.y = this.y * n;
    }

    public void div(float n) {
        this.x = this.x / n;
        this.y = this.y / n;
    }

    public float mag() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public void normalize() {
        float m = this.mag();
        if (m > 0) {
            this.div(m);
        }
    }

    public float dot(Vector2D a) {
        return a.getX() * this.x + a.getY() * this.y;
    }

    public void limit(float max) {
        if (this.mag() > max) {
            this.normalize();
            this.mult(max);
        }
    }

    public void setMag(float mag) {
        this.normalize();
        this.mult(mag);
    }

    public Vector2D copy() {
        Vector2D v = new Vector2D(this.x, this.y);
        return v;
    }

    public float heading() {
        return (float) Math.atan2(this.getY(),this.getX());
    } 

    /**
     * This method returns a unit vector pointing in a random direction.
     */
    public static Vector2D random2D() {
        float angle = (float) (Math.random() * Math.PI * 2);
        float length = 1;
        return new Vector2D((float) (length * Math.cos(angle)), (float) (length * Math.sin(angle)));
    }

    public static float dot(Vector2D a, Vector2D b) {
        return (float) a.getX() * b.getX() + a.getY() * b.getY();
    }
    
    public static double euclideanDistance(Vector2D a, Vector2D b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }

    public static Vector2D add(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.getX() + v2.getX(), v1.getY() + v2.getY());
        return v3;
    }

    public static Vector2D sub(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.getX() - v2.getX(), v1.getY() - v2.getY());
        return v3;
    }

    public static Vector2D mult(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.getX() * v2.getX(), v1.getY() * v2.getY());
        return v3;
    }

    public static Vector2D div(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.getX() / v2.getX(), v1.getY() / v2.getY());
        return v3;
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y;
    }
}
