package physicsengine.gameloop;

import java.util.function.Consumer;

public class FixedSteps extends GameLoop {
    private final Runnable renderer;
    private final Consumer<Integer> fpsReporter;
    private float secondsElapsedSinceLastFpsUpdate = 0f;
    private int framesSinceLastFpsUpdate = 0;
    private long previousTime = 0;
    private float accumulatedTime = 0;
    private static final float timeStep = 0.0166f;
    
    public FixedSteps(Runnable renderer, Consumer<Integer> fpsReporter)
    {
        this.renderer = renderer;
        this.fpsReporter = fpsReporter;
    }

    @Override
    public void handle(long currentTime) {
        float secondsElapsed = (currentTime - previousTime) / 1e9f; /* nanoseconds to seconds */
        float secondsElapsedCapped = Math.min(secondsElapsed, getMaximumStep());
        accumulatedTime += secondsElapsedCapped;
        previousTime = currentTime;

        while (accumulatedTime >= timeStep) {
            // updater.accept(timeStep);
            accumulatedTime -= timeStep;
        }

        renderer.run();

        secondsElapsedSinceLastFpsUpdate += secondsElapsed;
        framesSinceLastFpsUpdate++;
        if (secondsElapsedSinceLastFpsUpdate >= 0.5f) {
            int fps = Math.round(framesSinceLastFpsUpdate / secondsElapsedSinceLastFpsUpdate);
            fpsReporter.accept(fps);
            secondsElapsedSinceLastFpsUpdate = 0;
            framesSinceLastFpsUpdate = 0;
        }
    }

    @Override
    public void stop()
    {
        previousTime = 0;
        accumulatedTime = 0;
        secondsElapsedSinceLastFpsUpdate = 0f;
        framesSinceLastFpsUpdate = 0;
        super.stop();
    }
    
    @Override
    public String toString()
    {
        return "Fixed time steps";
    }
    
}
