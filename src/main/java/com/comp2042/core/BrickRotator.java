package com.comp2042.core;

import com.comp2042.core.logic.bricks.Brick;
import com.comp2042.event.NextShapeInfo;
import java.util.List;

/**
 * Manages the rotation state and current shape of a Tetris brick.
 * It keeps track of the brick's orientation and provides the next shape
 * in the rotation sequence.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates the next rotational state of the current brick without applying it.
     *
     * @return A {@link NextShapeInfo} object containing the 2D array of the next shape
     *         and its corresponding rotation position index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Returns the 2D array representation of the brick's current shape (orientation).
     *
     * @return A 2D integer array representing the current shape of the brick.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotational state (position) of the brick.
     * This method is typically called after a successful rotation.
     *
     * @param currentShape The index representing the new current shape (orientation).
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick that this rotator will manage.
     * When a new brick is set, its rotation state is reset to the first orientation (0).
     *
     * @param brick The {@link Brick} object to be managed.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
