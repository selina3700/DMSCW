package com.comp2042.models;

/**
 * Holds all necessary info for the GUI to render the current state of the falling brick, its position
 * and the preview/hold pieces.
 * <p>
 *    This object acts as a bridge between the game model and the view.
 * </p>
 */
public class ViewData {
    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int[][] heldBrickData;

    // Ghost piece coordinates
    private final int ghostX;
    private final int ghostY;

    /**
     * Constructs a new ViewData object containing all the current render information.
     * @param brickData The 2D array of the current falling brick's shape.
     * @param xPosition The column index (x-coordinate) of the falling brick's top-left corner on the board.
     * @param yPosition The 2D array of the next piece, used for the preview panel
     * @param nextBrickData The 2D array of the next piece
     * @param heldBrickData The 2D array of the currently held piece, or {@code null} if none
     * @param ghostX Column index of ghost brick
     * @param ghostY Row index of ghost brick
     */
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

    /**
     * Gets the shape matrix of the current falling brick.
     * @return The 2D array representing the falling brick.
     */
    public int[][] getBrickData() {
        return brickData;
    }

    /**
     * Gets the column index (x-coordinate) of the falling brick
     * @return The x position
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the row index (y-coordinate) of the falling brick
     * @return The y position
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets the shape matrix of the next piece to be generated
     * @return The 2D array representing the next brick
     */
    public int[][] getNextBrickData() {
        return nextBrickData;
    }

    /**
     * Gets the shape matrix of the currently held piece
     * @return The 2D array representing the held brick
     */
    public int[][] getHeldBrickData() {
        return heldBrickData;
    }

    /**
     * Gets the column index (x-coordinate) of the ghost piece.
     *
     * @return The x position of the ghost piece.
     */
    public int getGhostX() {
        return ghostX;
    }

    /**
     * Gets the row index (y-coordinate) of the ghost piece, indicating where the falling brick will land.
     *
     * @return The y position of the ghost piece.
     */
    // Ghost piece getters
    public int getGhostY() {
        return ghostY;
    }
}