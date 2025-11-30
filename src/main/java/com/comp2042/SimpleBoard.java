package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.Point;
import java.util.List;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    public SimpleBoard(int width, int height) {
        // NOTE: Based on your GameController, you are passing (25, 10).
        // This means 'width' acts as ROWS and 'height' acts as COLUMNS.
        // It is confusing naming, but the logic works consistently with it.
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    // ... [moveBrickDown, moveBrickLeft, moveBrickRight, rotateLeftBrick methods remain the same] ...

    @Override
    public boolean moveBrickDown() {
        int[][] boardCopy = MatrixOperations.copy(currentGameMatrix);
        Point newPos = new Point(currentOffset);
        newPos.translate(0, 1);

        boolean conflict = MatrixOperations.intersect(
                boardCopy,
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

    @Override
    public boolean moveBrickLeft() {
        int[][] boardCopy = MatrixOperations.copy(currentGameMatrix);
        Point newPos = new Point(currentOffset);
        newPos.translate(-1, 0);

        boolean conflict = MatrixOperations.intersect(
                boardCopy,
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

    @Override
    public boolean moveBrickRight() {
        int[][] boardCopy = MatrixOperations.copy(currentGameMatrix);
        Point newPos = new Point(currentOffset);
        newPos.translate(1, 0);

        boolean conflict = MatrixOperations.intersect(
                boardCopy,
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

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    // ======================================================
    //  ViewData + Ghost Piece
    // ======================================================
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

        // FIX 2: GHOST CALCULATION
        // We explicitly use 'brickMatrix' (the current active shape) to calculate the ghost.
        // This ensures the shadow ALWAYS matches the falling block.
        int ghostY = calculateGhostY(brickMatrix, brickX, brickY);
        view.setGhost(brickMatrix, brickX, ghostY);

        return view;
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }

    // ======================================================
    //                  GHOST PIECE HELPERS
    // ======================================================
    private int calculateGhostY(int[][] shape, int startX, int startY) {
        int ghostY = startY;

        // Loop: Keep pushing the ghost down until it hits something
        while (canBrickMoveDown(shape, startX, ghostY)) {
            ghostY++;
        }

        return ghostY;
    }

    /**
     * Checks whether a brick with given shape at (x, y) can move 1 row down
     */
    private boolean canBrickMoveDown(int[][] shape, int x, int y) {

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 0) {
                    continue;
                }

                int newY = y + row + 1;
                int newX = x + col;

                // Check boundaries using the confusing variable names:
                // width = number of rows (25)
                // height = number of cols (10)

                if (newY >= width) { // Bottom boundary
                    return false;
                }

                if (newX < 0 || newX >= height) { // Left/Right boundary
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
}