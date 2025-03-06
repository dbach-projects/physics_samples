package physicsengine.noise;

public class Interpolate {

    /**
     * Perform linear interpretation between points a and b, given that a and b
     * are points on a unit square's border, and the target point is frac % of
     * the way across the square from point a to b.
     * @param frac what % of the way across the square the target point is,
     *             represented with a double in the range [0.0, 1.0]
     * @param a a point on a unit square, < b
     * @param b a point on a unit square, > a
     * @return linearly interpolated value between a and b
     */
    static double linear(double frac, double a, double b) {
        return a + (frac * (b - a));
    }
    
    static double quadratic(double a0, double a1, double w) {
        return (a1 - a0) * (3.0 - w * 2.0) * w * w + a0;
    }

}
