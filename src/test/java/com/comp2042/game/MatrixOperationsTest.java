package com.comp2042.game;

import com.comp2042.models.ClearRow;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void intersect() {
        //No collision
        int[][] matrix = new int[10][10];
        int[][] brick = {{1, 1}, {1, 1}};
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0));

        //Collision with filled field
        matrix[5][5] = 1;
        int[][] brick2 = {{1, 1}, {1, 1}};
        assertTrue(MatrixOperations.intersect(matrix, brick2, 4, 4));

        //Out of bounds on the right
        assertTrue(MatrixOperations.intersect(matrix, brick, 9, 0));

        //Out of bounds at the bottom
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 9));

        //Out of bounds - negative x
        assertTrue(MatrixOperations.intersect(matrix, brick, -1, 0));

        //Brick with empty cells
        int[][] sparseBrick = {{1, 0}, {0, 1}};
        assertFalse(MatrixOperations.intersect(matrix, sparseBrick, 0, 0));
    }

    @Test
    void copy() {
        //Copy a simple matrix
        int[][] original = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] copied = MatrixOperations.copy(original);

        //Verify contents are the same
        assertArrayEquals(original, copied);

        //Verify it's a deep copy
        copied[1][1] = 99;
        assertNotEquals(original[1][1], copied[1][1]);
        assertEquals(5, original[1][1]);
        assertEquals(99, copied[1][1]);

        //Copy empty matrix
        int[][] emptyMatrix = new int[5][5];
        int[][] copiedEmpty = MatrixOperations.copy(emptyMatrix);
        assertArrayEquals(emptyMatrix, copiedEmpty);
    }

    @Test
    void merge() {
        //Simple merge
        int[][] filledFields = new int[10][10];
        filledFields[8][0] = 2;
        filledFields[9][0] = 2;

        int[][] brick = {{1, 1}, {1, 1}};
        int[][] result = MatrixOperations.merge(filledFields, brick, 2, 3);

        //Merged at the correct position
        assertEquals(1, result[3][2]);
        assertEquals(1, result[3][3]);
        assertEquals(1, result[4][2]);
        assertEquals(1, result[4][3]);

        //Original Filled Fields
        assertEquals(2, result[8][0]);
        assertEquals(2, result[9][0]);

        //Verify original matrix not modified
        assertEquals(0, filledFields[3][2]);

        //Merge with sparse brick
        int[][] sparseBrick = {{1, 0}, {0, 2}};
        int[][] result2 = MatrixOperations.merge(filledFields, sparseBrick, 5, 5);
        assertEquals(1, result2[5][5]);
        assertEquals(0, result2[5][6]);
        assertEquals(0, result2[6][5]);
        assertEquals(2, result2[6][6]);

        //Merge at edge
        int[][] result3 = MatrixOperations.merge(filledFields, brick, 9, 9);
        assertEquals(1, result3[9][9]);
    }

    @Test
    void checkRemoving() {
        //No rows to clear
        int[][] matrix = new int[10][10];
        matrix[9][0] = 1;
        matrix[9][1] = 1;

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());

        //Clear one full row
        int[][] matrix2 = new int[10][10];
        for (int i = 0; i < 10; i++) {
            matrix2[9][i] = 1;
        }
        matrix2[8][0] = 2;

        ClearRow result2 = MatrixOperations.checkRemoving(matrix2);
        assertEquals(1, result2.getLinesRemoved());
        assertEquals(50, result2.getScoreBonus()); // 50 * 1 * 1

        //Clear multiple rows
        int[][] matrix3 = new int[10][10];
        for (int i = 0; i < 10; i++) {
            matrix3[8][i] = 1;
            matrix3[9][i] = 2;
        }
        matrix3[7][5] = 3;

        ClearRow result3 = MatrixOperations.checkRemoving(matrix3);
        assertEquals(2, result3.getLinesRemoved());
        assertEquals(200, result3.getScoreBonus());

        //Verify blocks dropped correctly
        assertEquals(3, result3.getNewMatrix()[9][5]);
        assertEquals(0, result3.getNewMatrix()[8][5]);

        //Clear all rows
        int[][] matrix4 = new int[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrix4[i][j] = 1;
            }
        }

        ClearRow result4 = MatrixOperations.checkRemoving(matrix4);
        assertEquals(4, result4.getLinesRemoved());
        assertEquals(800, result4.getScoreBonus());
    }

    @Test
    void deepCopyList() {
        //Deep copy list of matrices
        List<int[][]> originalList = new ArrayList<>();
        originalList.add(new int[][]{{1, 2}, {3, 4}});
        originalList.add(new int[][]{{5, 6}, {7, 8}});

        List<int[][]> copiedList = MatrixOperations.deepCopyList(originalList);

        //Verify same size
        assertEquals(originalList.size(), copiedList.size());

        //Verify contents are the same
        assertArrayEquals(originalList.get(0), copiedList.get(0));
        assertArrayEquals(originalList.get(1), copiedList.get(1));

        //Verify it's a deep copy
        copiedList.get(0)[0][0] = 99;
        assertEquals(1, originalList.get(0)[0][0]);
        assertEquals(99, copiedList.get(0)[0][0]);

        //Empty list
        List<int[][]> emptyList = new ArrayList<>();
        List<int[][]> copiedEmpty = MatrixOperations.deepCopyList(emptyList);
        assertEquals(0, copiedEmpty.size());
    }
}