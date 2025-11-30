package com.comp2042;

import java.util.ArrayList;
import java.util.List;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedRows;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows != null ? new ArrayList<>(clearedRows) : new ArrayList<>();
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public List<Integer> getClearedRows() {
        return new ArrayList<>(clearedRows);
    }
}