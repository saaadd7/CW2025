package com.comp2042.core.logic.bricks;

/**
 * Defines the contract for generating Tetris bricks.
 * Implementations should provide a mechanism to get the current brick
 * and preview the next upcoming brick.
 */
public interface BrickGenerator {

    /**
     * Retrieves the next brick in the generation sequence.
     * After this method is called, the returned brick is considered "current".
     * @return The next {@link Brick} to be introduced into the game.
     */
    Brick getBrick();

    /**
     * Peeks at the next brick that will be returned by {@link #getBrick()} without removing it from the sequence.
     * This is useful for displaying a preview of the upcoming brick.
     * @return The {@link Brick} that is next in line.
     */
    Brick getNextBrick();
}
