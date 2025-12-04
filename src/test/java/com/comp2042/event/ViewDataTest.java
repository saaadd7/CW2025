package com.comp2042.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    @Test
    void testInitializationHoldsData() {
        // Arrange: Create dummy data for the constructor
        int[][] currentBrick = {{1}};
        int x = 5;
        int y = 10;

        // Next pieces (simulating the preview list)
        int[][] next1 = {{1, 1}};
        int[][] next2 = {{0, 1}};
        int[][] next3 = {{1, 0}};

        // Act
        ViewData view = new ViewData(currentBrick, x, y, next1, next2, next3);

        // Assert: Verify main brick data
        assertArrayEquals(currentBrick, view.getBrickData(), "Should return correct brick matrix");
        assertEquals(x, view.getxPosition(), "Should return correct X");
        assertEquals(y, view.getyPosition(), "Should return correct Y");

        // Assert: Verify next pieces
        assertArrayEquals(next1, view.getNextBrickData1(), "Should return next brick 1");
        assertArrayEquals(next2, view.getNextBrickData2(), "Should return next brick 2");
        assertArrayEquals(next3, view.getNextBrickData3(), "Should return next brick 3");
    }

    @Test
    void testGhostLogic() {
        // Arrange
        // Instantiate with nulls for the main data since we only care about ghost here
        ViewData view = new ViewData(null, 0, 0, null, null, null);

        int[][] ghostShape = {{2, 2}};
        int ghostX = 3;
        int ghostY = 18;

        // Act: Set the ghost data
        view.setGhost(ghostShape, ghostX, ghostY);

        // Assert: Verify getters
        assertArrayEquals(ghostShape, view.getGhostData(), "Ghost matrix should match");
        assertEquals(ghostX, view.getGhostX(), "Ghost X should match");
        assertEquals(ghostY, view.getGhostY(), "Ghost Y should match");
    }
}