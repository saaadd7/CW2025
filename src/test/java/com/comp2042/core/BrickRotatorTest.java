package com.comp2042.core;

import com.comp2042.core.logic.bricks.Brick;
import com.comp2042.core.logic.bricks.RandomBrickGenerator;
import com.comp2042.event.NextShapeInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    @Test
    void testInitialization() {
        BrickRotator rotator = new BrickRotator();

        // Before setting a brick, we cannot really test much.
        // This test just ensures the object is created without crashing.
        assertNotNull(rotator);
    }

    @Test
    void testSetBrickResetsShape() {
        // Arrange
        BrickRotator rotator = new BrickRotator();
        Brick brick = new RandomBrickGenerator().getBrick(); // Get a real brick

        // Act
        // Manually "dirty" the current shape index to something else (if possible)
        rotator.setCurrentShape(1);

        // Set the brick (should reset currentShape to 0)
        rotator.setBrick(brick);

        // Assert
        // We compare the array references.
        // The rotator's current shape should be the 0th shape of the brick.
        assertArrayEquals(brick.getShapeMatrix().get(0), rotator.getCurrentShape(),
                "Setting a new brick should reset the current shape to index 0");
    }

    @Test
    void testNextShapeLogic() {
        // Arrange
        BrickRotator rotator = new BrickRotator();
        Brick brick = new RandomBrickGenerator().getBrick();
        rotator.setBrick(brick);

        int numberOfShapes = brick.getShapeMatrix().size();

        // Act & Assert
        // We simulate rotating through every possible position
        for (int i = 0; i < numberOfShapes; i++) {
            // Update the rotator to the current index 'i'
            rotator.setCurrentShape(i);

            // Calculate what the NEXT shape should be
            NextShapeInfo nextInfo = rotator.getNextShape();

            // Expected index is (i + 1), but if we hit the end, it wraps to 0
            int expectedIndex = (i + 1) % numberOfShapes;

            assertEquals(expectedIndex, nextInfo.getPosition(),
                    "Rotation from index " + i + " should lead to " + expectedIndex);
        }
    }
}