package com.comp2042.core;

import com.comp2042.core.Score; // Correct import
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void testInitialScoreIsZero() {
        // Arrange
        Score gameScore = new Score();

        // Act & Assert
        // We use .scoreProperty() to access the public method
        // We use .get() to unwrap the integer value from the property
        assertEquals(0, gameScore.scoreProperty().get(), "Score should start at 0");
    }

    @Test
    void testAddScore() {
        // Arrange
        Score gameScore = new Score();

        // Act: Add 100 points
        gameScore.add(100);

        // Assert
        assertEquals(100, gameScore.scoreProperty().get(), "Score should be 100 after adding");
    }

    @Test
    void testScoreResets() {
        // Arrange
        Score gameScore = new Score();
        gameScore.add(500); // Add points first so we can verify reset works

        // Act
        gameScore.reset();

        // Assert
        assertEquals(0, gameScore.scoreProperty().get(), "Score should be 0 after reset");
    }
}