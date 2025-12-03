package com.comp2042.models;

public class ViewData {
    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int[][] heldBrickData;

    // Ghost piece coordinates
    private final int ghostX;
    private final int ghostY;

    // Updated Constructor with ghost data
    public ViewData(int[][] brickData, int xPosition, int yPosition,
                    int[][] nextBrickData, int[][] heldBrickData,
                    int ghostX, int ghostY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.heldBrickData = heldBrickData;
        this.ghostX = ghostX;
        this.ghostY = ghostY;
    }

    public int[][] getBrickData() {
        return brickData;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return nextBrickData;
    }

    public int[][] getHeldBrickData() {
        return heldBrickData;
    }

    // Ghost piece getters
    public int getGhostY() {
        return ghostY;
    }
}