package physicsengine.noise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import physicsengine.Vector2D;

public class PerlinNoise {
    private static boolean DEBUG = false; // Set to true for debugging printouts
    
    private static int[] permutation = { 151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180,151,
            160,137,91,90,15,131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,  8,
            99,37,240,21,10,23,190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,
            35,11,32,57,177,33,88,237,149,56,87,174,20,125,136,171,168,68,175,74,165,71,
            134,139,48,27,166,77,146,158,231,83,111,229,122,60,211,133,230,220,105,  92,
            41,55,46,245,40,244,102,143,54,65,25,63,161, 1,216,80,73,209,76,132,187,208,
            89,18,169,200,196,135,130,116,188,159, 86,164,100,109,198,173,186,  3,64,52,
            217,226,250,124,123,5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,
            58,17,182,189,28,42,223,183,170,213,119,248,152,2,44,154,163,70,221,153,101,
            155,167,43,172,9,129,22,39,253,19,98,108,110,79,113,224,232,178,185,112,104,
            218,246,97,228,251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,
            249,14,239,107,49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,
            127, 4,150,254,138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,
            61, 156, 180 };

    public PerlinNoise() {

    }

    /*
     * PERLIN 2D
     * https://github.com/lwilbur/Perlin_2D/blob/main/Perlin2D.java
     * https://thebookofshaders.com
     * https://adrianb.io/2014/08/09/perlinnoise.html
     */

    /**
     * PerlinNoise2D returns a List of List<Double> of Perlin Noise values in the range of [0,1].
     * @param imgWidth The width of the WritableImage and the drawn Perlin Noise.
     * @param imgHeight The height of the WritableImage and the drawn Perlin Noise.
     * @param seed The seed used to shuffle the permutation array,
     * which is used like a psudorandom number generator in the range of [0, 255]
     * @param pxPerGrid The size of the squares used to divide the imaga area up for the
     * Perlin Noise calculation.
     * @return A WritableImage containing Perlin Noise
     */
    public List<List<Double>> perlinNoise2D(int imgWidth, int imgHeight, long seed, int pxPerGrid, double persistence) {
        PerlinNoise.setSeed(seed);
        List<List<Double>> noise = new ArrayList<List<Double>>();

        for (int x = 0; x < imgWidth; x++) {
            List<Double> yNoise = new ArrayList<Double>();
            for (int y = 0; y < imgHeight; y++) {

                // Calculate where the current point falls in the grid of
                // PX_PER_GRID-wide squares
                double xInGrid = (double) x / pxPerGrid;
                double yInGrid = (double) y / pxPerGrid;

                double val = 0.0;
                double frequency = 1;
                double amplitude = 1;
                double maxValue = 0;
                int octave = 8;

                for (int o = 0; o < octave; o++) {
                    val += this.calcPerlinNoise2D(xInGrid * frequency, yInGrid * frequency) * amplitude;

                    maxValue += amplitude;

                    amplitude *= persistence;
                    frequency *= 2;
                }

                val = val / maxValue;

                yNoise.add(val);
            }
            noise.add(yNoise);
        }
        
        return noise;
    }

