package com.comp2042.rendering;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


import java.awt.Point;

/**
 * Calculates, manages, and renders the ghost piece.
 * The ghost piece shows a preview of where the current brick will land.
 */
public class GhostPiece {

    private static final int BRICK_SIZE = 22;

    private Point ghostPosition;
    private Rectangle[][] ghostRectangles;
    private GridPane gamePanel;

    public GhostPiece() {
        this.ghostPosition = new Point(0, 0);
    }

    /**
     * Initializes the ghost piece rectangles and adds them to the game panel
     * @param gamePanel The GridPane where the ghost will be displayed
     */
    public void initialize(GridPane gamePanel) {
        this.gamePanel = gamePanel;
        this.ghostRectangles = new Rectangle[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle ghostRect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghostRect.setFill(Color.TRANSPARENT);
                ghostRect.setStroke(Color.TRANSPARENT);
                ghostRect.setStrokeType(StrokeType.INSIDE);
                ghostRectangles[i][j] = ghostRect;
                gamePanel.add(ghostRect, j, 0);  // Will be repositioned later
            }
        }
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
     * Updates the visual display of the ghost piece
     * @param brickData The current brick's data
     * @param currentY The current Y position of the brick
     * @param colorProvider A functional interface to get colors for brick pieces
     */
    public void refresh(int[][] brickData, int currentY, ColorProvider colorProvider) {
        if (ghostRectangles == null || brickData == null) {
            return;
        }

        int ghostY = ghostPosition.y;
        int ghostX = ghostPosition.x;

        // Only show ghost if it's below the current brick position
        boolean showGhost = (ghostY > currentY);

        for (int i = 0; i < ghostRectangles.length; i++) {
            for (int j = 0; j < ghostRectangles[i].length; j++) {
                Rectangle ghostRect = ghostRectangles[i][j];

                // Check if this cell is part of the brick shape
                boolean isPartOfBrick = (i < brickData.length && j < brickData[i].length
                        && brickData[i][j] != 0);

                if (showGhost && isPartOfBrick) {
                    // Calculate board position
                    int boardRow = ghostY - 2 + i;
                    int boardCol = ghostX + j;

                    // Only show if within bounds
                    if (boardRow >= 0 && boardRow < 23 && boardCol >= 0 && boardCol < 10) {
                        Color baseColor = colorProvider.getColor(brickData[i][j]);

                        // Apply ghost styling: semi-transparent with outlined border
                        applyGhostStyle(ghostRect, baseColor);

                        // Position the ghost rectangle
                        GridPane.setRowIndex(ghostRect, boardRow);
                        GridPane.setColumnIndex(ghostRect, boardCol);
                    } else {
                        // Out of bounds - hide
                        hideRectangle(ghostRect);
                    }
                } else {
                    // Not showing ghost - hide this cell
                    hideRectangle(ghostRect);
                }
            }
        }
    }

    /**
     * Applies the ghost piece visual style to a rectangle
     * @param rect The rectangle to style
     * @param baseColor The base color of the brick piece
     */
    private void applyGhostStyle(Rectangle rect, Color baseColor) {
        // Semi-transparent fill
        rect.setFill(baseColor.deriveColor(0, 0.5, 1, 0.25));

        // Visible outline
        rect.setStroke(baseColor.deriveColor(0, 1, 0.8, 0.6));
        rect.setStrokeWidth(2.0);
    }

    /**
     * Hides a rectangle by making it transparent
     * @param rect The rectangle to hide
     */
    private void hideRectangle(Rectangle rect) {
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.TRANSPARENT);
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

    /**
     * Functional interface for providing colors
     */
    @FunctionalInterface
    public interface ColorProvider {
        Color getColor(int colorCode);
    }
}