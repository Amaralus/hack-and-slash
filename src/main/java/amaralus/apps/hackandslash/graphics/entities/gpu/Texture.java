package amaralus.apps.hackandslash.graphics.entities.gpu;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.graphics.entities.Bindable;

import static org.lwjgl.opengl.GL11.*;

public class Texture implements Bindable, Destroyable {

    private final int id;
    private final int width;
    private final int height;

    public Texture(int id, int width, int height) {
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
}
