package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LBrickTest {

    @Test
    void testLBrickStructure() {
        LBrick brick = new LBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        assertNotNull(shapes);

        for (int[][] matrix : shapes) {
            assertEquals(4, countBlocks(matrix), "Every rotation of L-Brick must have 4 blocks");
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