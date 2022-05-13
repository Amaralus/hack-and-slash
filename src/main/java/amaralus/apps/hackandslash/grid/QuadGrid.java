package amaralus.apps.hackandslash.grid;

import amaralus.apps.hackandslash.common.Destroyable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QuadGrid implements Grid {

    private final int width;
    private final int height;
    private final List<Node> nodes;

    public QuadGrid(int width, int height) {
        this.height = height;
        this.width = width;
        this.nodes = generate();
    }

    // todo to NodeElement
    public Node get(int index) {
        return nodes.get(index);
    }

    // todo to NodeElement
    public Node get(int x, int y) {
        checkIndex(x, y);
        return get(y * width + x);
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public void destroy() {
        nodes.forEach(Node::destroy);
    }

    List<Node> getNodes() {
        return nodes;
    }

    private void checkIndex(int x, int y) {
        Objects.checkIndex(x, width);
        Objects.checkIndex(y, height);
    }

    private List<Node> generate() {
        Node upperNode = null;
        Node lineFirstNode = null;
        Node leftNode = null;
        var nodeArray = new Node[width * height];

        for (int i = 0; i < width * height; i++) {
            var node = new Node(i, width);

            // линкование по горизонтали
            if (leftNode != null)
                link(leftNode, node, true);
            leftNode = (i + 1) % width == 0 ? null : node;

            // фиксация верхней ноды
            if (i % width == 0) {
                upperNode = lineFirstNode;
                lineFirstNode = node;
            }

            // линкование по вертикали
            if (i >= width) {
                link(upperNode, node, false);
                if ((i + 1) % width != 0)
                    upperNode = upperNode.right;
            }

            nodeArray[i] = node;
        }

        return Arrays.asList(nodeArray);
    }

    private void link(Node first, Node second, boolean horizontally) {
        if (horizontally) {
            first.right = second;
            second.left = first;
        } else {
            first.down = second;
            second.up = first;
        }
    }

    static class Node implements Destroyable {
        final int index;
        final int x;
        final int y;

        Node up;
        Node down;
        Node right;
        Node left;

        public Node(int index, int width) {
            this.index = index;
            x = index % width;
            y = (index - x) / width;
        }

        @Override
        public void destroy() {
            up = null;
            down = null;
            right = null;
            left = null;
        }

    }
}
