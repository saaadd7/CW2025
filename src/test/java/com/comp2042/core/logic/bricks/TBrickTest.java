package com.comp2042.core.logic.bricks; // MUST match exactly

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TBrickTest {

    @Test
    void testTBrickInitialization() {
        // Arrange & Act
        TBrick brick = new TBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        // Assert
        assertNotNull(shapes, "Shape matrix list should not be null");
        assertEquals(4, shapes.size(), "T-Brick should have 4 rotation states");
    }

    @Test
    void testFirstRotationShape() {
        // Arrange
        TBrick brick = new TBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        // Act: Get the first rotation (default spawn state)
        int[][] shape = shapes.get(0);

        // Assert: Verify the T-shape matrix (based on your code)
        // Expected:
        // {0, 0, 0, 0}
        // {6, 6, 6, 0}
        // {0, 6, 0, 0}
        // {0, 0, 0, 0}

        assertArrayEquals(new int[]{0, 0, 0, 0}, shape[0], "Row 0 should be empty");
        assertArrayEquals(new int[]{6, 6, 6, 0}, shape[1], "Row 1 should contain the T bar");
        assertArrayEquals(new int[]{0, 6, 0, 0}, shape[2], "Row 2 should contain the T stem");
        assertArrayEquals(new int[]{0, 0, 0, 0}, shape[3], "Row 3 should be empty");
    }

    @Test
    void testImmutability() {
        // Arrange
        TBrick brick = new TBrick();

        // Act
        // Get the list and try to modify it
        List<int[][]> list1 = brick.getShapeMatrix();
        list1.clear(); // We try to delete the shapes

        // Assert
        // The internal brick should remain safe because you used deepCopyList
        List<int[][]> list2 = brick.getShapeMatrix();
        assertEquals(4, list2.size(), "Modifying the returned list should not affect the original brick");
    }
}