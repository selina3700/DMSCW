package com.comp2042;

public class ViewData {
    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int[][] heldBrickData;

    // Updated Constructor
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.heldBrickData = heldBrickData;
    }

    public int[][] getHeldBrickData() {
        return heldBrickData;
    }

    public int[][] getBrickData() { return brickData; }
    public int getxPosition() { return xPosition; }
    public int getyPosition() { return yPosition; }
    public int[][] getNextBrickData() { return nextBrickData; }
}