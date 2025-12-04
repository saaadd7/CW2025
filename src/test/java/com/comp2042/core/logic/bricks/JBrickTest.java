package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JBrickTest {

    @Test
    void testJBrickStructure() {
        // Arrange
        JBrick brick = new JBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        // Assert
        assertNotNull(shapes, "Should not be null");
        assertFalse(shapes.isEmpty(), "Should contain rotations");

        // Verify every rotation has exactly 4 blocks
        for (int[][] matrix : shapes) {
            assertEquals(4, countBlocks(matrix), "Every rotation of J-Brick must have 4 blocks");
            assertEquals(4, matrix.length, "Matrix height must be 4");
            assertEquals(4, matrix[0].length, "Matrix width must be 4");
        }
    }

    // Helper method to count non-zero blocks in the matrix
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