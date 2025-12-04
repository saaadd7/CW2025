package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OBrickTest {

    @Test
    void testOBrickStructure() {
        OBrick brick = new OBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        assertNotNull(shapes);
        // O-Brick might have 1 or 4 states depending on implementation
        assertTrue(shapes.size() >= 1, "O-Brick must have at least 1 state");

        for (int[][] matrix : shapes) {
            assertEquals(4, countBlocks(matrix), "O-Brick must have 4 blocks (2x2 square)");
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