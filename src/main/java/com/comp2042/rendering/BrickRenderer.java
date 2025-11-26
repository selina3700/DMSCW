package com.comp2042.rendering;

import com.comp2042.models.ViewData;
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Handles all visual rendering of Tetris bricks and the game board.
 * Manages colors, positioning, and visual updates for both falling bricks and the background.
 */
public class BrickRenderer {

    private static final int BRICK_SIZE = 22;

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final BooleanProperty isPause;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private GhostPiece ghostPiece;
    private BrickPreview brickPreview;

    /**
     * Constructor for BrickRenderer
     *
     * @param gamePanel The main game grid panel
     * @param brickPanel The overlay panel for falling bricks
     * @param isPause Property tracking pause state
     */
    public BrickRenderer(GridPane gamePanel, GridPane brickPanel, BooleanProperty isPause) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.isPause = isPause;
    }

    /**
     * Initializes the display matrices and brick preview components
     */
    public void initializeBrickPreview(GridPane nextBrickPanel, GridPane holdBrickPanel) {
        ghostPiece = new GhostPiece();
        ghostPiece.initialize(gamePanel);

        brickPreview = new BrickPreview(
                nextBrickPanel,
                holdBrickPanel,
                colorCode -> getFillColor(colorCode)
        );
    }

    /**
     * Initializes the game board background grid
     */
    public void initializeBackground(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }
    }

    /**
     * Initializes the falling brick overlay
     */
    public void initializeFallingBrick(ViewData brick) {
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setArcHeight(0);
                rectangle.setArcWidth(0);
                rectangle.setStrokeType(StrokeType.INSIDE);

                int colorCode = brick.getBrickData()[i][j];

                if (colorCode == 0) {
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setStroke(Color.TRANSPARENT);
                } else {
                    Color base = getFillColor(colorCode);
                    rectangle.setFill(base);
                    rectangle.setStroke(base.darker());
                    rectangle.setStrokeWidth(1.0);
                }

                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        positionBrickPanel(brick);
    }

    /**
     * Updates the visual position and appearance of the falling brick
     */
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            positionBrickPanel(brick);

            int boardWidth = 10;
            int boardHeight = 23;

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    int cellX = brick.getxPosition() + j;
                    int cellY = brick.getyPosition() - 2 + i;

                    // Hide cells outside visible board
                    if (cellX < 0 || cellX >= boardWidth || cellY < 0 || cellY >= boardHeight) {
                        rectangles[i][j].setFill(Color.TRANSPARENT);
                        rectangles[i][j].setStroke(Color.TRANSPARENT);
                    } else {
                        setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                    }
                }
            }

            // Update brick preview
            if (brickPreview != null) {
                if (brick.getNextBrickData() != null) {
                    brickPreview.renderNextBrick(brick.getNextBrickData());
                }
                if (brick.getHeldBrickData() != null) {
                    brickPreview.renderHoldBrick(brick.getHeldBrickData());
                }
            }

            // Update ghost piece
            if (ghostPiece != null) {
                ghostPiece.refresh(brick.getBrickData(), brick.getyPosition(),
                        colorCode -> (Color) getFillColor(colorCode));
            }
        }
    }

    /**
     * Refreshes the entire game background grid
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Positions the brick panel at the correct location
     */
    private void positionBrickPanel(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(gamePanel.getLayoutY() + (brick.getyPosition() - 2) * BRICK_SIZE);
    }

    /**
     * Applies color and stroke to a rectangle
     */
    private void setRectangleData(int colorCode, Rectangle rectangle) {
        Color base = getFillColor(colorCode);
        rectangle.setFill(base);
        rectangle.setStroke(getDarker(base));
        rectangle.setStrokeWidth(1.2);
        rectangle.setStrokeType(StrokeType.INSIDE);
    }

    /**
     * Gets the fill color for a given brick color code
     */
    private Color getFillColor(int i) {
        Color returnColor;
        switch (i) {
            case 0:
                returnColor = Color.TRANSPARENT;
                break;
            case 1:
                returnColor = Color.web("#D24447");
                break;
            case 2:
                returnColor = Color.web("#EB8C4D");
                break;
            case 3:
                returnColor = Color.web("#D174FF");
                break;
            case 4:
                returnColor = Color.web("#5AC8FB");
                break;
            case 5:
                returnColor = Color.web("#FFEE57");
                break;
            case 6:
                returnColor = Color.web("#74FF64");
                break;
            case 7:
                returnColor = Color.web("#FA74FF");
                break;
            default:
                returnColor = Color.web("#FFFFFF");
                break;
        }
        return returnColor;
    }

    /**
     * Returns a darker version of the given color for borders
     */
    private Color getDarker(Color color) {
        return color.deriveColor(0, 1, 0.55, 1);
    }

    /**
     * Renders the initial next brick preview
     */
    public void renderInitialNextBrick(int[][] nextBrickData) {
        if (brickPreview != null) {
            brickPreview.renderNextBrick(nextBrickData);
        }
    }
}