package com.comp2042.controllers;

import com.comp2042.events.EventSource;
import com.comp2042.events.MoveEvent;
import com.comp2042.events.EventType;
import com.comp2042.models.DownData;
import com.comp2042.models.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private TestGuiController testGuiController;
    private GameController gameController;

    private MoveEvent leftEvent;
    private MoveEvent rightEvent;
    private MoveEvent downEvent;
    private MoveEvent rotateEvent;



    private static class TestGuiController extends GuiController {

        public int refreshBrickCallCount = 0;

        @Override
        public void setEventListener(com.comp2042.events.InputEventListener eventListener) {
        }

        @Override
        public void initGameView(int[][] boardMatrix, ViewData brick, double initialSpeed) {
        }

        @Override
        public void bindScore(javafx.beans.property.IntegerProperty scoreProperty) {
        }

        @Override
        public void setLevel(int level) {
        }

        @Override
        public void setLinesCleared(int lines) {
            // Do nothing
        }

        @Override
        public void refreshGameBackground(int[][] board) {
            // Do nothing
        }

        @Override
        public void refreshBrick(ViewData brick) {
            refreshBrickCallCount++;
        }

        @Override
        public void gameOver() {
            // Do nothing
        }

        @Override
        public void setGameSpeed(double newSpeed) {
            // Do nothing
        }
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        testGuiController = new TestGuiController();
        gameController = new GameController(testGuiController, 1);

        // Create keyboard event objects
        leftEvent = new MoveEvent(EventType.LEFT, EventSource.USER);
        rightEvent = new MoveEvent(EventType.RIGHT, EventSource.USER);
        downEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        rotateEvent = new MoveEvent(EventType.ROTATE, EventSource.USER);
    }

    @Test
    void onDownEvent() {
        DownData result = gameController.onDownEvent(downEvent);
        assertNotNull(result, "down key return viewdata");
        assertNotNull(result.getViewData(), "down key return valid in viewdata");
    }

    @Test
    void onLeftEvent() {
        ViewData result = gameController.onLeftEvent(leftEvent);
        assertNotNull(result, "left key return viewdata");
        assertNotNull(result.getBrickData(), "left key return valid in viewdata");
    }

    @Test
    void onRightEvent() {
        ViewData result = gameController.onRightEvent(rightEvent);
        assertNotNull(result, "right key return viewdata");
        assertNotNull(result.getBrickData(), "right key return valid in viewdata");
    }

    @Test
    void onRotateEvent() {
        ViewData result = gameController.onRotateEvent(rotateEvent);
        assertNotNull(result, "rotate key should return ViewData");
        assertNotNull(result.getBrickData(), "rotate key should return valid brick data");
    }

    @Test
    void createNewGame() {
        gameController.onLeftEvent(leftEvent);
        gameController.onRightEvent(rightEvent);
        assertDoesNotThrow(() -> gameController.createNewGame(),
                "new game should not throw exception");
        ViewData result = gameController.onLeftEvent(leftEvent);
        assertNotNull(result, "game should respond to keys after new game");
    }

    @Test
    void onHoldEvent() {
        int previousCount = testGuiController.refreshBrickCallCount;
        gameController.onHoldEvent();
        assertTrue(testGuiController.refreshBrickCallCount > previousCount);
    }
}