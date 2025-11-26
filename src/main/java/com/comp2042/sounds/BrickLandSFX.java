package com.comp2042.sounds;

import javafx.scene.media.AudioClip;

/**
 * Handles sound effects for brick landing events in Tetris.
 * Plays appropriate sounds when bricks lock into place.
 */
public class BrickLandSFX {

    private AudioClip landSoundPlayer;
    private boolean isMuted;
    private double defaultVolume = 0.25;

    /**
     * Creates a new BrickLandSFX handler
     */
    public BrickLandSFX() {
        this.isMuted = false;
        initializeSoundEffects();
    }

    /**
     * Initializes the sound effect for brick landing
     */
    private void initializeSoundEffects() {
            // You can change this path to your actual brick land sound file
            String soundPath = getClass().getResource("/sounds/brickland.mp3").toExternalForm();
            landSoundPlayer = new AudioClip(soundPath);
            landSoundPlayer.setVolume(1);
    }

    /**
     * Plays the brick landing sound effect
     */
    public void playLandSound() {
        if (landSoundPlayer != null && !isMuted) {
            landSoundPlayer.play();
        }
    }

    /**
     * Sets whether the sound should be muted
     * @param muted true to mute, false to unmute
     */
    public void setMuted(boolean muted) {
        this.isMuted = muted;
        if (landSoundPlayer != null) {
            landSoundPlayer.setVolume(muted ? 0.0 : 1);
        }
    }

    /**
     * Sets the volume of the land sound
     * @param volume Volume level (0.0 to 1.0)
     */
    public void setVolume(double volume) {
        this.defaultVolume = Math.max(0.0, Math.min(1.0, volume));
        if (landSoundPlayer != null && !isMuted) {
            landSoundPlayer.setVolume(defaultVolume);
        }
    }

    /**
     * Gets the current mute state
     * @return true if muted, false otherwise
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Checks if the sound is loaded and ready
     * @return true if sound is available, false otherwise
     */
    public boolean isSoundAvailable() {
        return landSoundPlayer != null;
    }
}