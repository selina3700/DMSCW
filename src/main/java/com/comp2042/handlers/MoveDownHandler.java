package com.comp2042.handlers;

import com.comp2042.models.DownData;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.models.NotificationPanel;
import com.comp2042.controllers.GuiController;
import com.comp2042.sounds.BrickLandSFX;
import com.comp2042.sounds.ClearLineSFX;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Group;

/**
 * Handles the logic for moving bricks down in the Tetris game.
 * Manages sound effects, score notifications, and brick position updates when moving down.
 */
public record MoveDownHandler(InputEventListener eventListener, GuiController guiController, BooleanProperty isPause,
                              ClearLineSFX clearLineSFX, BrickLandSFX brickLandSFX, Group groupNotification) {

    /**
     * Constructor for MoveDownHandler
     *
     * @param eventListener     The event listener for game logic
     * @param guiController     The GUI controller for visual updates
     * @param isPause           Property tracking pause state
     * @param clearLineSFX      Sound effect for clearing lines
     * @param brickLandSFX      Sound effect for brick landing
     * @param groupNotification UI group for displaying notifications
     */
    public MoveDownHandler {
    }

    /**
     * Executes the move down action for the current brick.
     * Handles row clearing, sound effects, and visual updates.
     *
     * @param event The move event containing movement information
     */
    public void execute(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                playClearSound(downData.getClearRow().getLinesRemoved());
                showScoreNotification(downData.getClearRow().getScoreBonus());
            }

            if (!downData.isMoved() && brickLandSFX != null) {
                brickLandSFX.playLandSound();
            }

            guiController.refreshBrick(downData.getViewData());
        }

        //Keep the game window focused
        guiController.requestGamePanelFocus();
    }

    /**
     * Plays the SFX when lines are cleared
     */
    private void playClearSound(int linesRemoved) {
        if (clearLineSFX != null) {
            clearLineSFX.playClearSound(linesRemoved);
        }
    }

    /**
     * Displays a floating score notification
     */
    private void showScoreNotification(int scoreBonus) {
        NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
    }
}