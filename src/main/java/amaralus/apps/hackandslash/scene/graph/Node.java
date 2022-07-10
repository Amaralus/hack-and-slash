package amaralus.apps.hackandslash.scene.graph;

import java.util.ArrayList;
import java.util.List;

import static amaralus.apps.hackandslash.scene.graph.NodeRemovingStrategy.SINGLE;

public abstract class Node {

    private final List<Node> children = new ArrayList<>();

    private Node parent;
    private int nodeLayerNumber;
    protected NodeRemovingStrategy nodeRemovingStrategy = SINGLE;

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

    public NodeRemovingStrategy getRemovingStrategy() {
        return nodeRemovingStrategy;
    }

    public void setRemovingStrategy(NodeRemovingStrategy nodeRemovingStrategy) {
        this.nodeRemovingStrategy = nodeRemovingStrategy;
    }
}
