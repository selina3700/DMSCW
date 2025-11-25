package com.comp2042;

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

    public void playBgm() {
        stopCurrent();
        if (!muted) {
            currentPlayer = bgmPlayer;
            currentPlayer.play();
        }
    }

    public void playGameOverMusic() {
        stopCurrent();
        if (!muted) {
            currentPlayer = gameOverPlayer;
            currentPlayer.play();
        }
    }

    public void stopCurrent() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    public void restart() {
        playBgm();
    }

    public void pause() {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
    }

    public void setMuted(boolean mute) {
        this.muted = mute;
        if (currentPlayer != null) {
            currentPlayer.setMute(mute);
        }
        if (bgmPlayer != null) bgmPlayer.setMute(mute);
        if (gameOverPlayer != null) gameOverPlayer.setMute(mute);
    }

}
