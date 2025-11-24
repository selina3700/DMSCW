package com.comp2042;

import java.awt.Point;

/**
 * Calculates and manages the ghost piece position.
 * The ghost piece shows a preview of where the current brick will land.
 */
public class GhostPiece {

    private Point ghostPosition;

    public GhostPiece() {
        this.ghostPosition = new Point(0, 0);
    }

    /**
     * Calculates where the current brick would land if dropped straight down.
     *
     * @param gameMatrix The current game board state
     * @param brickShape The current brick's shape
     * @param currentX Current X position of the brick
     * @param currentY Current Y position of the brick
     * @return The Y position where the brick would land
     */
    public int calculateGhostY(int[][] gameMatrix, int[][] brickShape, int currentX, int currentY) {
        if (gameMatrix == null || brickShape == null) {
            return currentY;
        }

        int ghostY = currentY;

        // Keep moving down until we hit something
        while (!wouldCollide(gameMatrix, brickShape, currentX, ghostY + 1)) {
            ghostY++;
        }

        ghostPosition = new Point(currentX, ghostY);
        return ghostY;
    }

    /**
     * Checks if the brick would collide at the given position.
     * This mimics the logic in MatrixOperations.intersect()
     */
    private boolean wouldCollide(int[][] gameMatrix, int[][] brickShape, int x, int y) {
        for (int i = 0; i < brickShape.length; i++) {
            for (int j = 0; j < brickShape[i].length; j++) {
                if (brickShape[i][j] != 0) {
                    int boardY = y + i;
                    int boardX = x + j;

                    // Check if out of bounds (bottom)
                    if (boardY >= gameMatrix.length) {
                        return true;
                    }

                    // Check if out of bounds (sides)
                    if (boardX < 0 || boardX >= gameMatrix[0].length) {
                        return true;
                    }

                    // Check if there's already a block there
                    if (boardY >= 0 && gameMatrix[boardY][boardX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the current ghost position X coordinate
     */
    public int getGhostX() {
        return ghostPosition.x;
    }

    /**
     * Gets the current ghost position Y coordinate
     */
    public int getGhostY() {
        return ghostPosition.y;
    }
}