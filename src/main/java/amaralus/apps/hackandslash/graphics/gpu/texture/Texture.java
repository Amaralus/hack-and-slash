package amaralus.apps.hackandslash.graphics.gpu.texture;

import amaralus.apps.hackandslash.graphics.gpu.Bindable;
import amaralus.apps.hackandslash.resources.Resource;

import static org.lwjgl.opengl.GL11.*;

public class Texture implements Resource<String>, Bindable {

    private final String resourceId;
    private final int id;
    private final int width;
    private final int height;

    public Texture(String resourceId, int id, int width, int height) {
        this.resourceId = resourceId;
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

    @Override
    public String getResourceId() {
        return resourceId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}