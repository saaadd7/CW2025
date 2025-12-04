package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ZBrickTest {

    @Test
    void testZBrickStructure() {
        ZBrick brick = new ZBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());

        for (int[][] matrix : shapes) {
            assertEquals(4, countBlocks(matrix), "Z-Brick must always have 4 blocks");
        }
    }

    private int countBlocks(int[][] matrix) {
        int count = 0;
        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0) count++;
            }
        }
        return count;
    }
}