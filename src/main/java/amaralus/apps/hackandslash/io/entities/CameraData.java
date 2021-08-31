package amaralus.apps.hackandslash.io.entities;

public class CameraData {

    private final float positionX;
    private final float positionY;

    public CameraData(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }
}
