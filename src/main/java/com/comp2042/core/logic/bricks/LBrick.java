package com.comp2042.core.logic.bricks;

import com.comp2042.core.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "L" shaped Tetris brick.
 * This brick has four rotational states.
 */
final class LBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an LBrick.
     * Initializes the {@code brickMatrix} with the four possible orientations of the L-brick.
     * The '3's represent the solid parts of the brick (assuming a color code of 3 for L-bricks).
     */
    public LBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 3},
                {0, 3, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 3, 0},
                {0, 0, 3, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 3, 0},
                {3, 3, 3, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing the
     * different rotational states of the L-brick.
     *
     * @return A {@link List} of 2D integer arrays, each defining a shape orientation.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
