package amaralus.apps.hackandslash.graphics.gpu.texture;

import amaralus.apps.hackandslash.common.ValueEnum;
import amaralus.apps.hackandslash.io.data.ImageData;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureBuilder {

    private final Map<TextureParameterName, ValueEnum> params = new EnumMap<>(TextureParameterName.class);
    private final String textureName;

    private int width;
    private int height;
    private ByteBuffer pixels;
    private PixelFormat pixelFormat;
    private boolean generateMipmap;

    public TextureBuilder(String textureName) {
        this.textureName = textureName;
    }

    public TextureBuilder width(int width) {
        this.width = width;
        return this;
    }

    public TextureBuilder height(int height) {
        this.height = height;
        return this;
    }

    public TextureBuilder pixels(ByteBuffer pixels) {
        this.pixels = pixels;
        return this;
    }

    public TextureBuilder imageData(ImageData imageData) {
        width = imageData.getWidth();
        height = imageData.getHeight();
        pixels = imageData.getImageBytes();
        return this;
    }

    public TextureBuilder pixelFormat(PixelFormat pixelFormat) {
        this.pixelFormat = pixelFormat;
        return this;
    }

    public TextureBuilder generateMipmap() {
        generateMipmap = true;
        return this;
    }

    public TextureBuilder param(TextureParameterName parameterName, ValueEnum parameterValue) {
        params.put(parameterName, parameterValue);
        return this;
    }

    public Texture produce() {
        var texture = new Texture(textureName, glGenTextures(), width, height);

        texture.bind();
        applyParams();

        var format = pixelFormat.getValue();
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, pixels);

        if (generateMipmap)
            glGenerateMipmap(GL_TEXTURE_2D);

        texture.unbind();
        return texture;
    }

    private void applyParams() {
        params.forEach((name, value) -> glTexParameteri(GL_TEXTURE_2D, name.getValue(), value.getValue()));
    }
}
