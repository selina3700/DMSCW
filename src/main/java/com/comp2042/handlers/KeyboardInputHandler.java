package com.comp2042.handlers;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.controllers.GuiController;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * Handles all keyboard input for the Tetris game.
 * Manages movement controls, rotation, hard drop, hold, and new game shortcuts.
 */
public class KeyboardInputHandler {

    private final InputEventListener eventListener;
    private final GuiController guiController;
    private final BooleanProperty isPause;
    private final BooleanProperty isGameOver;

    /**
     * Constructor for KeyboardInputHandler
     *
     * @param eventListener The event listener for game logic
     * @param guiController The GUI controller for visual updates
     * @param isPause Property tracking pause state
     * @param isGameOver Property tracking game over state
     */
    public KeyboardInputHandler(InputEventListener eventListener,
                                GuiController guiController,
                                BooleanProperty isPause,
                                BooleanProperty isGameOver) {
        this.eventListener = eventListener;
        this.guiController = guiController;
        this.isPause = isPause;
        this.isGameOver = isGameOver;
    }

    /**
     * Sets up keyboard event handling for the game panel
     *
     * @param gamePanel The grid panel to attach keyboard events to
     */
    public void setupKeyEvents(GridPane gamePanel) {
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                // Only allow movement if game is not paused or over
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    handleGameplayKeys(keyEvent);
                }

                // Global keys (work even when paused)
                handleGlobalKeys(keyEvent);
            }
        });
    }

    /**
     * Handles keys that only work during active gameplay
     */
    private void handleGameplayKeys(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.LEFT) {
            guiController.refreshBrick(
                    eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))
            );
            keyEvent.consume();
        }
        else if (code == KeyCode.RIGHT) {
            guiController.refreshBrick(
                    eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))
            );
            keyEvent.consume();
        }
        else if (code == KeyCode.UP) {
            guiController.refreshBrick(
                    eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))
            );
            keyEvent.consume();
        }
        else if (code == KeyCode.DOWN) {
            guiController.moveDownPublic(new MoveEvent(EventType.DOWN, EventSource.USER));
            keyEvent.consume();
        }
        else if (code == KeyCode.SPACE) {
            guiController.hardDropPublic();
            keyEvent.consume();
        }
        else if (code == KeyCode.H) {
            if (eventListener != null) {
                eventListener.onHoldEvent();
            }
            keyEvent.consume();
        }
    }

    /**
     * Handles keys that work regardless of game state
     */
    private void handleGlobalKeys(KeyEvent keyEvent) {
        // Press "N" to start a new game
        if (keyEvent.getCode() == KeyCode.N) {
            guiController.newGame(null);
            keyEvent.consume();
        }
    }
}