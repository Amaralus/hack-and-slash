package amaralus.apps.hackandslash.scene;

import java.util.ArrayList;
import java.util.List;

public class SceneGraphLayer {

    private final List<Node> nodes = new ArrayList<>();

    public SceneGraphLayer(Node... nodes) {
        addNodes(nodes);
    }

    public void addNodes(List<Node> nodes) {
        this.nodes.addAll(nodes);
    }

    public void addNodes(Node... nodes) {
        this.nodes.addAll(List.of(nodes));
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
