package com.comp2042.event;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    @Test
    void testClearRowHoldsData() {
        // Arrange
        int linesRemoved = 2;
        int[][] matrix = {
                {0, 0, 0},
                {1, 1, 1}
        };
        int scoreBonus = 300;
        List<Integer> clearedRows = Arrays.asList(18, 19);

        // Act
        // We pass all 4 arguments required by your constructor
        ClearRow event = new ClearRow(linesRemoved, matrix, scoreBonus, clearedRows);

        // Assert
        assertEquals(linesRemoved, event.getLinesRemoved(), "Should return correct lines removed");
        assertEquals(scoreBonus, event.getScoreBonus(), "Should return correct score bonus");
        assertEquals(clearedRows, event.getClearedRows(), "Should return the list of cleared row indices");

        // Check Matrix equality (using deepEquals for 2D arrays)
        assertArrayEquals(matrix, event.getNewMatrix(), "Should return a copy of the matrix");
    }

    @Test
    void testNullListHandling() {
        // Arrange
        int linesRemoved = 1;
        int[][] matrix = {{0}};
        int scoreBonus = 100;

        // Act: Pass NULL as the list
        ClearRow event = new ClearRow(linesRemoved, matrix, scoreBonus, null);

        // Assert: Your code handles null by creating an empty list
        assertNotNull(event.getClearedRows(), "Should return an empty list, not null");
        assertTrue(event.getClearedRows().isEmpty(), "List should be empty");
    }
}