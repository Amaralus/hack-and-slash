package amaralus.apps.hackandslash.graphics.entities.gpu.factory;

import amaralus.apps.hackandslash.common.ValueEnum;
import amaralus.apps.hackandslash.graphics.entities.gpu.Texture;
import amaralus.apps.hackandslash.io.data.ImageData;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

@Component
public class TextureFactory {

    public TextureFactoryBuilder newTexture(String resourceName) {
        return new TextureFactoryBuilder(resourceName);
    }

    public static class TextureFactoryBuilder {

        private final Map<Texture.ParameterName, ValueEnum> params = new HashMap<>();
        private final String resourceName;

        private int width;
        private int height;
        private ByteBuffer pixels;
        private Texture.PixelFormat pixelFormat;
        private boolean generateMipmap;

        public TextureFactoryBuilder(String resourceName) {
            this.resourceName = resourceName;
        }

        public TextureFactoryBuilder width(int width) {
            this.width = width;
            return this;
        }

        public TextureFactoryBuilder height(int height) {
            this.height = height;
            return this;
        }

        public TextureFactoryBuilder pixels(ByteBuffer pixels) {
            this.pixels = pixels;
            return this;
        }

        public TextureFactoryBuilder imageData(ImageData imageData) {
            width = imageData.getWidth();
            height = imageData.getHeight();
            pixels = imageData.getImageBytes();
            return this;
        }

        public TextureFactoryBuilder pixelFormat(Texture.PixelFormat pixelFormat) {
            this.pixelFormat = pixelFormat;
            return this;
        }

        public TextureFactoryBuilder generateMipmap() {
            generateMipmap = true;
            return this;
        }

        public TextureFactoryBuilder param(Texture.ParameterName parameterName, ValueEnum parameterValue) {
            params.put(parameterName, parameterValue);
            return this;
        }

        public Texture produce() {
            var texture = new Texture(resourceName, glGenTextures(), width, height);

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
}
