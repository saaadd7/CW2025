package com.comp2042.core.logic.bricks;

import com.comp2042.core.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "Z" shaped Tetris brick.
 * This brick has two rotational states.
 */
final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a ZBrick.
     * Initializes the {@code brickMatrix} with the two possible orientations of the Z-brick.
     * The '7's represent the solid parts of the brick (assuming a color code of 7 for Z-bricks).
     */
    public ZBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing the
     * different rotational states of the Z-brick.
     *
     * @return A {@link List} of 2D integer arrays, each defining a shape orientation.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
