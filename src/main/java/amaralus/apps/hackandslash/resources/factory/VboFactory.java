package amaralus.apps.hackandslash.resources.factory;

import amaralus.apps.hackandslash.graphics.entities.data.*;
import amaralus.apps.hackandslash.resources.ResourceManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VboFactory {

    private BufferType type;
    private BufferUsage usage;
    private IntBuffer intBuffer;
    private FloatBuffer floatBuffer;
    private boolean needDataFormat = true;

    private ResourceManager resourceManager;
    private String resourceName;

    public static VboFactory intBuffer(IntBuffer intBuffer) {
        return new VboFactory(intBuffer);
    }

    public static VboFactory intBuffer(int[] intBuffer) {
        return new VboFactory(IntBuffer.wrap(intBuffer));
    }

    public static VboFactory floatBuffer(FloatBuffer floatBuffer) {
        return new VboFactory(floatBuffer);
    }

    public static VboFactory floatBuffer(float[] floatBuffer) {
        return new VboFactory(FloatBuffer.wrap(floatBuffer));
    }

    private VboFactory(IntBuffer intBuffer) {
        this.intBuffer = intBuffer;
    }

    private VboFactory(FloatBuffer floatBuffer) {
        this.floatBuffer = floatBuffer;
    }

    public VboFactory type(BufferType type) {
        this.type = type;
        return this;
    }

    public VboFactory usage(BufferUsage usage) {
        this.usage = usage;
        return this;
    }

    public VboFactory needDataFormat(boolean needDataFormat) {
        this.needDataFormat = needDataFormat;
        return this;
    }

    public VboFactory saveAs(String name, ResourceManager resourceManager) {
        this.resourceName = name;
        this.resourceManager = resourceManager;
        return this;
    }

    public VertexBufferObject build() {
        var buffer = intBuffer != null ?
                new IntVertexBufferObject(type, usage, intBuffer) :
                new FloatVertexBufferObject(type, usage, floatBuffer);

        buffer.setNeedDataFormat(needDataFormat);

        if (resourceManager != null) resourceManager.addResource(resourceName, buffer);
        return buffer;
    }
}
