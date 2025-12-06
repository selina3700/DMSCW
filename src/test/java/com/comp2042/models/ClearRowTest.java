package com.comp2042.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    private ClearRow clearRow;
    private int[][] testMatrix;

    @BeforeEach
    void setUp() {
        testMatrix = new int[20][10];
        for (int i = 0; i < 10; i++) {
            testMatrix[19][i] = 1;
        }
    }

    @Test
    void getLinesRemoved() {
        clearRow = new ClearRow(0, testMatrix, 0);
        assertEquals(0, clearRow.getLinesRemoved());
    }

    @Test
    void getNewMatrix() {
        clearRow = new ClearRow(1, testMatrix, 100);
        int[][] result = clearRow.getNewMatrix();
        assertNotNull(result);
        assertEquals(20, result.length);
        assertEquals(10, result[0].length);
    }

    @Test
    void getScoreBonus() {
        clearRow = new ClearRow(0, testMatrix, 0);
        assertEquals(0, clearRow.getScoreBonus());
    }
}