package com.comp2042.sounds;

import javafx.scene.media.AudioClip;

/**
 * Handles sound effects for line clearing events in Tetris.
 * Plays sounds when one or more lines are cleared.
 */
public class ClearLineSFX {

    private AudioClip clearSoundPlayer;
    private boolean isMuted;
    private double defaultVolume = 0.3;

    /**
     * Creates a new ClearLineSFX handler
     */
    public ClearLineSFX() {
        this.isMuted = false;
        initializeSoundEffects();
    }

    /**
     * Initializes the sound effect for clearing lines
     */
    private void initializeSoundEffects() {
        try {
            String soundPath = getClass().getResource("/sounds/linecleared.mp3").toExternalForm();
            clearSoundPlayer = new AudioClip(soundPath);
            clearSoundPlayer.setVolume(defaultVolume);
        } catch (Exception e) {
            System.out.println("Clear sound not found: " + e.getMessage());
            System.out.println("Expected path: src/main/resources/sounds/linecleared.mp3");
        }
    }

    /**
     * Plays the line clear sound effect
     */
    public void playClearSound() {
        if (clearSoundPlayer != null && !isMuted) {
            clearSoundPlayer.play();
        }
    }

    /**
     * Plays the line clear sound effect based on number of lines cleared
     * Can be extended in the future to play different sounds for different combos
     * @param linesCleared Number of lines cleared (1-4)
     */
    public void playClearSound(int linesCleared) {
        if (clearSoundPlayer != null && !isMuted && linesCleared > 0) {
            // Future enhancement: could play different sounds for singles, doubles, triples, tetris
            clearSoundPlayer.play();
        }
    }

    /**
     * Sets whether the sound should be muted
     * @param muted true to mute, false to unmute
     */
    public void setMuted(boolean muted) {
        this.isMuted = muted;
        if (clearSoundPlayer != null) {
            clearSoundPlayer.setVolume(muted ? 0.0 : defaultVolume);
        }
    }

    /**
     * Sets the volume of the clear sound
     * @param volume Volume level (0.0 to 1.0)
     */
    public void setVolume(double volume) {
        this.defaultVolume = Math.max(0.0, Math.min(1.0, volume));
        if (clearSoundPlayer != null && !isMuted) {
            clearSoundPlayer.setVolume(defaultVolume);
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
        return clearSoundPlayer != null;
    }
}