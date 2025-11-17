package com.comp2042;

public final class ViewData {

    // Main piece
    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;

    // Next piece preview
    private final int[][] nextBrickData;

    // Ghost piece
    private int[][] ghostData;
    private int ghostX;
    private int ghostY;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    // Ghost getters/setter
    public int[][] getGhostData() {
        return ghostData;
    }

    public int getGhostX() {
        return ghostX;
    }

    public int getGhostY() {
        return ghostY;
    }

    public void setGhost(int[][] data, int x, int y) {
        this.ghostData = data;
        this.ghostX = x;
        this.ghostY = y;
    }
}
