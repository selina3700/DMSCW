package com.comp2042.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    private ViewData viewData;
    private int[][] currentBrick;
    private int[][] nextBrick;
    private int[][] heldBrick;

    @BeforeEach
    void setUp() {
        currentBrick = new int[][]{{1, 1}, {1, 1}};
        nextBrick = new int[][]{{1, 1, 1, 1}};
        heldBrick = new int[][]{{1, 1, 1}, {0, 1, 0}};
    }

    @Test
    void getBrickData() {
        viewData = new ViewData(currentBrick, 5, 10, nextBrick, heldBrick, 5, 18);

        int[][] result = viewData.getBrickData();
        assertNotNull(result);
        assertSame(currentBrick, result);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
    }

    @Test
    void getxPosition() {
        viewData = new ViewData(currentBrick, 5, 10, nextBrick, heldBrick, 5, 18);

        assertEquals(5, viewData.getxPosition()); //position should be 5
    }

    @Test
    void getyPosition() {
        viewData = new ViewData(currentBrick, 5, 10, nextBrick, heldBrick, 5, 18);

        assertEquals(10, viewData.getyPosition()); //position should be 10
    }

    @Test
    void getNextBrickData() {
        viewData = new ViewData(currentBrick, 5, 10, nextBrick, heldBrick, 5, 18);

        int[][] result = viewData.getNextBrickData();
        assertNotNull(result);
        assertSame(nextBrick, result);
        assertEquals(1, result.length);
        assertEquals(4, result[0].length);
    }

    @Test
    void getHeldBrickData() {
        viewData = new ViewData(currentBrick, 5, 10, nextBrick, heldBrick, 5, 18);

        int[][] result = viewData.getHeldBrickData();
        assertNotNull(result);
        assertSame(heldBrick, result);
    }

    @Test
    void getGhostX() {
        viewData = new ViewData(currentBrick, 5, 10, nextBrick, heldBrick, 5, 18);
        assertEquals(5, viewData.getGhostX()); //ghostX should be 5
    }

    @Test
    void getGhostY() {
        viewData = new ViewData(currentBrick, 5, 10, nextBrick, heldBrick, 5, 18);
        assertEquals(18, viewData.getGhostY()); //ghostY should be 18
    }
}