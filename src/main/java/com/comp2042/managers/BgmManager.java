package com.comp2042.managers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manages the BGM throughout the gameplay
 * <p>
 *     This manager is responsible for controlling 2 separate tracks (main game BGM and game over)
 *     and delegating the playback state (play, pause, mute).
 * </p>
 *
 */
public class BgmManager {

    private MediaPlayer currentPlayer;
    private MediaPlayer bgmPlayer;
    private MediaPlayer gameOverPlayer;
    private boolean muted = false;
    private String bgmPath;

    public BgmManager(String bgmPath, String gameOverPath) {
        this.bgmPath = bgmPath;

        bgmPlayer = new MediaPlayer(new Media(getClass().getResource(bgmPath).toExternalForm()));
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        gameOverPlayer = new MediaPlayer(new Media(getClass().getResource(gameOverPath).toExternalForm()));
        gameOverPlayer.setCycleCount(1);
    }

    /**
     * Plays BGM from the beginning
     */
    public void playBgm() {
        restart();
    }

    /**
     * Resumes BGM from where it was paused
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
     * Restarts BGM from the beginning
     */
    public void restart() {
        stopCurrent();

        bgmPlayer = new MediaPlayer(new Media(getClass().getResource(bgmPath).toExternalForm()));
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        bgmPlayer.setMute(muted);

        currentPlayer = bgmPlayer;
        currentPlayer.play();
    }


    /**
     * Pauses the current music
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