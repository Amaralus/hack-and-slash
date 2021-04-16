package amaralus.apps.hackandslash.graphics.scene;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    private final List<Node> children = new ArrayList<>();

    private Node parent;
    private int nodeLayerNumber;

    public void addChildren(Node... children) {
        for (var child : children) {
            child.setParent(this);
            this.children.add(child);
            updateLayerNumber(child, nodeLayerNumber);
        }
    }

    private void updateLayerNumber(Node childNode, int parentNumber) {
        childNode.setNodeLayerNumber(++parentNumber);
        for (var child : childNode.getChildren())
            updateLayerNumber(child, parentNumber);
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public int getNodeLayerNumber() {
        return nodeLayerNumber;
    }

    public void setNodeLayerNumber(int nodeLayerNumber) {
        this.nodeLayerNumber = nodeLayerNumber;
    }
}
