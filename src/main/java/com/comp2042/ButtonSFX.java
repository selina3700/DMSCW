package com.comp2042;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

public class ButtonSFX extends ImageView {

    private AudioClip clickSound;
    private Image normalImg;
    private Image hoverImg;
    private boolean sfxMuted = false;

    public ButtonSFX(String imagePath, String hoverPath) {
        try {
            // Load Images
            this.normalImg = new Image(getClass().getResource(imagePath).toExternalForm());
            this.hoverImg = new Image(getClass().getResource(hoverPath).toExternalForm());

            // Setup Sound
            String soundPath = getClass().getResource("/buttonClick.mp3").toExternalForm();
            this.clickSound = new AudioClip(soundPath);

        } catch (Exception e) {
            System.err.println("Error loading resources for ButtonSFX: " + e.getMessage());
            this.normalImg = null;
            this.hoverImg = null;
            this.clickSound = null;
        }

        if (normalImg != null) {
            setImage(normalImg);
        }
        setPreserveRatio(true);
        setStyle("-fx-cursor: hand;");

        setupInteractions();
    }

    public void setNormalImage(Image newImage) {
        this.normalImg = newImage;
        if (!isHover()) {
            setImage(newImage);
        }
    }

    public Image getNormalImage() {
        return this.normalImg;
    }

    public Image getHoverImage() {
        return this.hoverImg;
    }

    public void setHoverImage(Image newImage) {
        this.hoverImg = newImage;
    }

    public void setSFXMuted(boolean muted) {
        this.sfxMuted = muted;
        // Don't stop the sound here, just update the flag
    }

    private void setupInteractions() {
        setOnMouseEntered(event -> {
            if (hoverImg != null) setImage(hoverImg);
        });
        setOnMouseExited(event -> {
            if (normalImg != null) setImage(normalImg);
        });

        // CRITICAL FIX: Use addEventFilter instead of addEventHandler
        // This runs during the CAPTURE phase, before any other handlers
        addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            if (clickSound != null && !sfxMuted) {
                clickSound.play();
            }
        });
    }
}