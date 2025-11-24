package com.comp2042;

public class ViewData {
    private int[][] brickData;  // Current piece shape
    private int ghostX;         // X position of the ghost
    private int ghostY;         // Y position of the ghost

    public ViewData(int[][] brickData) {
        this.brickData = brickData;
    }

    public int[][] getBrickData() {
        return brickData;
    }

    public int[][] getGhostData() {
        return brickData; // Just returning the same data for simplicity
    }

    public int getGhostX() {
        return ghostX;
    }

    public int getGhostY() {
        return ghostY;
    }

    public void setGhostPosition(int x, int y) {
        this.ghostX = x;
        this.ghostY = y;
    }
}
