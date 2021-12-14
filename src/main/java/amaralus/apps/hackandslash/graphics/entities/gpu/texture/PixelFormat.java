package amaralus.apps.hackandslash.graphics.entities.gpu.texture;

import amaralus.apps.hackandslash.common.ValueEnum;

import static org.lwjgl.opengl.GL11.*;

public enum PixelFormat implements ValueEnum {
    RGB(GL_RGB),
    RGBA(GL_RGBA),
    RED(GL_RED),
    GREEN(GL_GREEN),
    BLUE(GL_BLUE),
    ALPHA(GL_ALPHA);

    private final int value;

    PixelFormat(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
