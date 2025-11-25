package com.comp2042.logic;
 import com.comp2042.ClearRow;
 import com.comp2042.MatrixOperations;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

public class MatrixOperationsTest {

    @Test
    public void testIntersect_NoIntersection() {
        int[][] matrix = {
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        int[][] brick = {
                {1, 1},
                {1, 0}
        };
        // Moving the brick in a position where it does not intersect with the matrix
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 1));
    }

    @Test
    public void testIntersect_WithIntersection() {
        int[][] matrix = {
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        int[][] brick = {
                {1, 1},
                {1, 0}
        };
        // Moving the brick to a position where it intersects with the matrix
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 0));
    }

    @Test
    public void testCopy() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6}
        };
        int[][] copy = MatrixOperations.copy(original);

        // Check if the copy is deep (modifying copy should not affect original)
        copy[0][0] = 99;
        assertEquals(1, original[0][0]);
        assertEquals(99, copy[0][0]);
    }

    @Test
    public void testMerge() {
        int[][] filledFields = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        int[][] brick = {
                {1, 1},
                {1, 1}
        };
        int x = 1, y = 1;
        int[][] result = MatrixOperations.merge(filledFields, brick, x, y);

        // Check if the brick is merged into the filledFields at the correct position
        assertEquals(1, result[1][1]);
        assertEquals(1, result[1][2]);
        assertEquals(1, result[2][1]);
        assertEquals(1, result[2][2]);
    }

    @Test
    public void testCheckRemoving_NoRowsCleared() {
        int[][] matrix = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(0, result.getLinesRemoved());
    }

    @Test
    public void testCheckRemoving_WithRowsCleared() {
        int[][] matrix = {
                {1, 1, 1},
                {1, 1, 1},
                {0, 0, 0}
        };
        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(2, result.getLinesRemoved());
        assertEquals(200, result.getScoreBonus()); // Corrected: 2 rows cleared -> 50 * 2 * 2 = 200
    }


    @Test
    public void testDeepCopyList() {
        List<int[][]> originalList = List.of(
                new int[][]{{1, 2}, {3, 4}},
                new int[][]{{5, 6}, {7, 8}}
        );
        List<int[][]> copiedList = MatrixOperations.deepCopyList(originalList);

        // Check if deep copy is done (modifying copied list shouldn't affect original list)
        copiedList.get(0)[0][0] = 99;
        assertEquals(1, originalList.get(0)[0][0]);
        assertEquals(99, copiedList.get(0)[0][0]);
    }
}
