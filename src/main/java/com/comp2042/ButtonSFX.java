package com.comp2042;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

public class ButtonSFX extends ImageView {

    private static final String SOUND_PATH = "/buttonClick.mp3";
    private static final AudioClip CLICK_SOUND = new AudioClip(ButtonSFX.class.getResource(SOUND_PATH).toExternalForm());

    private final Image normalImg;
    private final Image hoverImg;

    public ButtonSFX(String imagePath, String hoverPath) {
        // Load Images
        this.normalImg = new Image(getClass().getResource(imagePath).toExternalForm());
        this.hoverImg = new Image(getClass().getResource(hoverPath).toExternalForm());

        // Set Initial Look
        setImage(normalImg);
        setPreserveRatio(true);
        setStyle("-fx-cursor: hand;");

        setupInteractions();
    }

    private void setupInteractions() {
        setOnMouseEntered(event -> setImage(hoverImg));
        setOnMouseExited(event -> setImage(normalImg));
        setOnMousePressed(event -> setImage(hoverImg));
        setOnMouseReleased(event -> setImage(normalImg));

        addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            CLICK_SOUND.play();
        });
    }
}