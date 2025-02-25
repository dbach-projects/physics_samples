package physicsengine;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static float RESTITUTION = 0.75f;


    public static float constrain(float val, float min_val, float max_val) {
        return Math.max(Math.min(val, max_val), min_val);
    }

    public static double normalize(double value, double minA, double maxA, double minB, double maxB) {
        double newval = (value - minA) / (maxA - minA) * (maxB - minB) + minB;

        if (minB < maxB) {
            return Common.constrain((float)newval, (float)minB, (float)maxB);
        } else {
            return Common.constrain((float)newval, (float)maxB, (float)minB);
        }
    }

    public static List<Double> remapArray(List<Double> value, double start1, double stop1, double start2, double stop2) {
        List<Double> remapped = new ArrayList<>();
        for (int x = 0; x < value.size(); x++) {
            remapped.add(((value.get(x) - start1) / (stop1 - start1)) * (stop2 - start2) + start2); 
        }
        return remapped;
    }
}

