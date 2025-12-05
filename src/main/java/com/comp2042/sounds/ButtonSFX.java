package com.comp2042.sounds;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

/**
 * Implements an interactive button with hover state visuals and SFX  upon click
 * <p>
 *     Handles image swapping for mouse interaction and manages
 *     the sound clip based on the SFX mute state.
 * </p>
 */
public class ButtonSFX extends ImageView {

    private AudioClip clickSound;
    private Image normalImg;
    private Image hoverImg;
    private boolean sfxMuted = false;

    /**
     * Constructs a new ButtonSFX component, loading normal and hover images, and the click sound.
     * <p>
     *     Initializes the mouse interaction handlers and sets the initial image.
     * </p>
     * @param imagePath The resource path for the default button image.
     * @param hoverPath The resource path for the hover/clicked button image.
     */
    public ButtonSFX(String imagePath, String hoverPath) {
            // Load Images
            this.normalImg = new Image(getClass().getResource(imagePath).toExternalForm());
            this.hoverImg = new Image(getClass().getResource(hoverPath).toExternalForm());

            // Setup Sound
            String soundPath = getClass().getResource("/sounds/buttonClick.mp3").toExternalForm();
            this.clickSound = new AudioClip(soundPath);

            if (normalImg != null) {
                setImage(normalImg);
            }
            setPreserveRatio(true);
            setStyle("-fx-cursor: hand;");

            setupInteractions();
    }

    /**
     * Sets an image as the default image
     * <p>
     *     If the mouse isn't hovering over the button, this image is displayed.
     * </p>
     * @param newImage The new {@code Image} for the normal state.
     */
    public void setNormalImage(Image newImage) {
        this.normalImg = newImage;
        if (!isHover()) {
            setImage(newImage);
        }
    }

    /**
     * Gets the current image to use as the default
     * @return The normal state {@code Image}.
     */
    public Image getNormalImage() {
        return this.normalImg;
    }

    /**
     * Gets the current image used for the hover state
     * @return The hover state{@code Image}
     */
    public Image getHoverImage() {
        return this.hoverImg;
    }

    /**
     * Gets the current image used for the hover state
     * @param newImage The hover state {@code Image}
     */
    public void setHoverImage(Image newImage) {
        this.hoverImg = newImage;
    }

    /**
     * Sets the mute state for the internal click sound effect
     * <p>
     *     The sound will only play upon click if this flag is set to {@code false}
     * </p>
     * @param muted {@code true} to mute the SFX, {@code false} to enable it
     */
    public void setSFXMuted(boolean muted) {
        this.sfxMuted = muted;

    }

    /**
     * Sets up the interaction handlers for mouse events:
     * <ul>
     *     <li>{@code onMouseEntered}: Swaps to the hover image.</li>
     *     <li>{@code onMouseExited}: Restores the normal image.</li>
     *     <li>{@code addEventFilter}: Plays the click sound if SFX isn't muted.</li>
     * </ul>
     */
    private void setupInteractions() {
        setOnMouseEntered(event -> {
            if (hoverImg != null) setImage(hoverImg);
        });
        setOnMouseExited(event -> {
            if (normalImg != null) setImage(normalImg);
        });

        addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            if (clickSound != null && !sfxMuted) {
                clickSound.play();
            }
        });
    }
}