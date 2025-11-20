package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip; // <-- New Import

public class OptionsMenu {

    private ButtonSFX musicButtonRef;
    private ButtonSFX sfxButtonRef;
    private ButtonSFX controlsButtonRef;
    private ButtonSFX mainMenuButtonRef;
    private ButtonSFX backButtonRef;

    private boolean musicOn = true;
    private boolean sfxOn = true;
    private GuiController guiController;

    private Image volumeOnImg;
    private Image volumeOffImg;
    private Image sfxOnImg;
    private Image sfxOffImg;

    private AudioClip buttonClickSound; // <-- New field for manual SFX playback

    @FXML private ImageView logoImage;
    @FXML private VBox wideButtonContainer;
    @FXML private HBox iconButtonContainer;

    public void setGuiController(GuiController controller) {
        this.guiController = controller;
        syncButtonStates();
    }

    @FXML
    private void initialize() {
        try {
            logoImage.setImage(new Image(getClass().getResource("/images/Tetris_2.png").toExternalForm()));

            // Attempt to load the button click sound manually
            try {
                String soundPath = getClass().getResource("/buttonClick.mp3").toExternalForm();
                this.buttonClickSound = new AudioClip(soundPath);
            } catch (Exception soundE) {
                System.out.println("Button click sound not found for manual use: " + soundE.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Logo not found: " + e.getMessage());
        }

        // Load all images upfront
        volumeOnImg = loadImage("/images/Volume_Button.png");
        volumeOffImg = loadImage("/images/Mute_Button.png");
        sfxOnImg = loadImage("/images/SFX_Button.png");
        sfxOffImg = loadImage("/images/SFX_Mute.png");

        // Create icon buttons (music and SFX)
        musicButtonRef = createIconButton("/images/Volume_Button.png", "/images/Mute_Button.png", 65, 65, this::toggleMusic);
        sfxButtonRef = createIconButton("/images/SFX_Button.png", "/images/SFX_Mute.png", 65, 65, this::toggleSFX);
        iconButtonContainer.getChildren().addAll(musicButtonRef, sfxButtonRef);

        // Create wide buttons
        controlsButtonRef = createWideButton("/images/Controls_Button.png", "/images/Controls_After.png", 276, 57, this::goToControls);
        mainMenuButtonRef = createWideButton("/images/Main_Menu_2_Button.png", "/images/Main_Menu_2_After.png", 276, 57, this::goToMainMenu);
        backButtonRef = createWideButton("/images/Back_Button.png", "/images/Back_After.png", 276, 57, this::goBack);
        wideButtonContainer.getChildren().addAll(controlsButtonRef, mainMenuButtonRef, backButtonRef);
    }

    public void syncButtonStates() {
        if (guiController != null) {
            musicOn = !guiController.isMusicMuted();
            sfxOn = !guiController.isSFXMuted();

            updateMusicButtonVisual();
            updateSFXButtonVisual();
            updateAllButtonSFX();

            // Set initial volume for manual playback clip
            if (buttonClickSound != null) {
                buttonClickSound.setVolume(sfxOn ? 1.0 : 0.0);
            }
        }
    }

    private void updateAllButtonSFX() {
        boolean muted = !sfxOn;
        if (musicButtonRef != null) musicButtonRef.setSFXMuted(muted);
        if (sfxButtonRef != null) sfxButtonRef.setSFXMuted(muted);
        if (controlsButtonRef != null) controlsButtonRef.setSFXMuted(muted);
        if (mainMenuButtonRef != null) mainMenuButtonRef.setSFXMuted(muted);
        if (backButtonRef != null) backButtonRef.setSFXMuted(muted);
    }

    private void updateMusicButtonVisual() {
        if (musicButtonRef != null && volumeOnImg != null && volumeOffImg != null) {
            if (musicOn) {
                musicButtonRef.setNormalImage(volumeOnImg);
                musicButtonRef.setHoverImage(volumeOffImg);
            } else {
                musicButtonRef.setNormalImage(volumeOffImg);
                musicButtonRef.setHoverImage(volumeOffImg);
            }
            musicButtonRef.setImage(musicButtonRef.getNormalImage());
        }
    }

    private void updateSFXButtonVisual() {
        if (sfxButtonRef != null && sfxOnImg != null && sfxOffImg != null) {
            if (sfxOn) {
                sfxButtonRef.setNormalImage(sfxOnImg);
                sfxButtonRef.setHoverImage(sfxOffImg);
            } else {
                sfxButtonRef.setNormalImage(sfxOffImg);
                sfxButtonRef.setHoverImage(sfxOffImg);
            }
            sfxButtonRef.setImage(sfxButtonRef.getNormalImage());
        }
    }

    private ButtonSFX createIconButton(String path, String hoverPath, double width, double height, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);
        btn.setFitWidth(width);
        btn.setFitHeight(height);

        // FIX: Use addEventHandler to append action, preserving SFX handler
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());

        // FIX: Sync SFX mute with gui controller state upon creation
        if (this.guiController != null) {
            btn.setSFXMuted(this.guiController.isSFXMuted());
        } else {
            btn.setSFXMuted(!this.sfxOn);
        }

        return btn;
    }

    private ButtonSFX createWideButton(String path, String hoverPath, double width, double height, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);
        btn.setFitWidth(width);
        btn.setFitHeight(height);

        // FIX: Use addEventHandler to append action, preserving SFX handler
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());

        // FIX: Sync SFX mute with gui controller state upon creation
        if (this.guiController != null) {
            btn.setSFXMuted(this.guiController.isSFXMuted());
        } else {
            btn.setSFXMuted(!this.sfxOn);
        }

        return btn;
    }


    private void toggleMusic() {
        musicOn = !musicOn;

        if (guiController != null) {
            guiController.setMusicMute(!musicOn);
        }
        updateMusicButtonVisual();
    }

    private void toggleSFX() {
        boolean wasSFXOn = sfxOn; // Store state BEFORE toggle
        sfxOn = !sfxOn;

        if (guiController != null) {
            guiController.setSFXMute(!sfxOn);
        }

        updateSFXButtonVisual();
        updateAllButtonSFX();

        // FIX 1: Manually play the sound only if SFX was just turned ON
        if (!wasSFXOn && buttonClickSound != null) {
            buttonClickSound.play();
        }

        // Update volume for manual playback clip
        if (buttonClickSound != null) {
            buttonClickSound.setVolume(sfxOn ? 1.0 : 0.0);
        }
    }

    private void goToControls() {
        if (guiController != null) {
            guiController.hideOptionsMenu();
            // TODO: Implement showControlsMenu() in GuiController
            guiController.showMainMenu();
        }
    }

    private void goToMainMenu() {
        if (guiController != null) {
            guiController.hideOptionsMenu();
            guiController.showMainMenu();
        }
    }

    private void goBack() {
        // FIX 2: Manually play SFX right before the menu transition if SFX is currently ON
        if (sfxOn && buttonClickSound != null) {
            buttonClickSound.play();
        }

        if (guiController != null) {
            guiController.hideOptionsMenu();
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
            System.err.println("Error loading image " + path + ": " + e.getMessage());
            return null;
        }
    }
}