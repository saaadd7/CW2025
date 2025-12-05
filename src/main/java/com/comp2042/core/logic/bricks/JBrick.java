package com.comp2042.core.logic.bricks;

import com.comp2042.core.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "J" shaped Tetris brick.
 * This brick has four rotational states.
 */
final class JBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a JBrick.
     * Initializes the {@code brickMatrix} with the four possible orientations of the J-brick.
     * The '2's represent the solid parts of the brick (assuming a color code of 2 for J-bricks).
     */
    public JBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 2, 2, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 2, 2, 2},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 2, 0},
                {0, 0, 2, 0},
                {0, 2, 2, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing the
     * different rotational states of the J-brick.
     *
     * @return A {@link List} of 2D integer arrays, each defining a shape orientation.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
