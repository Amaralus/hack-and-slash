package amaralus.apps.hackandslash.graphics.gpu.buffer.factory;

import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexBufferObject;
import amaralus.apps.hackandslash.graphics.gpu.buffer.repository.VaoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VaoFactory {

    private final List<VertexBufferObject<?>> vertexBufferObjects;
    private final List<VboFactory> vboFactories;

    private VaoRepository vaoRepository;
    private String resourceName;

    public static VaoFactory newVao() {
        return new VaoFactory();
    }

    private VaoFactory() {
        vertexBufferObjects = new ArrayList<>();
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

    public VaoFactory saveAsVao(String name, VaoRepository vaoRepository) {
        return saveAs(name + "Vao", vaoRepository);
    }

    public VaoFactory saveAs(String name, VaoRepository vaoRepository) {
        this.resourceName = name;
        this.vaoRepository = vaoRepository;
        return this;
    }

    public VertexArraysObject build() {
        var vbos = vboFactories.stream()
                .map(VboFactory::build)
                .collect(Collectors.toList());

        vbos.addAll(vertexBufferObjects);
        var vao = new VertexArraysObject(resourceName, vbos);

        if (vaoRepository != null) vaoRepository.save(vao);
        return vao;
    }
}
