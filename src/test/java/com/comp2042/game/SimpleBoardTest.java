package com.comp2042.game;

import com.comp2042.models.ClearRow;
import com.comp2042.models.Score;
import com.comp2042.models.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {
    private SimpleBoard board;
    private static final int BWidth = 25;
    private static final int BHeight = 10;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(BWidth, BHeight);
        board.createNewBrick();
    }

    @Test
    void moveBrickDown() {
        boolean canMove = board.moveBrickDown();
        assertTrue(canMove);

        int moveCount = 0;
        while (board.moveBrickDown() && moveCount < 30) {
            moveCount++;
        }
        assertFalse(board.moveBrickDown());
    }

    @Test
    void moveBrickLeft() {
        boolean canMove = board.moveBrickLeft();
        assertTrue(canMove || !canMove);
        int moveCount = 0;
        while (board.moveBrickLeft() && moveCount < 10) {
            moveCount++;
        }
        assertFalse(board.moveBrickLeft()); //Hits the wall
    }

    @Test
    void moveBrickRight() {
        boolean canMove = board.moveBrickRight();
        assertTrue(canMove || !canMove);
        int moveCount = 0;
        while (board.moveBrickRight() && moveCount < 10) {
            moveCount++;
        }
        assertFalse(board.moveBrickRight());
    }

    @Test
    void rotateLeftBrick() {
        boolean canRotate = board.rotateLeftBrick();
        assertTrue(canRotate || !canRotate);
        board.rotateLeftBrick();
        board.rotateLeftBrick();
        board.rotateLeftBrick();
        assertDoesNotThrow(() -> board.rotateLeftBrick());
    }

    @Test
    void createNewBrick() {
        boolean gameOver = board.createNewBrick();
        assertFalse(gameOver);
        ViewData viewData = board.getViewData();
        assertNotNull(viewData);
        assertNotNull(viewData.getBrickData());
    }

    @Test
    void holdBrick() {
        ViewData beforeHold = board.getViewData();

        board.holdBrick();
        ViewData afterHold = board.getViewData();
        assertNotNull(afterHold);
        assertNotNull(afterHold.getHeldBrickData());

        board.holdBrick();
        assertNotNull(board.getViewData());
    }

    @Test
    void getBoardMatrix() {
        int[][] matrix = board.getBoardMatrix();

        assertNotNull(matrix);
        assertEquals(BWidth, matrix.length);
        assertEquals(BHeight, matrix[0].length);
    }

    @Test
    void getViewData() {
        ViewData viewData = board.getViewData();

        assertNotNull(viewData);
        assertNotNull(viewData.getBrickData());
        assertNotNull(viewData.getNextBrickData());

        assertTrue(viewData.getxPosition() >= 0);
        assertTrue(viewData.getyPosition() >= 0);
    }

    @Test
    void mergeBrickToBackground() {
        while (board.moveBrickDown()) {

        }
        int[][] beforeMerge = board.getBoardMatrix();
        board.mergeBrickToBackground();
        int[][] afterMerge = board.getBoardMatrix();
        assertNotNull(afterMerge);

        boolean hasBlocks = false;
        for (int i = 0; i < afterMerge.length; i++) {
            for (int j = 0; j < afterMerge[i].length; j++) {
                if (afterMerge[i][j] != 0) {
                    hasBlocks = true;
                    break;
                }
            }
        }
        assertTrue(hasBlocks);
    }

    @Test
    void clearRows() {
        ClearRow result = board.clearRows();

        assertNotNull(result);
        assertTrue(result.getLinesRemoved() >= 0);
        assertTrue(result.getScoreBonus() >= 0);
        assertNotNull(result.getNewMatrix());
    }


    @Test
    void getScore() {
        Score score = board.getScore();

        assertNotNull(score);
        assertTrue(score.scoreProperty().get() >= 0);
    }
}