package amaralus.apps.hackandslash.services;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;

public abstract class GameLoop {

    private final FpsMeter fpsMeter;
    private final long msPerUpdate;
    private boolean shouldStop = false;


    protected GameLoop(long msPerUpdate) {
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
        return fpsMeter.fps;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void processInput();

    public abstract void update();

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
                update();
                lag -= msPerUpdate;
            }

            render((double) lag / (double) msPerUpdate);
            fpsMeter.update();
        }

        onDisable();
    }

    private boolean disableLoop() {
        return shouldStop || getService(Window.class).isShouldClose();
    }

    private static class FpsMeter {

        private int frames = 0;
        private long millis = 0;
        private int fps = 0;

        private void update() {
            frames++;
            if (System.currentTimeMillis() - millis >= 1000) {
                fps = frames;
                frames = 0;
                millis = System.currentTimeMillis();
            }
        }
    }
}
