package com.comp2042.core;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Represents the player's score in the game.
 * This class uses JavaFX properties to allow for easy binding with UI elements.
 */
public final class Score {

    /**
     * Constructs a new Score instance, initializing the score to 0.
     */
    public Score() {
    }

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Returns the {@link IntegerProperty} for the score.
     * @return The {@link IntegerProperty} representing the current score.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds a specified value to the current score.
     * @param i The integer value to add to the score.
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}
