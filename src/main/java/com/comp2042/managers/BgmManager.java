package com.comp2042.managers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BgmManager {

    private MediaPlayer currentPlayer;
    private MediaPlayer bgmPlayer;
    private MediaPlayer gameOverPlayer;
    private boolean muted = false;

    public BgmManager(String bgmPath, String gameOverPath) {
        bgmPlayer = new MediaPlayer(new Media(getClass().getResource(bgmPath).toExternalForm()));
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        gameOverPlayer = new MediaPlayer(new Media(getClass().getResource(gameOverPath).toExternalForm()));
        gameOverPlayer.setCycleCount(1); // play once
    }

    /**
     * Plays BGM from the beginning (used for new game)
     */
    public void playBgm() {
        stopCurrent();
        if (!muted) {
            currentPlayer = bgmPlayer;
            currentPlayer.play();
        }
    }

    /**
     * Resumes BGM from where it was paused (used for unpause)
     */
    public void resume() {
        if (currentPlayer != null && !muted) {
            currentPlayer.play();
        }
    }

    /**
     * Plays game over music
     */
    public void playGameOverMusic() {
        stopCurrent();
        if (!muted) {
            currentPlayer = gameOverPlayer;
            currentPlayer.play();
        }
    }

    /**
     * Stops the current music completely
     */
    public void stopCurrent() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    /**
     * Restarts BGM from the beginning (used for new game)
     */
    public void restart() {
        playBgm();
    }

    /**
     * Pauses the current music (preserves position)
     */
    public void pause() {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
    }

    /**
     * Sets mute state for all players
     */
    public void setMuted(boolean mute) {
        this.muted = mute;
        if (currentPlayer != null) {
            currentPlayer.setMute(mute);
        }
        if (bgmPlayer != null) bgmPlayer.setMute(mute);
        if (gameOverPlayer != null) gameOverPlayer.setMute(mute);
    }
}