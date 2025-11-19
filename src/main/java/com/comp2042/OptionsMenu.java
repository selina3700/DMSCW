package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OptionsMenu {

    @FXML private ImageView musicButton;
    @FXML private ImageView sfxButton;
    @FXML private ImageView controlsButton;
    @FXML private ImageView mainMenuButton;
    @FXML private ImageView backButton;
    @FXML private ImageView logoImage;

    // Persistent toggle states
    private boolean musicOn = true;
    private boolean sfxOn = true;
    private GuiController guiController;

    public void setGuiController(GuiController controller) {
        this.guiController = controller;
    }

    @FXML
    private void initialize() {
        // Logo
        setImage(logoImage, "/images/Tetris.png");

        // Music toggle button
        setupButton(
                musicButton,
                "/images/Volume_Button.png",
                "/images/Mute_Button.png",
                this::toggleMusic
        );

        // SFX toggle button
        setupButton(
                sfxButton,
                "/images/SFX_Button.png",
                "/images/SFX_Mute.png",
                this::toggleSFX
        );

        // Controls
        setupButton(
                controlsButton,
                "/images/Controls_Button.png",
                "/images/Controls_After.png",
                this::goToControls
        );

        // Main Menu
        setupButton(
                mainMenuButton,
                "/images/Main_Menu_Button.png",
                "/images/Main_Menu_After.png",
                this::goToMainMenu
        );

        // Back
        setupButton(
                backButton,
                "/images/Back_Button.png",
                "/images/Back_Button_Hover.png",
                this::goBack
        );
    }

    private void toggleMusic() {
        musicOn = !musicOn;

        // UPDATE THE ACTUAL AUDIO
        if (guiController != null) {
            guiController.setMusicMute(!musicOn); // If musicOn is true, mute is false
        }

        musicButton.setImage(loadImage(
                musicOn ? "/images/Volume_Button.png" : "/images/Mute_Button.png"
        ));
    }

    private void toggleSFX() {
        sfxOn = !sfxOn;

        // UPDATE THE ACTUAL AUDIO
        if (guiController != null) {
            guiController.setSFXMute(!sfxOn);
        }

        sfxButton.setImage(loadImage(
                sfxOn ? "/images/SFX_Button.png" : "/images/SFX_Off_Button.png"
        ));
    }

    private void goToControls() {
        if (guiController != null) {
            guiController.hidePauseMenu();
            guiController.showMainMenu();
        }
    }

    private void goToMainMenu() {
        if (guiController != null) {
            guiController.hidePauseMenu();
            guiController.showMainMenu();
        }
    }

    private void goBack() {
        if (guiController != null) {
            guiController.showPauseMenu();
        }
    }

    private Image loadImage(String path) {
        try {
            if (getClass().getResource(path) == null) {
                System.out.println("Resource not found: " + path);
                return null;
            }
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setImage(ImageView view, String path) {
        if (view == null) return;
        Image img = loadImage(path);
        if (img != null) view.setImage(img);
    }

    private void setupButton(ImageView view, String normalPath, String hoverPath, Runnable action) {
        if (view == null) return;

        Image normalImg = loadImage(normalPath);
        Image hoverImg = loadImage(hoverPath);
        if (normalImg == null || hoverImg == null) return;

        view.setImage(normalImg);
        view.setStyle("-fx-cursor: hand;");

        view.setOnMouseEntered(e -> view.setImage(hoverImg));
        view.setOnMouseExited(e -> view.setImage(normalImg));
        view.setOnMousePressed(e -> view.setImage(hoverImg));
        view.setOnMouseReleased(e -> view.setImage(normalImg));
        view.setOnMouseClicked(e -> action.run());
    }
}
