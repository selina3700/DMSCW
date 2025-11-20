package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    // --- NEW VARIABLES FOR HOLD FUNCTION ---
    private Brick currentBrick; // Track the active brick
    private Brick heldBrick;    // Track the held brick
    private boolean hasHeldThisTurn = false; // Prevent infinite swapping
    // ---------------------------------------

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    // ... (Keep moveBrickDown, Left, Right, Rotate logic exactly the same) ...
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

    @Override
    public boolean createNewBrick() {
        // 1. Assign to currentBrick variable
        currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);

        // 2. Reset the hold flag for the new turn
        hasHeldThisTurn = false;

        currentOffset = new Point(4, 0);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    // --- NEW HOLD METHOD ---
    public void holdBrick() {
        if (hasHeldThisTurn) {
            return; // Can only hold once per drop
        }

        if (heldBrick == null) {
            // Scenario 1: No brick held yet. Save current, get new.
            heldBrick = currentBrick;
            createNewBrick();
        } else {
            // Scenario 2: Swap current with held.
            Brick temp = currentBrick;
            currentBrick = heldBrick;
            heldBrick = temp;

            // Setup the swapped brick
            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(4, 0); // Reset position to top
        }

        hasHeldThisTurn = true;
    }
    // -----------------------

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        // We need to send the held brick data to the GUI
        int[][] heldMatrix = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                heldMatrix
        );
    }

    // ... (Keep mergeBrickToBackground, clearRows, getScore, newGame, isValidPosition the same) ...
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null; // Reset held brick on new game
        createNewBrick();
    }

    public boolean isValidPosition(int[][] brickData, int x, int y) {
        return !MatrixOperations.intersect(currentGameMatrix, brickData, x, y);
    }
}