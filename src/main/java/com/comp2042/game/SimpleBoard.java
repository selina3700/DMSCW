package com.comp2042.game;

import com.comp2042.*;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.models.ClearRow;
import com.comp2042.models.NextShapeInfo;
import com.comp2042.models.Score;
import com.comp2042.models.ViewData;
import com.comp2042.rendering.GhostPiece;

import java.awt.*;

/**
 * Concrete implementation of the {@code Board} interface, representing the core
 * game model and state for Tetris
 * <p>
 *     This class managed the grid matrix, the current falling brick movement, rotation, line clearing, score, and brick
 *     holding.
 * </p>
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final GhostPiece ghostPiece;

    private Brick currentBrick;
    private Brick heldBrick;
    private boolean hasHeldThisTurn = false;

    /**
     * Constructs a new SimpleBoard with specified dimensions.
     * <p>
     *     Initializes the game matrix, brick generator, brick rotator, score and ghost piece logic.
     * </p>
     * @param width Width of the game board
     * @param height Height of the game board
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
        ghostPiece = new GhostPiece();
    }

    /**
     * Attempts to move the current falling brick one unit down
     * <p>
     *     If moving down causes an intersection with the background or the board boundaries, the movement
     *     is prevented.
     * </p>
     * @return True if the brick was successfully moved, false if conflict was detected
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current falling brick one unit to the left.
     * @return {@code true} if the brick was successfully moved, {@code false} if a conflict was detected.
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current falling brick one unit to the right.
     * @return {@code true} if the brick was successfully moved, {@code false} if a conflict was detected.
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }
    
    /**
     * Attempts to rotate the current falling brick to its next rotation
     * <p>
     *      Rotation is applied only if the resulting shape does not conflict with existing blocks or board boundaries 
     *      at current offset.
     * </p>
     * @return {@code true} if the brick was successfully rotated, {@code false} if otherwise.
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Generates a new falling brick from the brick generator and initializes its position and rotation.
     * <p>
     *     Resets the {@code hasHeldThisTurn} flag
     * </p>
     * @return {@code true} if the new brick immediately intersects, {@code false} if otherwise
     */
    @Override
    public boolean createNewBrick() {
        currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        hasHeldThisTurn = false;
        currentOffset = new Point(3, 1);

        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(),
                (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Swaps the current falling brick with the brick held in the hold slot
     * <p>
     *     If slot is empty, current brick is placed inside, otherwise it will swap out the brick inside with the current brick.
     *     This operation can only be performed once per brick turn.
     * </p>
     */
    public void holdBrick() {
        if (hasHeldThisTurn) {
            return;
        }

        if (heldBrick == null) {
            heldBrick = currentBrick;
            createNewBrick();
        } else {
            Brick temp = currentBrick;
            currentBrick = heldBrick;
            heldBrick = temp;

            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(3, 1);
        }

        hasHeldThisTurn = true;
    }

    /**
     * Retrieves the current board matrix, including all merged blocks.
     *
     * @return The 2D integer array representing the permanent board state.
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * Collects and returns all necessary data for the GUI to render.
     * <p>
     *     Includes current brick's shape and position, the next and held brick preview, calculated position of the ghost piece.
     * </p>
     * @return A {@code ViewData} object containing the render-ready state.
     */
    @Override
    public ViewData getViewData() {
        int[][] heldMatrix = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;

        int ghostY = ghostPiece.calculateGhostY(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                heldMatrix,
                (int) currentOffset.getX(),
                ghostY
        );
    }

    /**
     * Merges the current falling brick into the permanent background.
     * This is called when the brick lands and locks into place.
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Scans the board for completed rows, removes them and calculates the score.
     * <p>
     *     Updates the internal {@code currentGameMatrix} with the cleared and compacted board.
     * </p>
     * @return A {@code ClearRow} object containing information about the operation
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    /**
     * Returns the score manager
     * @return {@code Score} object
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the game state for a new game
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        brickGenerator = new RandomBrickGenerator();
        createNewBrick();
    }
}