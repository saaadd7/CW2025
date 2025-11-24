package com.comp2042;

public final class ViewData {

    // Main piece
    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;

    // Next piece preview
    private final int[][] nextBrickData1;
    private final int[][] nextBrickData2;
    private final int[][] nextBrickData3;


    // Ghost piece
    private int[][] ghostData;
    private int ghostX;
    private int ghostY;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData1, int[][] nextBrickData2, int[][] nextBrickData3) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData1 = nextBrickData1;
        this.nextBrickData2 = nextBrickData2;
        this.nextBrickData3 = nextBrickData3;
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

    public int[][] getNextBrickData1() {
        return MatrixOperations.copy(nextBrickData1);
    }

    public int[][] getNextBrickData2() {
        return MatrixOperations.copy(nextBrickData2);
    }

    public int[][] getNextBrickData3() {
        return MatrixOperations.copy(nextBrickData3);
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
