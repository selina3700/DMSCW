package com.comp2042.game;

import com.comp2042.models.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {

    private MatrixOperations(){
    }

    /**
     * Checks if the given brick placed on the board, intersects with existing filled fields or goes out of bounds.
     *
     * @param matrix 2D array representing the game board background
     * @param brick 2D array representing the brick shape
     * @param x Column index
     * @param y Row index
     * @return {@code true} if there is a collision
     * {@code false} if the brick can be placed there freely
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                if (brick[row][col] != 0) {
                    int targetRow = y + row;
                    int targetCol = x + col;

                    if (checkOutOfBound(matrix, targetCol, targetRow)) {
                        return true;
                    }
                    if (matrix[targetRow][targetCol] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Ensures that the brick (x,y) stay within the board
     *
     * @param matrix 2D array representing the game board.
     * @param col Column index
     * @param row Row index
     * @return True if within bounds, otherwise false.
     */
    private static boolean checkOutOfBound(int[][] matrix, int col, int row) {
        if (row >= 0 && col >= 0 && row < matrix.length && col < matrix[0].length) {
            return false;
        }
        return true;
    }

    /**
     * Performs a deep copy of a 2D integer array
     *
     * @param original The 2D array to be copied
     * @return New, independent 2D array containing the same values as the original.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges the fallen brick into the background matrix at the specified position
     * @param filledFields 2D array representing the existing the background block.
     * @param brick 2D array representing the falling brick's shape.
     * @param x Column Index
     * @param y Row Index
     * @return 2D array representing the board state after the brick has been merged.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                if (brick[row][col] != 0) {
                    int targetRow = y + row;
                    int targetCol = x + col;

                    if (targetRow >= 0 && targetRow < copy.length && targetCol >= 0 && targetCol < copy[0].length) {
                        copy[targetRow][targetCol] = brick[row][col];
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Checks the board for filled rows, remove the rows and adjusts the score.
     * <p>
     *     Copies non-cleared rows to the top of a new matrix, effectively dropping blocks.
     * </p>
     * @param matrix The 2D array representing the current state of the board fields.
     * @return {@code ClearRow} object containing the count of lines removed, the resulting new board matrix, and the
     * calculated score bonus.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    /**
     * Performs a deep copy of a list containing 2D integer arrays.
     * Each inner 2D array is copied independently
     * @param list The {@code List<int[][]>} to be copied
     * @return A new {@code List<int[][]>} where all elements are independent deep copies.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}