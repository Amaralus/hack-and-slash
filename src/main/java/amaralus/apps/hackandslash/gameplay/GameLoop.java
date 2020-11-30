package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.utils.FpsMeter;

public abstract class GameLoop {

    private final Window window;
    private final long msPerUpdate;
    private boolean shouldStop = false;

    private final FpsMeter fpsMeter;

    protected GameLoop(Window window, long msPerUpdate) {
        this.window = window;
        this.msPerUpdate = msPerUpdate;
        fpsMeter = new FpsMeter();
    }

    public void enable() {
        shouldStop = false;
        loop();
    }

    public void disable() {
        shouldStop = true;
    }

    public int getFps() {
        return fpsMeter.getFps();
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void processInput();

    public abstract void update(long elapsedTime);

    public abstract void render(double timeShift);

    private void loop() {
        onEnable();

        long previous = System.currentTimeMillis();
        long lag = 0;
        while (!disableLoop()) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            processInput();

            while (lag >= msPerUpdate) {
                update(msPerUpdate);
                lag -= msPerUpdate;
            }

            render((double) lag / (double) msPerUpdate);
            fpsMeter.update();
        }

        onDisable();
    }

    private boolean disableLoop() {
        return shouldStop || window.isShouldClose();
    }
}
