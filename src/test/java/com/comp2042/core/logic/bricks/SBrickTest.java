package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SBrickTest {

    @Test
    void testSBrickStructure() {
        SBrick brick = new SBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        assertNotNull(shapes);
        // S-Brick usually has 2 or 4 rotation states
        assertFalse(shapes.isEmpty());

        for (int[][] matrix : shapes) {
            assertEquals(4, countBlocks(matrix), "S-Brick must always have 4 blocks");
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