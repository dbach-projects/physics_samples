package physicsengine;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static int constrain(int val, int min_val, int max_val) {
        return Math.min(max_val, Math.max(min_val, val));
    }

    public static double constrain(double val, double min_val, double max_val) {
        return Math.min(max_val, Math.max(min_val, val));
    }

    public static List<Double> remapArray(List<Double> value, double start1, double stop1, double start2, double stop2) {
        List<Double> remapped = new ArrayList<>();
        for (int x = 0; x < value.size(); x++) {
            remapped.add(((value.get(x) - start1) / (stop1 - start1)) * (stop2 - start2) + start2); 
        }
        return remapped;
    }
}

