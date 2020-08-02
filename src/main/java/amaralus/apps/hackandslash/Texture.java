package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.io.FileLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private static final Logger log = LoggerFactory.getLogger(Texture.class);

    private final int width;
    private final int height;
    private final int id;

    public Texture(String name) {
        log.debug("Загрузка текстуры {}", name);
        var imageData = new FileLoadService().loadImageData("sprites/" + name + ".png");

        width = imageData.getWidth();
        height = imageData.getHeight();
        id = glGenTextures();

        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData.getImageBytes());
        glGenerateMipmap(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }
}
