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

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        // matrix is [row][col] = [y][x]
        // brick is [row][col] = [y][x]
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                if (brick[row][col] != 0) {
                    int targetRow = y + row;  // y offset + brick row
                    int targetCol = x + col;  // x offset + brick col

                    if (checkOutOfBound(matrix, targetCol, targetRow)) {
                        return true; // Out of bounds = collision
                    }
                    if (matrix[targetRow][targetCol] != 0) {
                        return true; // Collision with existing block
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int col, int row) {
        // matrix is [row][col], so check row against matrix.length and col against matrix[0].length
        if (row >= 0 && col >= 0 && row < matrix.length && col < matrix[0].length) {
            return false; // Within bounds
        }
        return true; // Out of bounds
    }

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

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}