package amaralus.apps.hackandslash.graphics.entities.gpu;

import amaralus.apps.hackandslash.graphics.entities.Bindable;
import amaralus.apps.hackandslash.resources.Resource;

import static org.lwjgl.opengl.GL11.*;

public class Texture extends Resource implements Bindable {

    private final int id;
    private final int width;
    private final int height;

    public Texture(String resourceName, int id, int width, int height) {
        super(resourceName);
        this.id = id;
        this.width = width;
        this.height = height;
    }

    @Override
    public void destroy() {
        glDeleteTextures(id);
    }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    @Override
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public int id() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public enum Filter {
        NEAREST(GL_NEAREST),
        LINEAR(GL_LINEAR);

        private final int value;

        Filter(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum WrapMode {
        CLAMP(GL_CLAMP),
        REPEAT(GL_REPEAT);

        private final int value;

        WrapMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ParameterName {
        MAG_FILTER(GL_TEXTURE_MAG_FILTER),
        MIN_FILTER(GL_TEXTURE_MIN_FILTER),
        WRAP_S(GL_TEXTURE_WRAP_S),
        WRAP_T(GL_TEXTURE_WRAP_T);

        private final int value;

        ParameterName(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}