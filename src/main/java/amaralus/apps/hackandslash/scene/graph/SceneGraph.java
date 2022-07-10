package amaralus.apps.hackandslash.scene.graph;

import java.util.ArrayList;
import java.util.List;

public class SceneGraph extends Node {

    public List<SceneGraphLayer> buildSceneGraphLayers() {
        var layers = new ArrayList<SceneGraphLayer>();
        fillLayers(layers, this);
        return layers;
    }

    private void fillLayers(List<SceneGraphLayer> layers, Node parentNode) {
        if (parentNode.getChildren().isEmpty())
            return;
        else if (layers.size() == parentNode.getNodeLayerNumber())
            layers.add(new SceneGraphLayer());

        var children = parentNode.getChildren();
        layers.get(parentNode.getNodeLayerNumber()).addNodes(children);
        for (var child : children)
            fillLayers(layers, child);
    }
}
