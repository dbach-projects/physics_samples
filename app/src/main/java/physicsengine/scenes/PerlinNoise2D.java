package physicsengine.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import physicsengine.simulation.Body;
import physicsengine.noise.PerlinNoise;
import physicsengine.Debouncer;

public class PerlinNoise2D implements Sim {
    private Pane pane;
    private GridPane controls;
    private GraphicsContext gc;
    private final Debouncer debouncer = new Debouncer();
    private List<Body> bodyItems = new ArrayList<Body>();


    public PerlinNoise2D(int imgWidth, int imgHeight) {
        // Create a canvas to draw on, and get its GraphicsContext.
        Canvas canvas = new Canvas(imgWidth, imgHeight);
        gc = canvas.getGraphicsContext2D();

        /* CREATE PANE */
        this.pane = new AnchorPane(canvas);
        this.pane.setPrefSize(imgWidth, imgHeight);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        // add interface items
        this.controls = new GridPane();
        controls.setPadding(new Insets(10, 10, 10, 10));

        Label seedLabel = new Label("seed");
        Slider seed = slider(1, 5000, 5000);
        controls.add(seedLabel, 0, 0);
        controls.add(seed, 0, 1);

        Label gridSizeLabel = new Label("grid size");
        Slider gridSize = slider(1, 600, 50);
        controls.add(gridSizeLabel, 0, 2);
        controls.add(gridSize, 0, 3);

        Label amplitudeLabel = new Label("amplitude");
        Slider amplitude = slider(0, 256, 128);
        controls.add(amplitudeLabel, 0, 4);
        controls.add(amplitude, 0, 5);

        Label wavelengthLabel = new Label("wavelength");
        Slider wavelength = slider(0, 128, 64);
        controls.add(wavelengthLabel, 0, 6);
        controls.add(wavelength, 0, 7);

        Label octivesLabel = new Label("octives");
        Slider octives = slider(1, 16, 8);
        controls.add(octivesLabel, 0, 8);
        controls.add(octives, 0, 9);

        AnchorPane.setTopAnchor(controls, 25.0);
        AnchorPane.setLeftAnchor(controls, 0.0);

        this.pane.getChildren().addAll(controls);

        /* INTERFACE ITEM LISTENERS */
        seed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                debouncer.debounce(Void.class, new Runnable() {

                    @Override
                    public void run() {
                        constructNoise(amplitude.valueProperty().intValue(), wavelength.valueProperty().intValue(),
                                octives.valueProperty().intValue(), newValue.longValue(), 50, 1.2, imgWidth, imgHeight);
                    }

                }, 300, TimeUnit.MILLISECONDS);

            }
        });

        gridSize.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                debouncer.debounce(Void.class, new Runnable() {

                    @Override
                    public void run() {
                        constructNoise(amplitude.valueProperty().intValue(), wavelength.valueProperty().intValue(),
                                octives.valueProperty().intValue(), seed.valueProperty().longValue(), newValue.intValue(), 1.2, imgWidth, imgHeight);
                    }

                }, 300, TimeUnit.MILLISECONDS);

            }
        });
        
        amplitude.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                debouncer.debounce(Void.class, new Runnable() {

                    @Override
                    public void run() {
                        constructNoise(newValue.intValue(), wavelength.valueProperty().intValue(),
                                octives.valueProperty().intValue(), 50932, 50, 1.2, imgWidth, imgHeight);
                    }

                }, 300, TimeUnit.MILLISECONDS);

            }
        });
        wavelength.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                debouncer.debounce(Void.class, new Runnable() {

                    @Override
                    public void run() {
                        constructNoise(amplitude.valueProperty().intValue(), newValue.intValue(),
                                octives.valueProperty().intValue(), 50932, 50, 1.2, imgWidth, imgHeight);
                    }
                    
                }, 300, TimeUnit.MILLISECONDS);

            }
        });
        octives.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                debouncer.debounce(Void.class, new Runnable() {

                    @Override
                    public void run() {
                        constructNoise(amplitude.valueProperty().intValue(), wavelength.valueProperty().intValue(),
                                newValue.intValue(), 50932, 50, 1.2, imgWidth, imgHeight);
                    }
                    
                }, 300, TimeUnit.MILLISECONDS);

            }
        });

        this.constructNoise(1.0, 1.0, 8, 50932, 50, 1.2, imgWidth, imgHeight);
    }
    
    private Slider slider(int min, int max, int val) {
        Slider slider = new Slider(min, max, val);
        slider.setValue(val);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);

        return slider;
    }

    private void constructNoise(double frequency, double amplitude, double octaves, long seed, int pxPerGrid,
            double persistence, int imgWidth, int imgHeight) {
        gc.clearRect(0, 0, imgWidth, imgHeight);
        long startTime = System.nanoTime();
        PerlinNoise pn = new PerlinNoise();
        WritableImage img = pn.perlinNoise2DWritableImage(frequency, amplitude, octaves, seed, pxPerGrid, persistence, imgWidth, imgHeight);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Method execution time: " + executionTime + "ns");

        // Draw the generated image on the canvas.
        gc.drawImage(img, 0, 0);
    }

    @Override
    public void stageSim() {
        for (Body body : this.bodyItems) {
            this.pane.getChildren().add(body.getNode());
        }
    }

    @Override
    public Runnable getRendererCallback() {
        return () -> {

            for (Body body : this.bodyItems) {
                body.run();
            }

        };
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