    /**
     * PerlinNoise2DWritableImage returns a WritableImage that has had Perlin Noise drawn to it.
     * @param imgWidth The width of the WritableImage and the drawn Perlin Noise.
     * @param imgHeight The height of the WritableImage and the drawn Perlin Noise.
     * @param seed The seed used to shuffle the permutation array,
     * which is used like a psudorandom number generator in the range of [0, 255]
     * @param pxPerGrid The size of the squares used to divide the imaga area up for the
     * Perlin Noise calculation.
     * @return A WritableImage containing Perlin Noise
     */
    public WritableImage perlinNoise2DWritableImage(int imgWidth, int imgHeight, long seed, int pxPerGrid,  double persistence) {
        PerlinNoise.setSeed(seed);
        WritableImage perlinNoiseImage = new WritableImage(imgWidth, imgHeight);

        List<List<Double>> noise = this.perlinNoise2D(imgWidth, imgHeight, seed, pxPerGrid, persistence);

        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                Color c = this.getGreyscaleColor(noise.get(x).get(y));
                perlinNoiseImage.getPixelWriter().setColor(x, y, c);
            }
        }

        return perlinNoiseImage;
    }

    /**
     * Returns a Perlin noise value for a given (x, y) coordinate.
     * @param x x-coordinate to generate noise value for
     * @param y y-coordinate to generate noise value for
     * @return double value in range [0.0, 1.0] for that point
     */
    public double calcPerlinNoise2D(double x, double y) {
        // get unit square
        Vector2D[] unitSquare = getSquareCoords(x, y);
        double[] dotProds = new double[4];  // stores each gradiant • distance val

        // For each corner of the unit square
        for (int i = 0; i < unitSquare.length; i++) {
            Vector2D corner = unitSquare[i];

            // Calculate distance vector and grad vector based on given point
            Vector2D distVec = new Vector2D((float)(x - corner.getX()), (float)(y - corner.getY()));
            Vector2D gradVec = selectGradVector((int) corner.getX(), (int) corner.getY());

            dotProds[i] = Vector2D.dot(gradVec, distVec);

            if (DEBUG) {
                System.out.println(String.format("corner: " + corner.toString()));
                System.out.println(String.format("gradVec: " + gradVec.toString()));
                System.out.println("dotProds: " + dotProds[i]);
            }

        }

        double u = this.fade(x - (int)x);
        double v = this.fade(y - (int)y);

        double top = Interpolate.linear(u, dotProds[0], dotProds[1]);
        double bot = Interpolate.linear(u, dotProds[2], dotProds[3]);
        double vert = Interpolate.linear(v, top, bot);

        if (DEBUG) {
            System.out.println(
                    "\txFrac=" + u + "\n\ttop=" + top + "\n\tbot=" + bot + "\n\tyFrac=" + v + "\n\tvert=" + vert);

            System.out.println("result:  " + (vert + 1) / 2);
        }

        return (vert + 1) / 2; // Shift from output range of [-1, 1] to [0, 1]
    }

    /**
     * Changes hash table from Ken Perlin's default to a shuffled form of it,
     * allowing for many different outputs
     * @param seed a long, which decides how the hash lookup table will be
     *             shuffled
     */
    private static void setSeed(long seed) {
        // convert hash table to list (allows for shuffling)
        ArrayList<Integer> permutationList = new ArrayList<Integer>();
        for (int i = 0; i < permutation.length; i++)
            permutationList.add(permutation[i]);

        Collections.shuffle(permutationList, new Random(seed));

        // overwrite hash table
        for (int i = 0; i < permutation.length; i++)
            permutation[i] = permutationList.get(i);
    }

    /**
     * For any given (x, y) coordinate, pseudorandomly select a gradient vector
     * using the hash method.
     * @param x integer x-coordinate of grid to generate a gradient vector for
     * @param y integer y-coordinate of grid to generate a gradient vector for
     * @return The gradient vector for that point, in the format {x, y}, where
     *         the vector is measured with its tail at (0, 0) and its head at
     *         (x, y).
     */
    public static Vector2D selectGradVector(int x, int y) {
        final Vector2D[] validGradientVecs = { new Vector2D( 1, 1 ), new Vector2D( -1, 1 ), new Vector2D( 1, -1 ), new Vector2D( -1, -1 ) };
        return validGradientVecs[hash(x, y) & 3]; // same as % 4, but faster
    }
    
    /**
     * Hashing function based on the function given in
     * https://www.scratchapixel.com/lessons/procedural-generation-virtual-worlds/perlin-noise-part-2,
     * which is based on Ken Perlin's original.
     * @param x x-coordinate of point to generate hash for
     * @param y y-coordinate of point to generate hash for
     * @return value in range [0, 255] from hash table
     */
    private static int hash(int x, int y) {
        x = x & 255;  // same as % 256, but faster
        y = y & 255;
        return permutation[permutation[x] + y];
    }
    
    /**
     * Determine the coordinates of the 4 corners of a given point's unit square
     * @param x x-coordinate of point to find unit square of
     * @param y y-coordinate of point to find unit square of
     * @return list of coordinate pairs in the format {{x0, y0}, {x1, y0}, {x0, y1}, {x1, y1}}
     *         (that is, {{top lt}, {top rt}, {bot lt}, {bot rt}})
     */
    private static Vector2D[] getSquareCoords(double x, double y) {
        int x0, x1, y0, y1;  // (x0,y0) is up-left corner, (x1,y1) is bot-right
        x0 = (int)x;
        y0 = (int)y;
        x1 = x0 + 1;
        y1 = y0 + 1;

        Vector2D[] coords = { new Vector2D(x0, y0), new Vector2D(x1, y0), new Vector2D(x0, y1), new Vector2D(x1, y1) };
        return coords;
    }

    /**
     * Fade function to smooth out each coordinate towards integral values,
     * making it look more natural and appealing. Function originally defined
     * by Ken Perlin.
     * @param val coordinate to be smoothed, represented in the range [0.0, 1.0]
     *            as its location in its unit square (e.g. .25 if you're fading
     *            a point which is 25% of the way across its unit square)
     * @return smoothed value for the coordinate at that point in range [0.0, 1.0]
     */
    private double fade(double val) {
        return ((6 * val - 15) * val + 10) * val * val * val;
    }


    /*
     * PERLIN 1D
     * https://blog.oliverbalfour.com/javascript/2016/03/19/1d-perlin-noise.html
     */

    /**
     * PerlinNoise1D Uses the private helper functions to assemble and return 1D Perlin Noise.
     * This is the public function that should be called by the developer
     * @param amplitude The amplitude is the distance from the top (or highest possible value)
     *                  to the bottom (or lowest possible value) of the wave.
     * @param wavelength The wavelength is the distance from the peak of one wave to the peak of the next.
     * @param octives How many octives should be generated.
     * @param divisor How "fine" of detail should be generated from the previous octives curve
     * @param width How long should the noise curve be. Usually the screen width.
     * @return List of values for noise
     */
    public List<Double> perlinNoise1D(double amplitude, double wavelength, double octaves, int divisor, int width) {
        List<List<Double>> octives = this.generateNoiseOctives1D(amplitude, wavelength, octaves, divisor, width);
        List<Double> noise = this.combineNoise1D(octives);
        return noise;
    }

    /**
     * GenerateNoiseOctives1D generates a List of Perlin Noise for each octive.
     * For each octive the amplitude and wavelength are reduced by the amount of the divisor
     * e.g. (amplitude / divisor) (wavelength / divisor) thereby creating finer and finer variance with each octive.
     * @param amplitude The amplitude is the distance from the top (or highest possible value)
     *                  to the bottom (or lowest possible value) of the wave.
     * @param wavelength The wavelength is the distance from the peak of one wave to the peak of the next.
     * @param octives How many octives should be generated.
     * @param divisor How "fine" of detail should be generated from the previous octives curve
     * @param width How long should the noise curve be. Usually the screen width.
     * @return List of values for noise
     */
    private List<List<Double>> generateNoiseOctives1D(double amplitude, double wavelength, double octaves, double divisor, int width) {
        List<List<Double>> result = new ArrayList<List<Double>>();
        for (int i = 0; i < octaves; i++) {
            result.add(this.calcPerlinNoise1D(amplitude, wavelength, width));
            amplitude /= divisor;
            wavelength /= divisor;
        }

        return result;
    }
    
    /**
     * CombineNoise1D takes in a List of Perlin Noise octive Lists and combines them
     * into a single value allowing for additional detail to the noise curve.
     * @param noise A List of Perlin Noise Lists.
     * @return List of values for noise
     */
    private List<Double> combineNoise1D(List<List<Double>> noise) {
        List<Double> result = new ArrayList<Double>();
        List<Double> pos = noise.getFirst();

        for (int i = 0; i < pos.size(); i++) {
            double total = 0;
            for (int j = 0; j < noise.size(); j++) {
                total += noise.get(j).get(i);
            }
            result.add(total);
        }

        return result;
    }
    
    /**
     * CalcPerlinNoise1D calculates Perlin noise in one dimension.
     * Utilizes a table of predefined values [0, 255] as a psudorandom number generator.
     * @param amplitude The amplitude is the distance from the top (or highest possible value)
     *                  to the bottom (or lowest possible value) of the wave.
     * @param wavelength The wavelength is the distance from the peak of one wave to the peak of the next.
     * @return List of values for noise in the range of [0, amplitude]
     */
    private List<Double> calcPerlinNoise1D(double amplitude, double wavelength, int width) {
        double x = 0;
        double amp = amplitude;
        double wl = wavelength;
        int aRand = (int) (Math.random() * 255);
        int bRand = (int) (Math.random() * 255);
        double a = (double) permutation[aRand] / 255;
        double b = (double) permutation[bRand] / 255;
        List<Double> pos = new ArrayList<Double>();

        while (x < width) {
            if (x % wl == 0) {
                a = b;
                int bRand2 = (int) (Math.random() * 255);
                b = (double) permutation[bRand2] / 255;

                pos.add(a * amp);
            } else {
                pos.add(Interpolate.linear(((x % wl) / wl), a, b)  * amp);
            }
            x++;
        }

        return pos;
    }
    

    /*
     * HELPER FUNCTIONS
     */

    /**
     * GetGreyscaleColor takes in a value [0,1] and returns a Greyscale Color
     * @param val A value [0,1]
     * @return A Greyscale Color 
     */
    private Color getGreyscaleColor(double val) {
        int val256 = (int)(255 * val);   // map range [0, 1] to [0, 255]
        return Color.grayRgb(val256);
    }
}