package com.comp2042.menu;

import com.comp2042.controllers.GuiController;
import com.comp2042.sounds.ButtonSFX;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox; // Import HBox

/**
 * Controller for the Pause Menu screen
 * <p>
 *     Initializes the pause menu visuals when the game is paused.
 * </p>
 */
public class PauseMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;

    @FXML private VBox wideButtonContainer;
    @FXML private HBox iconButtonContainer;

    private GuiController guiController;

    /**
     * Sets the main application controller to enable delegation of game control actions
     * <p>
     *      This method also synchronizes the SFX mute state for all interactive buttons.
     * </p>
     * @param controller The main {@code GuiController} instance.
     */
    public void setGuiController(GuiController controller) {
        this.guiController = controller;
        syncButtonStates();
    }

    /**
     * Initializes the controller after the FXML elements are loaded.
     * <p>
     *     Loads the main logo image and calls {@code createButtons()} to set up
     *     menu actions.
     * </p>
     */
    @FXML
    private void initialize() {

        try {
            logoImage.setImage(new Image(getClass().getResource("/images/Tetris.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Logo not found");
        }

        ButtonSFX optionsBtn = createIconButton("/images/Options Button.png", "/images/Options_After_Button.png", 276, 57, this::optionsMenu);
        ButtonSFX quitBtn = createIconButton("/images/Quit Button.png", "/images/Quit_After.png", 276, 57, this::quitGame);

        wideButtonContainer.getChildren().addAll(optionsBtn, quitBtn);

        ButtonSFX homeBtn = createIconButton("/images/Main_Menu_Button.png", "/images/Main_Menu_After.png", 65, 65, this::mainMenu);
        ButtonSFX restartBtn = createIconButton("/images/Restart_Button.png", "/images/Restart_After.png", 65, 65, this::restartGame);
        ButtonSFX resumeBtn = createIconButton("/images/Resume_Button.png", "/images/Resume_After.png", 65, 65, this::resumeGame);

        iconButtonContainer.getChildren().addAll(homeBtn, restartBtn, resumeBtn);
    }

    /**
     * Synchronizes the sound effect (SFX) mute state for all interactive buttons
     * on the pause menu with the current state held by the {@code GuiController}.
     */
    private void syncButtonStates() {
        if (guiController == null) return;
        boolean isMuted = guiController.isSFXMuted();

        if (wideButtonContainer != null) {
            wideButtonContainer.getChildren().forEach(node -> {
                if (node instanceof ButtonSFX) ((ButtonSFX) node).setSFXMuted(isMuted);
            });
        }
        if (iconButtonContainer != null) {
            iconButtonContainer.getChildren().forEach(node -> {
                if (node instanceof ButtonSFX) ((ButtonSFX) node).setSFXMuted(isMuted);
            });
        }
    }

    /**
     * Factory method to create an interactive {@code ButtonSFX} with a click action.
     *
     * @param path The resource path for the default button image.
     * @param hoverPath The resource path for the image when the button is hovered/clicked.
     * @param width The desired width of the button image.
     * @param height The desired height of the button image.
     * @param action The {@code Runnable} action to execute on button click.
     * @return A configured {@code ButtonSFX} instance.
     */
    private ButtonSFX createIconButton(String path, String hoverPath, double width, double height, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);

        btn.setFitWidth(width);
        btn.setFitHeight(height);

        btn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> action.run());

        // Sync SFX state
        if (guiController != null) {
            btn.setSFXMuted(guiController.isSFXMuted());
        }

        return btn;
    }

    /**
     * Delegates control to the {@code GuiController} to resume the game.
     * This typically involves hiding the pause menu and resuming the game loop timeline.
     */
    private void resumeGame() {
        if (guiController != null) {
            guiController.resumeGame();
        }
    }

    /**
     * Delegates control to the {@code GuiController} to start a new game.
     * The pause menu is hidden before the new game is initialized.
     */
    private void restartGame() {
        if (guiController != null) {
            guiController.newGame(null);
            guiController.hidePauseMenu();
        }
    }

    /**
     * Closes the main application window
     */
    private void quitGame() {
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.getGamePanel().getScene().getWindow().hide();
        }
    }

    /**
     * Delegates control to the {@code GuiController} to show the main menu.
     * The pause menu is hidden before transitioning to the main menu view.
     */
    private void mainMenu() {
        if (guiController != null) {
            guiController.hidePauseMenu();
            guiController.showMainMenu();
        }
    }

    /**
     * Delegates control to the {@code GuiController} to show the options menu overlay.
     * The options menu is shown, attached to the current main menu {@code root} pane.
     */
    private void optionsMenu(){
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.hidePauseMenu();
            guiController.showOptionsMenu();
        }
    }
}