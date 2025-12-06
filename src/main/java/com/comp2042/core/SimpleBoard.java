package com.comp2042.core;

import com.comp2042.event.ClearRow;
import com.comp2042.event.NextShapeInfo;
import com.comp2042.event.ViewData;
import com.comp2042.core.logic.bricks.Brick;
import com.comp2042.core.logic.bricks.BrickGenerator;
import com.comp2042.core.logic.bricks.RandomBrickGenerator;
import com.comp2042.GameMode;

import java.awt.Point;
import java.util.List;

/**
 * Implements the {@link Board} interface, providing the core game logic for Tetris.
 * It manages the game grid, brick movements, rotations, collision detection,
 * line clearing, and scoring.
 */
public class SimpleBoard implements Board {

    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private GameMode gameMode = GameMode.CLASSIC;
    private int linesCleared = 0;
    private long startTime = 0;

    /**
     * Constructs a new SimpleBoard with specified dimensions.
     * Initializes the game matrix, brick generator, brick rotator, and score.
     *
     * @param rows The number of rows in the game board.
     * @param cols The number of columns in the game board.
     */
    public SimpleBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        currentGameMatrix = new int[rows][cols];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }


    /**
     * Attempts to move the current brick by the given delta (dx, dy).
     *
     * @param dx The change in the x-coordinate (columns).
     * @param dy The change in the y-coordinate (rows).
     * @return true if the brick was successfully moved, false otherwise (e.g., due to collision or boundary).
     */
    private boolean moveBrick(int dx, int dy) {
        Point newPos = new Point(currentOffset);
        newPos.translate(dx, dy);

        boolean conflict = MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                newPos.x,
                newPos.y
        );

        if (conflict) {
            return false;
        }

        currentOffset = newPos;
        return true;
    }

    /**
     * Attempts to move the current brick one step down.
     * @return true if the brick moved down, false if it collided or hit the bottom.
     */
    @Override
    public boolean moveBrickDown() {
        return moveBrick(0, 1);
    }

    /**
     * Attempts to move the current brick one step left.
     * @return true if the brick moved left, false if it collided or hit the left wall.
     */
    @Override
    public boolean moveBrickLeft() {
        return moveBrick(-1, 0);
    }

    /**
     * Attempts to move the current brick one step right.
     * @return true if the brick moved right, false if it collided or hit the right wall.
     */
    @Override
    public boolean moveBrickRight() {
        return moveBrick(1, 0);
    }

    /**
     * Attempts to rotate the current brick to the left (counter-clockwise).
     * @return true if the brick was successfully rotated, false otherwise (e.g., due to collision).
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] boardCopy = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();

        boolean conflict = MatrixOperations.intersect(
                boardCopy,
                nextShape.getShape(),
                currentOffset.x,
                currentOffset.y
        );

        if (conflict) {
            return false;
        }

        brickRotator.setCurrentShape(nextShape.getPosition());
        return true;
    }

    /**
     * Creates and places a new random brick at the top of the board.
     * If the new brick immediately collides, it means the game is over.
     * @return true if the new brick immediately collides, indicating game over; false otherwise.
     */
    @Override
    public boolean createNewBrick() {
        Brick brick = brickGenerator.getBrick();
        brickRotator.setBrick(brick);


        currentOffset = new Point(4, 2);

        return MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
    }

    /**
     * Returns the current state of the game board as a 2D integer matrix.
     * @return A 2D integer array representing the game board.
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }


    /**
     * Generates a {@link ViewData} object containing information about the current game state
     * for rendering, including the current brick's position and shape, upcoming bricks, and the ghost brick.
     * @return A {@link ViewData} object for rendering the game.
     */
    @Override
    public ViewData getViewData() {

        // 1. Capture the CURRENT brick shape
        int[][] brickMatrix = brickRotator.getCurrentShape();
        int brickX = currentOffset.x;
        int brickY = currentOffset.y;

        // 2. Get Next Bricks (Standard Logic)
        List<Brick> nextBricks = ((RandomBrickGenerator) brickGenerator).getNextBricks(3);

        int[][] nextShape1 = null;
        int[][] nextShape2 = null;
        int[][] nextShape3 = null;

        if (nextBricks.size() > 0) nextShape1 = nextBricks.get(0).getShapeMatrix().get(0);
        if (nextBricks.size() > 1) nextShape2 = nextBricks.get(1).getShapeMatrix().get(0);
        if (nextBricks.size() > 2) nextShape3 = nextBricks.get(2).getShapeMatrix().get(0);

        // 3. Create View Data
        ViewData view = new ViewData(brickMatrix, brickX, brickY, nextShape1, nextShape2, nextShape3);


        int ghostY = calculateGhostY(brickMatrix, brickX, brickY);
        view.setGhost(brickMatrix, brickX, ghostY);

        return view;
    }

    /**
     * Merges the current falling brick into the background game matrix.
     * This occurs when a brick has landed and can no longer move down.
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
    }

    /**
     * Checks for and clears any completed rows from the game board.
     * Updates the game matrix after clearing rows.
     * @return A {@link ClearRow} object indicating which rows were cleared and the score bonus.
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        linesCleared += clearRow.getLinesRemoved();
        return clearRow;
    }

    /**
     * Returns the {@link Score} object associated with this game board.
     * @return The current game score.
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the game board to its initial empty state and starts a new game
     * by creating the first brick.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[rows][cols];
        score.reset();
        linesCleared = 0;
        startTime = System.currentTimeMillis();
        createNewBrick();

    }


    /**
     * Calculates the Y-coordinate for the "ghost" brick, which shows where the current brick
     * would land if immediately dropped.
     *
     * @param shape The 2D array representing the shape of the current brick.
     * @param startX The current X-coordinate (column) of the brick.
     * @param startY The current Y-coordinate (row) of the brick.
     * @return The Y-coordinate where the ghost brick should be rendered.
     */
    private int calculateGhostY(int[][] shape, int startX, int startY) {
        int ghostY = startY;

        // Loop: Keep pushing the ghost down until it hits something
        while (canBrickMoveDown(shape, startX, ghostY)) {
            ghostY++;
        }

        return ghostY;
    }

    /**
     * Checks whether a brick with given shape at (x, y) can move 1 row down.
     * This method is used internally for collision detection, especially for the ghost brick.
     *
     * @param shape The 2D array representing the shape of the brick.
     * @param x The current X-coordinate (column) of the brick's top-left corner.
     * @param y The current Y-coordinate (row) of the brick's top-left corner.
     * @return true if the brick can move one row down without collision, false otherwise.
     */
    private boolean canBrickMoveDown(int[][] shape, int x, int y) {

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 0) {
                    continue;
                }

                int newY = y + row + 1;
                int newX = x + col;



                if (newY >= rows) { // Bottom boundary
                    return false;
                }

                if (newX < 0 || newX >= cols) { // Left/Right boundary
                    return false;
                }

                // Collision with existing blocks
                if (currentGameMatrix[newY][newX] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
        if (mode == GameMode.ULTRA) {
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public boolean isGameModeComplete() {
        switch (gameMode) {
            case SPRINT:
                return linesCleared >= 30;
            case ULTRA:
                long elapsed = System.currentTimeMillis() - startTime;
                return elapsed >= 180000; // 3 minutes in milliseconds
            case CLASSIC:
            default:
                return false; // Classic never completes, only game over
        }
    }

    @Override
    public String getGameModeStatus() {
        switch (gameMode) {
            case SPRINT:
                return "Lines: " + linesCleared + "/30";
            case ULTRA:
                long elapsed = System.currentTimeMillis() - startTime;
                long remaining = Math.max(0, 180000 - elapsed);
                long seconds = (remaining / 1000) % 60;
                long minutes = remaining / 60000;
                return String.format("Time: %d:%02d", minutes, seconds);
            case CLASSIC:
            default:
                return "Score: " + score.scoreProperty().get();
        }
    }
}