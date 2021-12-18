package amaralus.apps.hackandslash.graphics.gpu.buffer.factory;

import amaralus.apps.hackandslash.graphics.gpu.buffer.*;
import amaralus.apps.hackandslash.resources.ResourceManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static amaralus.apps.hackandslash.utils.BufferUtil.bufferOf;

public class VboFactory {

    private BufferType type;
    private BufferUsage usage;
    private IntBuffer intBuffer;
    private FloatBuffer floatBuffer;

    private ResourceManager resourceManager;
    private String resourceName;

    private final List<VertexBufferObject.DataFormat> dataFormats = new ArrayList<>();

    public static VboFactory intBuffer(IntBuffer intBuffer) {
        return new VboFactory(intBuffer);
    }

    public static VboFactory intBuffer(int... intBuffer) {
        return new VboFactory(bufferOf(intBuffer));
    }

    public static VboFactory floatBuffer(FloatBuffer floatBuffer) {
        return new VboFactory(floatBuffer);
    }

    public static VboFactory floatBuffer(float... floats) {
        return new VboFactory(bufferOf(floats));
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

    public VboFactory dataFormat(int index, int size, int stride, long offset, Class<? extends Number> dataType) {
        dataFormats.add(new VertexBufferObject.DataFormat(index, size, stride, offset, dataType));
        return this;
    }

    public VboFactory saveAsVbo(String name, ResourceManager resourceManager) {
        return saveAs(name + "Vbo", resourceManager);
    }

    public VboFactory saveAsEbo(String name, ResourceManager resourceManager) {
        return saveAs(name + "Ebo", resourceManager);
    }

    public VboFactory saveAs(String name, ResourceManager resourceManager) {
        this.resourceName = name;
        this.resourceManager = resourceManager;
        return this;
    }

    public VertexBufferObject build() {
        var buffer = intBuffer != null ?
                new IntVertexBufferObject(resourceName, type, usage, intBuffer) :
                new FloatVertexBufferObject(resourceName, type, usage, floatBuffer);

        buffer.setDataFormats(dataFormats);

        if (resourceManager != null) resourceManager.addResource(buffer);
        return buffer;
    }
}
