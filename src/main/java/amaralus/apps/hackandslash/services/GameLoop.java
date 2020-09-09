package amaralus.apps.hackandslash.services;

public abstract class GameLoop {

    private static final long MS_PER_UPDATE = 10;

    private boolean shouldStop = false;

    public void enable() {
        onEnable();
        shouldStop = false;
        loop();
    }

    public void disable() {
        onDisable();
        shouldStop = true;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void processInput();

    public abstract void update();

    public abstract void render(double timeShift);

    private void loop() {
        long previous = System.currentTimeMillis();
        long lag = 0;
        while (!shouldStop) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            lag += elapsed;

            processInput();

            while (lag >= MS_PER_UPDATE) {
                update();
                lag -= MS_PER_UPDATE;
            }

            render((double) lag / (double) MS_PER_UPDATE);
        }
    }
}
