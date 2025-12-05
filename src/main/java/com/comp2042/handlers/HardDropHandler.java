package com.comp2042.handlers;

import com.comp2042.controllers.GuiController;
import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.models.DownData;
import com.comp2042.models.NotificationPanel;
import com.comp2042.sounds.BrickLandSFX;
import javafx.scene.media.AudioClip;

/**
 * Handles the hard drop functionality for Tetris.
 * Drops the current brick instantly to its lowest possible position.
 */
public class HardDropHandler {

    private final InputEventListener eventListener;
    private final GuiController guiController;
    private AudioClip clearSoundPlayer;
    private BrickLandSFX brickLandSFX;

    /**
     * Creates a new HardDropHandler
     * @param eventListener The game's event listener
     * @param guiController Visual updates
     */
    public HardDropHandler(InputEventListener eventListener, GuiController guiController) {
        this.eventListener = eventListener;
        this.guiController = guiController;
        this.brickLandSFX = new BrickLandSFX();
        initializeSoundEffects();
    }

    /**
     * Initializes the sound effect for clearing lines
     */
    private void initializeSoundEffects() {
        try {
            String soundPath = getClass().getResource("/sounds/linecleared.mp3").toExternalForm();
            clearSoundPlayer = new AudioClip(soundPath);
            clearSoundPlayer.setVolume(0.3);
        } catch (Exception e) {
        }
    }

    /**
     * Instantly drops the brick to the bottom of the board
     * @param isPaused Check if game is currently paused
     * @param isGameOver Check if the game is over
     */
    public void execute(boolean isPaused, boolean isGameOver) {
        if (isPaused || isGameOver) {
            return;
        }

        boolean canMoveDown = true;
        MoveEvent downEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        DownData downData = null;

        // Keep moving down until the brick reaches bottom
        while (canMoveDown) {
            downData = eventListener.onDownEvent(downEvent);
            if (!downData.isMoved()) {
                canMoveDown = false;
            }
        }

        if (downData != null) {
            guiController.refreshBrick(downData.getViewData());

            if (brickLandSFX != null) {
                brickLandSFX.playLandSound();
            }
            handleRowClearing(downData);
        }
    }

    /**
     * Handles visual and audio feedback when rows are cleared
     * @param downData The data from the down movement
     */
    private void handleRowClearing(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            if (clearSoundPlayer != null && !guiController.isSFXMuted()) {
                clearSoundPlayer.play();
            }

            NotificationPanel notificationPanel = new NotificationPanel(
                    "+" + downData.getClearRow().getScoreBonus()
            );
            guiController.showNotification(notificationPanel);
        }
    }

    /**
     * Updates the sound effect mute status
     * @param muted Whether sound should be muted
     */
    public void setSFXMuted(boolean muted) {
        if (clearSoundPlayer != null) {
            clearSoundPlayer.setVolume(muted ? 0.0 : 0.3);
        }
        if (brickLandSFX != null) {
            brickLandSFX.setMuted(muted);
        }
    }
}