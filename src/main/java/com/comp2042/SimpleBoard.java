package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] boardCopy = MatrixOperations.copy(currentGameMatrix);
        Point newPos = new Point(currentOffset);
        newPos.translate(0, 1);

        boolean conflict = MatrixOperations.intersect(boardCopy,
                brickRotator.getCurrentShape(),
                newPos.x, newPos.y);

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

        boolean conflict = MatrixOperations.intersect(boardCopy,
                brickRotator.getCurrentShape(),
                newPos.x, newPos.y);

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

        boolean conflict = MatrixOperations.intersect(boardCopy,
                brickRotator.getCurrentShape(),
                newPos.x, newPos.y);

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

        boolean conflict = MatrixOperations.intersect(boardCopy,
                nextShape.getShape(),
                currentOffset.x, currentOffset.y);

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

        // spawn position
        currentOffset = new Point(4, 10);

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
    //  Ghost Piece logic
    // ======================================================
    @Override
    public ViewData getViewData() {

        int[][] brickMatrix = brickRotator.getCurrentShape();
        int brickX = currentOffset.x;
        int brickY = currentOffset.y;

        ViewData view = new ViewData(
                brickMatrix,
                brickX,
                brickY,
                brickGenerator.getNextBrick().getShapeMatrix().get(0)  // next shape preview
        );

        // === Calculate ghost Y ===
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
    private int calculateGhostY(int[][] brickShape, int startX, int startY) {
        int ghostY = startY;

        while (canBrickMoveDown(brickShape, startX, ghostY)) {
            ghostY++;
        }

        return ghostY;
    }

    private boolean canBrickMoveDown(int[][] shape, int x, int y) {

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 0)
                    continue;

                int newY = y + row + 1;
                int newX = x + col;

                // bottom boundary
                if (newY >= height)
                    return false;

                // collision with background
                if (currentGameMatrix[newY][newX] != 0)
                    return false;
            }
        }

        return true;
    }
}
