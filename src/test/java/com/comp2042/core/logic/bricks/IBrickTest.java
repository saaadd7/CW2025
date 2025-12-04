package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class IBrickTest {

    @Test
    void testIBrickStructure() {
        IBrick brick = new IBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());

        for (int[][] matrix : shapes) {
            // Check block count
            assertEquals(4, countBlocks(matrix), "I-Brick must always have 4 blocks");

            // Check matrix dimensions (Should usually be 4x4 for consistent rotation)
            // If your IBrick uses a different size (like 1x4), this assertion might fail.
            // If it fails, change 4 to matrix.length.
            assertEquals(4, matrix.length, "I-Brick matrix height should be 4");
            assertEquals(4, matrix[0].length, "I-Brick matrix width should be 4");
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