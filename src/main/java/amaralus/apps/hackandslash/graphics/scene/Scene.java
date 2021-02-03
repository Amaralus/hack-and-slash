package amaralus.apps.hackandslash.graphics.scene;

import amaralus.apps.hackandslash.graphics.entities.Camera;

import java.util.ArrayList;
import java.util.List;

public class Scene extends Node {

    private final Camera camera;

    public Scene(float width, float height) {
        camera = new Camera(width, height);
        camera.setScale(0.5f);
        addChildren(camera);
    }

    public List<NodeLayer> buildSceneGraphLayers() {
        var layers = new ArrayList<NodeLayer>();
        fillNodeLayer(layers, this);
        return layers;
    }

    private void fillNodeLayer(List<NodeLayer> layers, Node parentNode) {
        if (parentNode.getChildren().isEmpty())
            return;
        else if (layers.size() == parentNode.getNodeLayerNumber())
            layers.add(new NodeLayer());

        var children = parentNode.getChildren();
        layers.get(parentNode.getNodeLayerNumber()).addNodes(children);
        for (var child : children)
            fillNodeLayer(layers, child);
    }

    public Camera getCamera() {
        return camera;
    }
}
