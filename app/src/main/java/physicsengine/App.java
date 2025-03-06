package physicsengine;

import java.util.function.Consumer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import physicsengine.gameloop.FixedSteps;
import physicsengine.gameloop.GameLoop;
import physicsengine.scenes.ArriveAtTargetSim;
import physicsengine.scenes.FlockingSim;
import physicsengine.scenes.FlowFieldSim;
import physicsengine.scenes.PerlinNoise1D;
import physicsengine.scenes.PerlinNoise2D;
import physicsengine.scenes.GravitySim;
import physicsengine.scenes.ParticleSim;
import physicsengine.scenes.PendulumSim;
import physicsengine.scenes.Sim;
import physicsengine.scenes.SpringSim;

public class App extends Application {
    /* CONSTANTS */
    int WINDOW_WIDTH = 600;
    int WINDOW_HEIGHT = 600;

    Scene scene;
    Sim selectedSim;
    Runnable renderer;
    GameLoop gameLoop;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* CONTROLS */
        final Label fpsLabel = new Label();
        Consumer<Integer> fpsReporter = fps -> fpsLabel.setText(String.format("FPS: %d", fps));
        ChoiceBox<String> sceneSelector = new ChoiceBox<String>();
        ObservableList<String> scenes = FXCollections.observableArrayList();

        /* CREATE SCENES */
        scenes.addAll("Flocking", "FlowField","PerlinNoise2D","PerlinNoise1D", "ArriveAtSim", "GravitySim", "ParticleSim", "SpringSim", "PendulumSim");
        sceneSelector.setItems(scenes);
        sceneSelector.getSelectionModel().select(scenes.get(0));

        /* LAY OUT PANES */
        AnchorPane root = new AnchorPane();

        // controls
        GridPane controls = new GridPane();
        controls.add(sceneSelector, 0, 0);

        AnchorPane.setTopAnchor(controls, 0.0);
        AnchorPane.setLeftAnchor(controls, 0.0);
        AnchorPane.setTopAnchor(fpsLabel, 10.0);
        AnchorPane.setRightAnchor(fpsLabel, 10.0);

        root.getChildren().addAll(controls, fpsLabel);

 
        /* CHOICEBOX ACTION */
        sceneSelector.setOnAction(event -> {
            gameLoop.stop();
            selectedSim = setSim(root, sceneSelector, selectedSim);
            renderer = selectedSim.getRendererCallback();
            selectedSim.stageSim();
            gameLoop = new FixedSteps(renderer, fpsReporter);
            gameLoop.start();
        });

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Physics Samples");
        primaryStage.setResizable(false);
        primaryStage.show();

        // stage the simulation
        selectedSim = setSim(root, sceneSelector, selectedSim);
        renderer = selectedSim.getRendererCallback();
        selectedSim.stageSim();

        gameLoop = new FixedSteps(renderer, fpsReporter);
        gameLoop.start();
    }

    public Sim setSim(AnchorPane anchorPane, ChoiceBox<String> sceneSelector, Sim oldSim) {
        String selectedScene = sceneSelector.getValue();
        Sim selectedSim;

        switch (selectedScene) {
            case "Flocking":
                selectedSim = new FlockingSim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "FlowField":
                selectedSim = new FlowFieldSim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "PerlinNoise2D":
                selectedSim = new PerlinNoise2D(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "PerlinNoise1D":
                selectedSim = new PerlinNoise1D(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "GravitySim":
                selectedSim = new GravitySim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "ParticleSim":
                selectedSim = new ParticleSim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "SpringSim":
                selectedSim = new SpringSim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "PendulumSim":
                selectedSim = new PendulumSim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            case "ArriveAtSim":
                selectedSim = new ArriveAtTargetSim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;

            default:
                selectedSim = new GravitySim(WINDOW_WIDTH, WINDOW_HEIGHT);
                break;
        }

        // remove old pane
        if (oldSim != null) {
            Pane oldPane = oldSim.getPane();
            anchorPane.getChildren().remove(oldPane);
        }

        // add new pane
        Pane selectedPane = selectedSim.getPane();
        anchorPane.getChildren().add(0, selectedPane);

        return selectedSim;
    }
    
    public static void main(String[] args) {
        launch();
    }
}