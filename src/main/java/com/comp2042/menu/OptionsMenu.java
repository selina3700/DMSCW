package com.comp2042.menu;

import com.comp2042.controllers.GuiController;
import com.comp2042.sounds.ButtonSFX;
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

/**
 * Controller for the Options Menu screen
 * <p>
 * This class manages the user interface for adjusting audio settings (Music and SFX),
 * providing visual feedback for toggles, and handling navigation to the Controls Menu
 * or back to the parent screen (Main Menu, Pause Menu, or Game Over screen).
 *  </p>
 */
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

    /**
     * Sets the main application controller to enable delegation of game control actions
     * <p>
     *     This method also synchronizes the SFX mute state for all interactive buttons.
     * </p>
     * @param controller The main application {@code GuiController} instance.
     */
    public void setGuiController(GuiController controller) {
        this.guiController = controller;
        syncButtonStates();
    }

    /**
     * Ensures that the options menu can be opened from the game over screen
     * @param fromGameOver {@code true} if the menu was opened from the Game Over panel.
     */
    public void setOpenedFromGameOver(boolean fromGameOver) {
        this.openedFromGameOver = fromGameOver;
    }

    /**
     * Initializes the controller.
     * <p>
     *     Loads all the images like the logo and sounds, initializes
     *     the audio toggle buttons, and sets up the layout and action handlers for all
     *     wide navigation buttons.
     * </p>
     */
    @FXML
    private void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/Tetris_2.png").toExternalForm()));
        String soundPath = getClass().getResource("/sounds/buttonClick.mp3").toExternalForm();
        this.buttonClickSound = new AudioClip(soundPath);

        //Button Images
        volumeOnImg = loadImage("/images/Volume_Button.png");
        volumeOffImg = loadImage("/images/Mute_Button.png");
        sfxOnImg = loadImage("/images/SFX_Button.png");
        sfxOffImg = loadImage("/images/SFX_Mute.png");
        musicButtonRef = createIconButton("/images/Volume_Button.png", "/images/Mute_Button.png", 65, 65, this::toggleMusic);
        sfxButtonRef = createIconButton("/images/SFX_Button.png", "/images/SFX_Mute.png", 65, 65, this::toggleSFX);

        //Font
        Font.loadFont(getClass().getResourceAsStream("/font/PixelifySans.ttf"), 28);

        // Create Labels
        Label musicLabel = new Label("Music");
        musicLabel.getStyleClass().add("button-label");

        Label sfxLabel = new Label("SFX");
        sfxLabel.getStyleClass().add("button-label");

        //Group Music
        HBox musicGroup = new HBox(15);
        musicGroup.setAlignment(Pos.CENTER_LEFT);
        musicGroup.getChildren().addAll(musicButtonRef, musicLabel);

        //Group SFX
        HBox sfxGroup = new HBox(15);
        sfxGroup.setAlignment(Pos.CENTER_LEFT);
        sfxGroup.getChildren().addAll(sfxButtonRef, sfxLabel);

        iconButtonContainer.setAlignment(Pos.CENTER);
        iconButtonContainer.setSpacing(60);
        iconButtonContainer.getChildren().addAll(musicGroup, sfxGroup);

        //Wide buttons
        controlsButtonRef = createWideButton("/images/Controls_Button.png", "/images/Controls_After.png", 276, 57, this::goToControls);
        mainMenuButtonRef = createWideButton("/images/Main_Menu_2_Button.png", "/images/Main_Menu_2_After.png", 276, 57, this::goToMainMenu);
        backButtonRef = createWideButton("/images/Back_Button.png", "/images/Back_After.png", 276, 57, this::goBack);
        wideButtonContainer.getChildren().addAll(controlsButtonRef, mainMenuButtonRef, backButtonRef);
    }

    /**
     * Syncs the button states (SFX or BGM on/off with the current state held by the {@code GuiController}.
     */
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

    /**
     * Updates the SFX mute state for all the buttons on the options menu panel
     */
    private void updateAllButtonSFX() {
        boolean muted = !sfxOn;
        if (musicButtonRef != null) musicButtonRef.setSFXMuted(muted);
        if (sfxButtonRef != null) sfxButtonRef.setSFXMuted(muted);
        if (controlsButtonRef != null) controlsButtonRef.setSFXMuted(muted);
        if (mainMenuButtonRef != null) mainMenuButtonRef.setSFXMuted(muted);
        if (backButtonRef != null) backButtonRef.setSFXMuted(muted);
    }

    /**
     * Updates the visual appearance of the BGM toggle button based on current {@code musicOn} state.
     */
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

    /**
     * Updates the visual appearance of the BGM toggle button based on current {@code sfxOn} state.
     */
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

    /**
     * Factory method to create an icon-style {@code ButtonSFX} with a click action.
     * @param path Resource path for default image button
     * @param hoverPath Resource path for hover image button
     * @param width Width of the button
     * @param height Height of the button
     * @param action The {@code Runnable} action when the button is clicked
     * @return A configured {@code ButtonSFX} instance
     */
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

    /**
     * Factory method to create a wide-style {@code ButtonSFX} with a click action.
     * @param path Resource path for default image button
     * @param hoverPath Resource path for hover image button
     * @param width Width of the button
     * @param height Height of the button
     * @param action The {@code Runnable} action when the button is clicked
     * @return A configured {@code ButtonSFX} instance
     */
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

    /**
     * Toggles the BGM (on/off)
     * <p>
     *      This flips the internal {@code musicOn} state, delegates the mute change to the
     *      {@code GuiController}, and updates the visual appearance of the music button.
     * </p>
     */
    private void toggleMusic() {
        musicOn = !musicOn;
        if (guiController != null) {
            boolean mute = !musicOn;
            guiController.setMusicMuted(mute);
        }
        updateMusicButtonVisual();
    }

    /**
     * Toggles the SFX (on/off)
     * <p>
     *      This flips the internal {@code SFXOn} state, delegates the mute change to the
     *      {@code GuiController}, and updates SFX-related visuals and volumes
     * </p>
     */
    private void toggleSFX() {
        boolean wasSFXOn = sfxOn;
        sfxOn = !sfxOn;
        if (guiController != null) guiController.setSFXMute(!sfxOn);
        updateSFXButtonVisual();
        updateAllButtonSFX();
        if (!wasSFXOn && buttonClickSound != null) buttonClickSound.play();
        if (buttonClickSound != null) buttonClickSound.setVolume(sfxOn ? 1.0 : 0.0);
    }

    /**
     * Hides the Options Menu and displays the Controls Menu
     * <p>
     *     Includes logic to determine the parent to ensure seamless transition.
     * </p>
     */
    private void goToControls() {
        if (guiController == null) {
            return;
        }

        StackPane root = optionsMenuRoot;

        if (root == null) {
            javafx.scene.Parent current = wideButtonContainer.getParent();

            while (current != null) {
                if (current instanceof StackPane) {
                    root = (StackPane) current;
                    break;
                }
                current = current.getParent();
            }
        }

        if (root != null) {
            guiController.showControlsMenuFromMenu(root);
        }
    }

    /**
     * Hides the Options Menu and displays the Main Menu
     */
    private void goToMainMenu() {
        if (guiController != null) {
            guiController.hideOptionsMenu();
            guiController.showMainMenu();
        }
    }

    /**
     * Hides the options menu and navigates back to the previous screen.
     * <p>
     * The destination depends on the {@code openedFromGameOver} flag and the current state
     * of the {@code GuiController} (e.g., Pause Menu, Game Over Panel, or Main Menu).
     * </p>
     */
    private void goBack() {
        if (sfxOn && buttonClickSound != null) buttonClickSound.play();
        if (guiController != null) {
            guiController.hideOptionsMenu();

            if (openedFromGameOver) {
                guiController.getGameOverPanel().setVisible(true);
            } else if (!guiController.isMainMenuOpen()) {
                guiController.showPauseMenu();
            }
        }
    }

    /**
     * Loads an image from the resource path given
     * @param path The resource path
     * @return The loaded {@code Image} object, or {@code null}
     */
    private Image loadImage(String path) {
        try {
            if (getClass().getResource(path) == null) return null;
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            return null;
        }
    }
}