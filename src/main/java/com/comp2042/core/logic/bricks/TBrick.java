package com.comp2042.core.logic.bricks;

import com.comp2042.core.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "T" shaped Tetris brick.
 * This brick has four rotational states.
 */
final class TBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a TBrick.
     * Initializes the {@code brickMatrix} with the four possible orientations of the T-brick.
     * The '6's represent the solid parts of the brick (assuming a color code of 6 for T-bricks).
     */
    public TBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {6, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 6, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 0, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing the
     * different rotational states of the T-brick.
     *
     * @return A {@link List} of 2D integer arrays, each defining a shape orientation.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
