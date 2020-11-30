package amaralus.apps.hackandslash.graphics.entities.gpu.factory;

import amaralus.apps.hackandslash.graphics.entities.gpu.Texture;
import amaralus.apps.hackandslash.io.FileLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

@Component
public class TextureFactory {

    private static final Logger log = LoggerFactory.getLogger(TextureFactory.class);

    private final FileLoadService fileLoadService;

    public TextureFactory(FileLoadService fileLoadService) {
        this.fileLoadService = fileLoadService;
    }

    public Texture produce(String name) {
        log.debug("Загрузка текстуры {}", name);

        var imageData = fileLoadService.loadImageData("sprites/" + name + ".png");

        var texture = new Texture(glGenTextures(), imageData.getWidth(), imageData.getHeight());
        texture.bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData.getImageBytes());
        glGenerateMipmap(GL_TEXTURE_2D);

        texture.unbind();
        return texture;
    }
}
