package amaralus.apps.hackandslash.services;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;

public abstract class GameLoop {

    private final long msPerUpdate;
    private boolean shouldStop = false;

    protected GameLoop(long msPerUpdate) {
        this.msPerUpdate = msPerUpdate;
    }

    public void enable() {
        shouldStop = false;
        loop();
    }

    public void disable() {
        shouldStop = true;
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
            lag += elapsed;

            processInput();

            while (lag >= msPerUpdate) {
                update();
                lag -= msPerUpdate;
            }

            render((double) lag / (double) msPerUpdate);
        }

        onDisable();
    }

    private boolean disableLoop() {
        return shouldStop || getService(Window.class).isShouldClose();
    }
}
