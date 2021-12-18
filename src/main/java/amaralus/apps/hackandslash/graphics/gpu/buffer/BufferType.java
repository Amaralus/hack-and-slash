package amaralus.apps.hackandslash.graphics.gpu.buffer;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public enum BufferType {

    ARRAY_BUFFER(GL_ARRAY_BUFFER),
    ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER);

    private final int type;

    BufferType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
