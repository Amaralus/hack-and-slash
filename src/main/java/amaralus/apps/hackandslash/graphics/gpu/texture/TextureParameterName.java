package amaralus.apps.hackandslash.graphics.gpu.texture;

import static org.lwjgl.opengl.GL11.*;

public enum TextureParameterName {
    MAG_FILTER(GL_TEXTURE_MAG_FILTER),
    MIN_FILTER(GL_TEXTURE_MIN_FILTER),
    WRAP_S(GL_TEXTURE_WRAP_S),
    WRAP_T(GL_TEXTURE_WRAP_T);

    private final int value;

    TextureParameterName(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
