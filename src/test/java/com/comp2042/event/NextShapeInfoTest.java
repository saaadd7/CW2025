package com.comp2042.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NextShapeInfoTest {

    @Test
    void testNextShapeInfoHoldsData() {
        // Arrange
        // Create a dummy shape (2x2 matrix)
        int[][] dummyShape = {
                {1, 0},
                {1, 1}
        };
        int rotationIndex = 2;

        // Act
        NextShapeInfo info = new NextShapeInfo(dummyShape, rotationIndex);

        // Assert
        // Verify the object is storing exactly what we gave it
        assertArrayEquals(dummyShape, info.getShape(), "Should return the exact shape matrix");
        assertEquals(rotationIndex, info.getPosition(), "Should return the correct rotation index");
    }
}