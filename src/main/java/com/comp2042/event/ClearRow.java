package com.comp2042.event;

import com.comp2042.core.MatrixOperations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the result of clearing one or more rows from the game board.
 * This immutable class encapsulates information about the number of lines removed,
 * the updated game matrix, the score bonus obtained, and the indices of the cleared rows.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedRows;

    /**
     * Constructs a ClearRow object.
     *
     * @param linesRemoved The total number of lines that were removed.
     * @param newMatrix The state of the game board after the rows have been removed and blocks shifted down.
     * @param scoreBonus The score awarded for clearing these lines.
     * @param clearedRows A list of integer indices representing the rows that were cleared.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        // Defensive copy for mutable list
        this.clearedRows = clearedRows != null ? new ArrayList<>(clearedRows) : new ArrayList<>();
    }

    /**
     * Returns the number of lines that were removed in this operation.
     * @return The count of removed lines.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a deep copy of the game board matrix after the rows have been cleared.
     * @return A 2D integer array representing the new game matrix.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the score bonus awarded for clearing these lines.
     * @return The score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }

    /**
     * Returns a new {@link ArrayList} containing the indices of the rows that were cleared.
     * This is a defensive copy to prevent external modification of the internal list.
     * @return A list of integer row indices.
     */
    public List<Integer> getClearedRows() {
        return new ArrayList<>(clearedRows);
    }
}