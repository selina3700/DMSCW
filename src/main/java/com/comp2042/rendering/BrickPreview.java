package com.comp2042.rendering;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Handles rendering of brick previews (next brick and hold brick).
 * Provides a centralized way to display brick previews in GridPanes.
 */
public class BrickPreview{

    private static final int BRICK_SIZE = 22;
    private static final int PREVIEW_GRID_SIZE = 4; // 4x4 preview grid

    private final GridPane nextBrickPanel;
    private final GridPane holdBrickPanel;
    private final ColorProvider colorProvider;

    /**
     * Creates a new BrickPreviewRenderer
     * @param nextBrickPanel The GridPane for displaying the next brick
     * @param holdBrickPanel The GridPane for displaying the held brick
     * @param colorProvider A function to get colors for brick pieces
     */
    public BrickPreview(GridPane nextBrickPanel, GridPane holdBrickPanel, ColorProvider colorProvider) {
        this.nextBrickPanel = nextBrickPanel;
        this.holdBrickPanel = holdBrickPanel;
        this.colorProvider = colorProvider;
    }

    /**
     * Renders the next brick preview
     * @param nextBrickData The data array for the next brick
     */
    public void renderNextBrick(int[][] nextBrickData) {
        if (nextBrickData == null || nextBrickPanel == null) {
            return;
        }
        renderBrickPreview(nextBrickPanel, nextBrickData);
    }

    /**
     * Renders the held brick preview
     * @param heldBrickData The data array for the held brick
     */
    public void renderHoldBrick(int[][] heldBrickData) {
        if (heldBrickData == null || holdBrickPanel == null) {
            return;
        }
        renderBrickPreview(holdBrickPanel, heldBrickData);
    }

    /**
     * Generic method to render a brick preview in any GridPane
     * @param panel The GridPane to render in
     * @param brickData The brick data to render
     */
    private void renderBrickPreview(GridPane panel, int[][] brickData) {
        // Clear existing preview
        panel.getChildren().clear();

        int brickRows = brickData.length;
        int brickCols = brickData[0].length;

        // Calculate offsets to center the shape within the 4x4 grid
        int rowOffset = (PREVIEW_GRID_SIZE - brickRows) / 2;
        int colOffset = (PREVIEW_GRID_SIZE - brickCols) / 2;

        // Render each cell of the brick
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (brickData[i][j] != 0) {
                    Rectangle rect = createBrickRectangle(brickData[i][j]);

                    // Center the rectangle inside the grid cell
                    GridPane.setHalignment(rect, HPos.CENTER);
                    GridPane.setValignment(rect, VPos.CENTER);

                    // Add to panel with offset for centering
                    panel.add(rect, j + colOffset, i + rowOffset);
                }
            }
        }
    }

    /**
     * Creates a styled rectangle for a brick piece
     * @param colorCode The color code for the brick piece
     * @return A styled Rectangle
     */
    private Rectangle createBrickRectangle(int colorCode) {
        Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);

        Color baseColor = colorProvider.getColor(colorCode);
        rect.setFill(baseColor);
        rect.setStroke(getDarkerColor(baseColor));
        rect.setStrokeWidth(1.0);
        rect.setStrokeType(StrokeType.INSIDE);

        return rect;
    }

    /**
     * Gets a darker version of a color for borders
     * @param color The base color
     * @return A darker version of the color
     */
    private Color getDarkerColor(Color color) {
        return color.deriveColor(0, 1, 0.55, 1);
    }

    /**
     * Clears the next brick preview
     */
    public void clearNextBrick() {
        if (nextBrickPanel != null) {
            nextBrickPanel.getChildren().clear();
        }
    }

    /**
     * Clears the hold brick preview
     */
    public void clearHoldBrick() {
        if (holdBrickPanel != null) {
            holdBrickPanel.getChildren().clear();
        }
    }

    /**
     * Clears both previews
     */
    public void clearAll() {
        clearNextBrick();
        clearHoldBrick();
    }

    /**
     * Functional interface for providing colors
     */
    @FunctionalInterface
    public interface ColorProvider {
        Color getColor(int colorCode);
    }
}