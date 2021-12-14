package amaralus.apps.hackandslash.graphics.entities.gpu.texture;

import amaralus.apps.hackandslash.common.ValueEnum;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_REPEAT;

public enum TextureWrapMode implements ValueEnum {
    CLAMP(GL_CLAMP),
    REPEAT(GL_REPEAT);

    private final int value;

    TextureWrapMode(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

}
