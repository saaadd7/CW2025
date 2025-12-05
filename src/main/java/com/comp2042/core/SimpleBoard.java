package com.comp2042.core;

import com.comp2042.event.ClearRow;
import com.comp2042.event.NextShapeInfo;
import com.comp2042.event.ViewData;
import com.comp2042.core.logic.bricks.Brick;
import com.comp2042.core.logic.bricks.BrickGenerator;
import com.comp2042.core.logic.bricks.RandomBrickGenerator;

import java.awt.Point;
import java.util.List;

public class SimpleBoard implements Board {

    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    public SimpleBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        currentGameMatrix = new int[rows][cols];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }



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

    @Override
    public boolean moveBrickDown() {
        return moveBrick(0, 1);
    }

    @Override
    public boolean moveBrickLeft() {
        return moveBrick(-1, 0);
    }

    @Override
    public boolean moveBrickRight() {
        return moveBrick(1, 0);
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
        currentGameMatrix = new int[rows][cols];
        score.reset();
        createNewBrick();
    }


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
}