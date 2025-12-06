package com.comp2042.core;

import com.comp2042.event.ClearRow;
import com.comp2042.event.ViewData;
import com.comp2042.core.Score;
import com.comp2042.GameMode;

/**
 * Defines the contract for a game board in a block-stacking game like Tetris.
 * Implementations of this interface are responsible for managing the game grid,
 * brick movements, rotations, collision detection, row clearing, and scoring.
 */
public interface Board {

    /**
     * Attempts to move the current falling brick one unit down.
     * @return true if the brick successfully moved down, false otherwise (e.g., hit bottom or another brick).
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current falling brick one unit to the left.
     * @return true if the brick successfully moved left, false otherwise.
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current falling brick one unit to the right.
     * @return true if the brick successfully moved right, false otherwise.
     */
    boolean moveBrickRight();

    /**
     * Attempts to rotate the current falling brick. The direction of rotation
     * (e.g., left/counter-clockwise) is specific to the implementation.
     * @return true if the brick was successfully rotated, false otherwise (e.g., collision after rotation).
     */
    boolean rotateLeftBrick();

    /**
     * Creates and places a new brick at the top of the board.
     * This method is typically called after a brick has landed or at the start of a new game.
     * @return true if the newly created brick immediately collides with existing blocks,
     *         indicating a game over condition; false otherwise.
     */
    boolean createNewBrick();

    /**
     * Returns the current state of the game board as a 2D integer matrix.
     * Each element in the matrix represents a cell on the board, with different integer values
     * potentially indicating different block types or empty space.
     * @return A 2D array representing the game board matrix.
     */
    int[][] getBoardMatrix();

    /**
     * Provides data necessary for rendering the current state of the game.
     * This typically includes the current brick's position and shape, as well as upcoming bricks.
     * @return A {@link ViewData} object containing rendering information.
     */
    ViewData getViewData();

    /**
     * Merges the current falling brick into the background game matrix.
     * This action is performed when a brick lands and becomes a part of the static board.
     */
    void mergeBrickToBackground();

    /**
     * Checks for and clears any completed rows on the game board.
     * @return A {@link ClearRow} object detailing which rows were cleared and any associated score bonus.
     */
    ClearRow clearRows();

    /**
     * Returns the {@link Score} object associated with this game board,
     * allowing access to and modification of the player's score.
     * @return The {@link Score} object.
     */
    Score getScore();

    /**
     * Resets the game board to its initial state, clearing all blocks,
     * resetting the score, and preparing for a new game.
     */
    void newGame();


    // Game mode methods
    void setGameMode(GameMode mode);
    GameMode getGameMode();
    boolean isGameModeComplete();
    String getGameModeStatus();
}
