package amaralus.apps.hackandslash.graphics.entities.gpu.texture;

import amaralus.apps.hackandslash.common.ValueEnum;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public enum TextureFilter implements ValueEnum {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR),
    CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE);

    private final int value;

    TextureFilter(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
