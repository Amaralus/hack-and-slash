package amaralus.apps.hackandslash.graphics.scene;

import java.util.ArrayList;
import java.util.List;

public class NodeLayer {

    private final List<Node> nodes = new ArrayList<>();

    public NodeLayer(Node... nodes) {
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
