package amaralus.apps.hackandslash.graphics.entities.gpu.factory;

import amaralus.apps.hackandslash.graphics.entities.gpu.Texture;
import amaralus.apps.hackandslash.io.FileLoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

import static amaralus.apps.hackandslash.graphics.entities.gpu.Texture.Filter.LINEAR;
import static amaralus.apps.hackandslash.graphics.entities.gpu.Texture.Filter.NEAREST;
import static amaralus.apps.hackandslash.graphics.entities.gpu.Texture.ParameterName.*;
import static amaralus.apps.hackandslash.graphics.entities.gpu.Texture.WrapMode.REPEAT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

@Component
@Slf4j
public class TextureFactory {

    private final FileLoadService fileLoadService;

    public TextureFactory(FileLoadService fileLoadService) {
        this.fileLoadService = fileLoadService;
    }

    public Texture produce(String name) {
        log.debug("Загрузка текстуры {}", name);

        var imageData = fileLoadService.loadImageData("sprites/" + name + ".png");

        var texture = new Texture(name, glGenTextures(), imageData.getWidth(), imageData.getHeight());
        texture.bind();

        setParam(WRAP_S, REPEAT);
        setParam(WRAP_T, REPEAT);
        setParam(MIN_FILTER, NEAREST);
        setParam(MAG_FILTER, NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData.getImageBytes());
        glGenerateMipmap(GL_TEXTURE_2D);

        texture.unbind();
        return texture;
    }

    public Texture produceFontTexture(ByteBuffer byteBuffer, int width, int height) {
        var texture = new Texture("font", glGenTextures(), width, height);
        texture.bind();

        setParam(MIN_FILTER, LINEAR);
        setParam(MAG_FILTER, LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, byteBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        texture.unbind();
        return texture;
    }

    private void setParam(Texture.ParameterName parameterName, Texture.Filter filter) {
        glTexParameteri(GL_TEXTURE_2D, parameterName.getValue(), filter.getValue());
    }

    private void setParam(Texture.ParameterName parameterName, Texture.WrapMode wrapMode) {
        glTexParameteri(GL_TEXTURE_2D, parameterName.getValue(), wrapMode.getValue());
    }
}
