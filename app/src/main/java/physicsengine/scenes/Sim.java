package physicsengine.scenes;

import javafx.scene.layout.Pane;

public interface Sim {

    public Pane getPane();

    public Runnable getRendererCallback();

    public void stageSim();

}
