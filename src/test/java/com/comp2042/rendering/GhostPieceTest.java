package com.comp2042.rendering;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GhostPieceTest {

    private GhostPiece ghostPiece;
    private int[][] emptyBoard;
    private int[][] brickShape;

    @BeforeEach
    void setUp() {
        ghostPiece = new GhostPiece();
        emptyBoard = new int[25][10];
        brickShape = new int[][]{{1, 1}, {1, 1}};
    }

    @Test
    void calculateGhostYEmpty() {
        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY > currentY);
        assertTrue(ghostY >= 23);
    }

    @Test
    void calculateGhostYTop() {
        int currentX = 3;
        int currentY = 0;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY > currentY);
        assertTrue(ghostY >= 20);
    }

    @Test
    void calculateGhostYNearBottom() {
        // When brick is already near bottom, ghost should be just below
        int currentX = 3;
        int currentY = 22;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY >= currentY);
        assertTrue(ghostY <= 24);
    }

    @Test
    void calculateGhostYWithObstacle() {
        //Place obstacle at bottom
        for (int col = 0; col < 10; col++) {
            emptyBoard[24][col] = 1;
            emptyBoard[23][col] = 1;
        }

        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY > currentY);
        assertTrue(ghostY < 23);
    }

    @Test
    void calculateGhostY_OnTopOfObstacle() {
        for (int col = 0; col < 10; col++) {
            emptyBoard[15][col] = 1;
        }

        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY > currentY);
        assertTrue(ghostY <= 14);
    }

    @Test
    void calculateGhostYNullGameMatrix() {
        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(null, brickShape, currentX, currentY);

        assertEquals(currentY, ghostY);
    }

    @Test
    void calculateGhostY_NullBrickShape() {
        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, null, currentX, currentY);

        assertEquals(currentY, ghostY);
    }

    @Test
    void calculateGhostY_IBrick() {
        int[][] iBrick = {{1, 1, 1, 1}};
        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, iBrick, currentX, currentY);

        assertTrue(ghostY > currentY);
    }

    @Test
    void calculateGhostY_LeftEdge() {
        // Test brick at left edge
        int currentX = 0;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY > currentY);
    }

    @Test
    void calculateGhostY_RightEdge() {
        // Test brick at right edge
        int currentX = 8;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY > currentY);
    }

    @Test
    void getGhostX() {
        int currentX = 5;
        int currentY = 10;

        ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertEquals(currentX, ghostPiece.getGhostX());
    }

    @Test
    void getGhostY() {
        int currentX = 5;
        int currentY = 10;

        int calculatedY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertEquals(calculatedY, ghostPiece.getGhostY());
    }

    @Test
    void getGhostY_InitialValue() {
        assertEquals(0, ghostPiece.getGhostY(),
                "Initial ghost Y should be 0");
    }

    @Test
    void calculateGhostY_UpdatesPosition() {
        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertEquals(currentX, ghostPiece.getGhostX());
        assertEquals(ghostY, ghostPiece.getGhostY());
    }

    @Test
    void calculateGhostY_PartiallyFilledBoard() {
        emptyBoard[20][3] = 1;
        emptyBoard[20][4] = 1;
        emptyBoard[18][5] = 1;
        emptyBoard[22][2] = 1;

        int currentX = 3;
        int currentY = 5;

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertTrue(ghostY > currentY);
        assertTrue(ghostY <= 19);
    }

    @Test
    void calculateGhostY_MultipleCalculations() {
        //Test multiple calculations in sequence
        int[] positions = {5, 10, 15, 20};

        for (int pos : positions) {
            int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, 3, pos);
            assertTrue(ghostY >= pos);
        }
    }

    @Test
    void calculateGhostY_SameAsCurrentPosition() {
        // Place blocks directly below brick
        int currentX = 3;
        int currentY = 20;

        // Fill rows below
        for (int col = 0; col < 10; col++) {
            emptyBoard[21][col] = 1;
        }

        int ghostY = ghostPiece.calculateGhostY(emptyBoard, brickShape, currentX, currentY);

        assertEquals(currentY, ghostY);
    }

    @Test
    void calculateGhostY_DifferentBrickShapes() {
        int[][] tBrick = {{1, 1, 1}, {0, 1, 0}};

        int ghostY1 = ghostPiece.calculateGhostY(emptyBoard, brickShape, 3, 5);
        int ghostY2 = ghostPiece.calculateGhostY(emptyBoard, tBrick, 3, 5);

        assertTrue(ghostY1 > 5, "O-brick ghost should fall");
        assertTrue(ghostY2 > 5, "T-brick ghost should fall");
    }

    @Test
    void ghostPosition_PersistsAcrossCalls() {
        //Calculate ghost position
        ghostPiece.calculateGhostY(emptyBoard, brickShape, 5, 10);

        int x1 = ghostPiece.getGhostX();
        int y1 = ghostPiece.getGhostY();

        int x2 = ghostPiece.getGhostX();
        int y2 = ghostPiece.getGhostY();

        assertEquals(x1, x2, "Ghost X should persist");
        assertEquals(y1, y2, "Ghost Y should persist");
    }
}