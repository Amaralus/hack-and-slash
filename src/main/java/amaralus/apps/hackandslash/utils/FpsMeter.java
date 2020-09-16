package amaralus.apps.hackandslash.utils;

public class FpsMeter {

    private int frames = 0;
    private long millis = 0;
    private int fps = 0;

    public void update() {
        frames++;
        if (System.currentTimeMillis() - millis >= 1000) {
            fps = frames;
            frames = 0;
            millis = System.currentTimeMillis();
        }
    }

    public int getFps() {
        return fps;
    }
}
