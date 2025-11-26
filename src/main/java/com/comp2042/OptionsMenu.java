package com.comp2042;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

public class OptionsMenu {

    private ButtonSFX musicButtonRef;
    private ButtonSFX sfxButtonRef;
    private ButtonSFX controlsButtonRef;
    private ButtonSFX mainMenuButtonRef;
    private ButtonSFX backButtonRef;
    private boolean openedFromGameOver = false;


    private boolean musicOn = true;
    private boolean sfxOn = true;
    private GuiController guiController;

    private Image volumeOnImg;
    private Image volumeOffImg;
    private Image sfxOnImg;
    private Image sfxOffImg;

    private AudioClip buttonClickSound;

    @FXML private ImageView logoImage;
    @FXML private VBox wideButtonContainer;
    @FXML private HBox iconButtonContainer;
    @FXML private StackPane optionsMenuRoot;

    public void setGuiController(GuiController controller) {
        System.out.println("=== OptionsMenu.setGuiController() called ===");
        System.out.println("controller is null? " + (controller == null));

        this.guiController = controller;
        syncButtonStates();
    }

    public void setOpenedFromGameOver(boolean fromGameOver) {
        this.openedFromGameOver = fromGameOver;
    }


    @FXML
    private void initialize() {
        try {
            logoImage.setImage(new Image(getClass().getResource("/images/Tetris_2.png").toExternalForm()));

            try {
                String soundPath = getClass().getResource("/buttonClick.mp3").toExternalForm();
                this.buttonClickSound = new AudioClip(soundPath);
            } catch (Exception soundE) {
                System.out.println("Button click sound not found: " + soundE.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Logo not found: " + e.getMessage());
        }

        volumeOnImg = loadImage("/images/Volume_Button.png");
        volumeOffImg = loadImage("/images/Mute_Button.png");
        sfxOnImg = loadImage("/images/SFX_Button.png");
        sfxOffImg = loadImage("/images/SFX_Mute.png");

        musicButtonRef = createIconButton("/images/Volume_Button.png", "/images/Mute_Button.png", 65, 65, this::toggleMusic);
        sfxButtonRef = createIconButton("/images/SFX_Button.png", "/images/SFX_Mute.png", 65, 65, this::toggleSFX);

        // ---------------------------------------------------------
        // UPDATED: Using CSS for styling
        // ---------------------------------------------------------

        // We still need to load the font once so JavaFX knows it exists for the CSS to use it
        Font.loadFont(getClass().getResourceAsStream("/font/PixelifySans.ttf"), 28);

        // Create Labels and apply CSS class
        Label musicLabel = new Label("Music");
        musicLabel.getStyleClass().add("button-label"); // <--- CSS Class

        Label sfxLabel = new Label("SFX");
        sfxLabel.getStyleClass().add("button-label");   // <--- CSS Class

        // Group Music
        HBox musicGroup = new HBox(15);
        musicGroup.setAlignment(Pos.CENTER_LEFT);
        musicGroup.getChildren().addAll(musicButtonRef, musicLabel);

        // Group SFX
        HBox sfxGroup = new HBox(15);
        sfxGroup.setAlignment(Pos.CENTER_LEFT);
        sfxGroup.getChildren().addAll(sfxButtonRef, sfxLabel);

        // Main Container
        iconButtonContainer.setAlignment(Pos.CENTER);
        iconButtonContainer.setSpacing(60);
        iconButtonContainer.getChildren().addAll(musicGroup, sfxGroup);

        // ---------------------------------------------------------

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
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
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
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        if (this.guiController != null) {
            btn.setSFXMuted(this.guiController.isSFXMuted());
        } else {
            btn.setSFXMuted(!this.sfxOn);
        }
        return btn;
    }

    private void toggleMusic() {
        System.out.println("=== toggleMusic() called ===");
        System.out.println("musicOn BEFORE: " + musicOn);

        // Flip the state
        musicOn = !musicOn;

        System.out.println("musicOn AFTER: " + musicOn);
        System.out.println("guiController is null? " + (guiController == null));

        if (guiController != null) {
            boolean mute = !musicOn; // when musicOn = false, mute = true
            System.out.println("Calling guiController.setMusicMuted(" + mute + ")");
            guiController.setMusicMuted(mute);
        } else {
            System.out.println("ERROR: guiController is NULL - cannot toggle music!");
        }

        updateMusicButtonVisual();
    }

    private void toggleSFX() {
        boolean wasSFXOn = sfxOn;
        sfxOn = !sfxOn;
        if (guiController != null) guiController.setSFXMute(!sfxOn);
        updateSFXButtonVisual();
        updateAllButtonSFX();
        if (!wasSFXOn && buttonClickSound != null) buttonClickSound.play();
        if (buttonClickSound != null) buttonClickSound.setVolume(sfxOn ? 1.0 : 0.0);
    }

    private void goToControls() {
        System.out.println("=== goToControls() called ===");

        if (guiController == null) {
            System.out.println("ERROR: guiController is NULL");
            return;
        }

        // Try to find the StackPane root
        StackPane root = optionsMenuRoot;

        // If optionsMenuRoot is null, traverse up to find it
        if (root == null) {
            System.out.println("optionsMenuRoot is null, searching for StackPane...");
            javafx.scene.Parent current = wideButtonContainer.getParent();

            while (current != null) {
                System.out.println("Checking parent: " + current.getClass().getSimpleName());
                if (current instanceof StackPane) {
                    root = (StackPane) current;
                    System.out.println("Found StackPane!");
                    break;
                }
                current = current.getParent();
            }
        }

        if (root != null) {
            System.out.println("Opening controls menu with root: " + root.getClass().getSimpleName());
            guiController.showControlsMenuFromMenu(root);
        } else {
            System.out.println("ERROR: Could not find StackPane root for Controls Menu");
        }
    }

    private void goToMainMenu() {
        if (guiController != null) {
            guiController.hideOptionsMenu();
            guiController.showMainMenu();
        }
    }

    private void goBack() {
        if (sfxOn && buttonClickSound != null) buttonClickSound.play();
        if (guiController != null) {
            guiController.hideOptionsMenu();

            if (openedFromGameOver) {
                // Go back to game over screen - it's already visible, just show it
                guiController.getGameOverPanel().setVisible(true);
            } else if (!guiController.isMainMenuOpen()) {
                guiController.showPauseMenu();
            }
            // If main menu is open, just closing options is enough
        }
    }

    private Image loadImage(String path) {
        try {
            if (getClass().getResource(path) == null) return null;
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            return null;
        }
    }
}