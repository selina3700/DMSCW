package com.comp2042.models;

import com.comp2042.game.MatrixOperations;

/**
 * A data transfer object (DTO) that encapsulates the result of the line-clearing
 * operation on the game board
 * <p>
 *     This object is passed from the game model to the controller after a brick is locked and
 *     rows have been checked for completion.
 * </p>
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a new CLearRow result object
     * @param linesRemoved The total number of rows cleared
     * @param newMatrix Resulting 2D array of the game board
     * @param scoreBonus Calculated score bonus awarded for this
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines that were cleared
     * @return Number of lines removed
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets a copy of the resulting game matric after the rows have been cleared
     * @return A copy of the new board matrix
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus calculated for the rows cleared
     * @return The score bonus awarded
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
