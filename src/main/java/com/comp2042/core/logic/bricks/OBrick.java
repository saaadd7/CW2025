package com.comp2042.core.logic.bricks;

import com.comp2042.core.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "O" shaped Tetris brick (square).
 * This brick has only one rotational state.
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an OBrick.
     * Initializes the {@code brickMatrix} with the single orientation of the O-brick.
     * The '4's represent the solid parts of the brick (assuming a color code of 4 for O-bricks).
     */
    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing the
     * rotational states of the O-brick. Since it's a square, it only has one unique state.
     *
     * @return A {@link List} of 2D integer arrays, each defining a shape orientation.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
