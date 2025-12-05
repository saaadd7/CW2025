package com.comp2042.core.logic.bricks;

import java.util.List;

/**
 * Defines the contract for a Tetris brick.
 * Implementations of this interface provide the various shapes and their rotations.
 */
public interface Brick {

    /**
     * Returns a list of 2D integer arrays, where each array represents a different
     * rotational state of the brick. The integers within the arrays typically
     * represent colors or block types, with 0 indicating an empty space.
     * @return A {@link List} of 2D integer arrays, each defining a shape orientation.
     */
    List<int[][]> getShapeMatrix();
}
