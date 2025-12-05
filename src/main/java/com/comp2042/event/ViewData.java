package com.comp2042.event;

import com.comp2042.core.MatrixOperations;
import java.util.Arrays;

/**
 * An immutable data class that holds all the necessary information
 * for rendering the current state of the game board and its elements.
 * This includes the main falling brick, its position, upcoming brick previews,
 * and the ghost piece's position.
 */
public final class ViewData {

    // Main piece
    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;

    // Next piece preview
    private final int[][] nextBrickData1;
    private final int[][] nextBrickData2;
    private final int[][] nextBrickData3;


    // Ghost piece
    private int[][] ghostData;
    private int ghostX;
    private int ghostY;

    /**
     * Constructs a ViewData object with the current brick's state and upcoming brick previews.
     *
     * @param brickData The 2D integer array representing the shape of the current falling brick.
     * @param xPosition The x-coordinate (column) of the current falling brick's top-left corner.
     * @param yPosition The y-coordinate (row) of the current falling brick's top-left corner.
     * @param nextBrickData1 The 2D integer array of the first upcoming brick for preview.
     * @param nextBrickData2 The 2D integer array of the second upcoming brick for preview.
     * @param nextBrickData3 The 2D integer array of the third upcoming brick for preview.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData1, int[][] nextBrickData2, int[][] nextBrickData3) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData1 = nextBrickData1;
        this.nextBrickData2 = nextBrickData2;
        this.nextBrickData3 = nextBrickData3;
    }

    /**
     * Returns a deep copy of the 2D integer array representing the current falling brick's shape.
     * @return A 2D integer array of the current brick's data.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the x-coordinate (column) of the current falling brick's top-left corner.
     * @return The x-coordinate.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Returns the y-coordinate (row) of the current falling brick's top-left corner.
     * @return The y-coordinate.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Returns a deep copy of the 2D integer array for the first upcoming brick preview.
     * @return A 2D integer array of the first next brick's data.
     */
    public int[][] getNextBrickData1() {
        return MatrixOperations.copy(nextBrickData1);
    }

    /**
     * Returns a deep copy of the 2D integer array for the second upcoming brick preview.
     * @return A 2D integer array of the second next brick's data.
     */
    public int[][] getNextBrickData2() {
        return MatrixOperations.copy(nextBrickData2);
    }

    /**
     * Returns a deep copy of the 2D integer array for the third upcoming brick preview.
     * @return A 2D integer array of the third next brick's data.
     */
    public int[][] getNextBrickData3() {
        return MatrixOperations.copy(nextBrickData3);
    }

    /**
     * Returns the 2D integer array representing the ghost piece's shape.
     * @return A 2D integer array of the ghost piece's data.
     */
    public int[][] getGhostData() {
        return ghostData;
    }

    /**
     * Returns the x-coordinate (column) of the ghost piece's top-left corner.
     * @return The x-coordinate of the ghost piece.
     */
    public int getGhostX() {
        return ghostX;
    }

    /**
     * Returns the y-coordinate (row) of the ghost piece's top-left corner.
     * @return The y-coordinate of the ghost piece.
     */
    public int getGhostY() {
        return ghostY;
    }

    /**
     * Sets the data and position for the ghost piece.
     * @param data The 2D integer array representing the ghost piece's shape.
     * @param x The x-coordinate (column) of the ghost piece.
     * @param y The y-coordinate (row) of the ghost piece.
     */
    public void setGhost(int[][] data, int x, int y) {
        this.ghostData = data;
        this.ghostX = x;
        this.ghostY = y;
    }
}
