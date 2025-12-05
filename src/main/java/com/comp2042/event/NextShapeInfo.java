package com.comp2042.event;

import com.comp2042.core.MatrixOperations;


/**
 * An immutable data class that holds information about a brick's shape and its rotational position.
 * This is typically used to convey information about the next potential shape after a rotation
 * or the shape of an upcoming brick for preview.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a NextShapeInfo object.
     *
     * @param shape A 2D integer array representing the shape of the brick.
     * @param position An integer representing the rotational position (index) of the shape.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns a deep copy of the 2D integer array representing the brick's shape.
     * @return A 2D integer array of the shape.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Returns the rotational position (index) of the shape.
     * @return The integer position.
     */
    public int getPosition() {
        return position;
    }
}
