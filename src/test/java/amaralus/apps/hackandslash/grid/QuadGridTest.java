package amaralus.apps.hackandslash.grid;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class QuadGridTest {

    @Test
    void testGridSize() {
        var grid = new QuadGrid(3, 3);

        assertEquals(9, grid.size());
    }

    @Test
    void testGridLinks() {
        var grid = new QuadGrid(3, 3);
        var nodes = grid.getNodes();

        // 0 1 2
        // 3 4 5
        // 6 7 8

        // up, right, down, left
        assertArrayEquals(nums(-1, 1, 3, -1), nodeToNodesNums(nodes.get(0)));
        assertArrayEquals(nums(-1, 2, 4, 0), nodeToNodesNums(nodes.get(1)));
        assertArrayEquals(nums(-1, -1, 5, 1), nodeToNodesNums(nodes.get(2)));

        assertArrayEquals(nums(0, 4, 6, -1), nodeToNodesNums(nodes.get(3)));
        assertArrayEquals(nums(1, 5, 7, 3), nodeToNodesNums(nodes.get(4)));
        assertArrayEquals(nums(2, -1, 8, 4), nodeToNodesNums(nodes.get(5)));

        assertArrayEquals(nums(3, 7, -1, -1), nodeToNodesNums(nodes.get(6)));
        assertArrayEquals(nums(4, 8, -1, 6), nodeToNodesNums(nodes.get(7)));
        assertArrayEquals(nums(5, -1, -1, 7), nodeToNodesNums(nodes.get(8)));
    }


    private int[] nums(int... nums) {
        return nums;
    }

    private int[] nodeToNodesNums(QuadGrid.Node node) {
        return Stream.of(node.up, node.right, node.down, node.left)
                .mapToInt(this::getNum)
                .toArray();
    }

    private int getNum(QuadGrid.Node node) {
        return node == null ? -1 : node.index;
    }
}