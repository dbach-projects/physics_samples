/*
 * https://github.com/svanimpe/fx-game-loops/blob/master/src/main/java/svanimpe/fxgameloop/Main.java
 */

package physicsengine.gameloop;

import javafx.animation.AnimationTimer;

public abstract class GameLoop extends AnimationTimer {

    private float maximumStep = Float.MAX_VALUE;

    public float getMaximumStep() {
        return maximumStep;
    }

    public void setMaximumStep(float maximumStep) {
        this.maximumStep = maximumStep;
    }
    
}
