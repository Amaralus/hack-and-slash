package amaralus.apps.hackandslash.resources.factory;

import amaralus.apps.hackandslash.graphics.entities.data.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.entities.data.VertexBufferObject;
import amaralus.apps.hackandslash.resources.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VaoFactory {

    private final List<VertexBufferObject<?>> vertexBufferObjects;
    private final List<VertexArraysObject.VertexBufferDataFormat> dataFormats;
    private final List<VboFactory> vboFactories;

    private ResourceManager resourceManager;
    private String resourceName;

    public static VaoFactory newVao() {
        return new VaoFactory();
    }

    private VaoFactory() {
        vertexBufferObjects = new ArrayList<>();
        dataFormats = new ArrayList<>();
        vboFactories = new ArrayList<>();
    }

    public VaoFactory buffer(VertexBufferObject<?> vbo) {
        vertexBufferObjects.add(vbo);
        return this;
    }

    public VaoFactory buffer(VboFactory vboFactory) {
        vboFactories.add(vboFactory);
        return this;
    }

    public VaoFactory dataFormat(int index, int size, int stride, long offset, Class<? extends Number> dataType) {
        dataFormats.add(new VertexArraysObject.VertexBufferDataFormat(index, size, stride, offset, dataType));
        return this;
    }

    public VaoFactory saveAs(String name, ResourceManager resourceManager) {
        this.resourceName = name;
        this.resourceManager = resourceManager;
        return this;
    }

    public VertexArraysObject build() {
        var vbos = vboFactories.stream()
                .map(VboFactory::build)
                .collect(Collectors.toList());

        vbos.addAll(vertexBufferObjects);
        var vao = new VertexArraysObject(vbos, dataFormats);

        if (resourceManager != null) resourceManager.addResource(resourceName, vao);
        return vao;
    }
}
