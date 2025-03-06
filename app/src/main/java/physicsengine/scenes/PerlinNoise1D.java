package physicsengine.scenes;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import physicsengine.simulation.Body;
import physicsengine.simulation.SolidBody;
import physicsengine.noise.PerlinNoise;
import physicsengine.shapes.PolylineWrapper;

public class PerlinNoise1D implements Sim {
    private Pane pane;
    GridPane controls;
    private List<Body> bodyItems = new ArrayList<Body>();


    public PerlinNoise1D(int width, int height) {
        /* CREATE PANE */
        this.pane = new AnchorPane();
        this.pane.setPrefSize(width, height);
        this.pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // add interface items
        this.controls = new GridPane();
        controls.setPadding(new Insets(10, 10, 10, 10));

        Label amplitudeLabel = new Label("amplitude");
        Slider amplitude = slider(0, 256, 128);
        controls.add(amplitudeLabel, 0, 0);
        controls.add(amplitude, 0, 1);

        Label wavelengthLabel = new Label("wavelength");
        Slider wavelength = slider(0, 128, 64);
        controls.add(wavelengthLabel, 0, 2);
        controls.add(wavelength, 0, 3);

        Label octivesLabel = new Label("octives");
        Slider octives = slider(1, 16, 8);
        controls.add(octivesLabel, 0, 4);
        controls.add(octives, 0, 5);

        AnchorPane.setTopAnchor(controls, 25.0);
        AnchorPane.setLeftAnchor(controls, 0.0);

        this.pane.getChildren().addAll(controls);

        /* INTERFACE ITEM LISTENERS */
        amplitude.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                constructNoise(newValue.intValue(), wavelength.valueProperty().intValue(), octives.valueProperty().intValue(), 2, width, height);
            }
        });
        wavelength.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                constructNoise(amplitude.valueProperty().intValue(), newValue.intValue(), octives.valueProperty().intValue(), 2, width, height);
            }
        });
        octives.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                constructNoise(amplitude.valueProperty().intValue(), wavelength.valueProperty().intValue(), newValue.intValue(), 2, width, height);
            }
        });

        /* CONSTRUCT 1D NOISE */
        this.constructNoise(128, 128, 8, 2, width, height);
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
    
    private void constructNoise(int amplitude, int wavelength, int octives, int divisor, int width, int height) {
        PerlinNoise pn = new PerlinNoise();
        List<Double> points = pn.perlinNoise1D(amplitude, wavelength, octives, divisor, width);
        List<Double> result = new ArrayList<Double>();

        //perlin noise is outputed but it still needs to be spread out along the x axis effectivly turning them into points
        for (var i = 0; i < points.size(); i++) {
            double xpos = i;
            // subtract amplitude to reorient to center of Y axis
            double ypos = (height / 2) + points.get(i) - 128;
            result.add(xpos);
            result.add(ypos);
        }

        PolylineWrapper line = new PolylineWrapper(result, 3f, Color.GREEN);
        Body lineBody = new SolidBody(0, 0, 1, 3, 0, -1, line);

        // clear old nodes
        for (Body body : this.bodyItems) {
            this.pane.getChildren().remove(body.getNode());
        }
        this.bodyItems = new ArrayList<Body>();

        this.bodyItems.add(lineBody);

        this.stageSim();
    }

    @Override
    public void stageSim() {
        for (Body body : this.bodyItems) {
            if (!this.pane.getChildren().contains(body.getNode())) {
                this.pane.getChildren().add(body.getNode());
            }
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
