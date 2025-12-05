package com.comp2042.menu;

import com.comp2042.controllers.GuiController;
import com.comp2042.sounds.ButtonSFX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * Game Over overlay that's displayed when the game ends
 * <p>
 *     Handles the dynamic loading of its FXML layout, the graphics and
 *     provides buttons to restart the game, return to main menu, access options
 *     or quit the application.
 * </p>
 */
public class GameOverPanel extends StackPane {

    @FXML private ImageView gameOverImage;
    @FXML private HBox iconButtonContainer;
    @FXML private VBox quitButtonContainer;

    private GuiController guiController;

    /**
     * Constructs the panel by loading its FXML layout and initializing the visual hierarchy.
     */
    public GameOverPanel() {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameOver.fxml"));
        loader.setController(this);

        Node loadedContent;
        try {
            loadedContent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GameOverPanel FXML", e);
        }

        this.getChildren().add(loadedContent);
        setAlignment(javafx.geometry.Pos.CENTER);

        if (loadedContent instanceof StackPane) {
            StackPane sp = (StackPane) loadedContent;
            sp.prefWidthProperty().bind(this.widthProperty());
            sp.prefHeightProperty().bind(this.heightProperty());
        }
    }

    /**
     * Sets the main application controller to enable delegation of game control actions.
     * <p>
     *     This ensures that the SFX mute state of all buttons on the panel is synchronized
     *     with the {@code GuiController}'s current state.
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
     *     Loads the "Game Over" image, creates the interactive {@code ButtonSFX}
     *     instances for restart, menu, and quit actions and adds them to their respective containers.
     * </p>
     */
    @FXML
    private void initialize() {
        try {
            gameOverImage.setImage(new Image(getClass().getResource("/images/Game_Over.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Game Over image not found: " + e.getMessage());
        }

        ButtonSFX homeBtn = createIconButton(
                "/images/Main_Menu_Button.png",
                "/images/Main_Menu_After.png",
                65, 65,
                this::mainMenu
        );

        ButtonSFX restartBtn = createIconButton(
                "/images/Restart_Button.png",
                "/images/Restart_After.png",
                65, 65,
                this::restartGame
        );

        ButtonSFX settingsBtn = createIconButton(
                "/images/Settings_Button.png",
                "/images/Settings_After.png",
                65, 65,
                this::optionsMenu
        );

        iconButtonContainer.getChildren().addAll(homeBtn, restartBtn, settingsBtn);

        ButtonSFX quitBtn = createIconButton(
                "/images/Quit Button.png",
                "/images/Quit_After.png",
                276, 57,
                this::quitGame
        );

        quitButtonContainer.getChildren().add(quitBtn);
    }

    /**
     * Syncs SFX mute state for all buttons on the panel with the state managed
     * by the {@code GuiController}.
     */
    private void syncButtonStates() {
        if (guiController == null) return;
        boolean isMuted = guiController.isSFXMuted();

        if (iconButtonContainer != null) {
            iconButtonContainer.getChildren().forEach(node -> {
                if (node instanceof ButtonSFX) ((ButtonSFX) node).setSFXMuted(isMuted);
            });
        }
        if (quitButtonContainer != null) {
            quitButtonContainer.getChildren().forEach(node -> {
                if (node instanceof ButtonSFX) ((ButtonSFX) node).setSFXMuted(isMuted);
            });
        }
    }

    /**
     * Create customized buttons with SFX
     * @param path Resource path for button image
     * @param hoverPath Resource path when button is hovers/clicked
     * @param width Width of button
     * @param height Height of button
     * @param action The {@code Runnable} action to execute on click
     * @return A configured {@code ButtonSFX} instance
     */
    private ButtonSFX createIconButton(String path, String hoverPath, double width, double height, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);
        btn.setFitWidth(width);
        btn.setFitHeight(height);
        btn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> action.run());

        if (guiController != null) {
            btn.setSFXMuted(guiController.isSFXMuted());
        }

        return btn;
    }

    /**
     * Hides the panel and delegates control to the {@code GuiController} to start a new game.
     */
    private void restartGame() {
        if (guiController != null) {
            this.setVisible(false);
            guiController.newGame(null);
        }
    }

    /**
     * Hides the panel and delegates control to the {@code GuiController}
     */
    private void mainMenu() {
        if (guiController != null) {
            this.setVisible(false);
            guiController.showMainMenu();
        }
    }

    /**
     * Hides the panel and delegates control to the {@code GuiController} to show the options menu,
     * signaling that the menu was launched from the Game Over screen.
     */
    private void optionsMenu() {
        if (guiController != null) {
            this.setVisible(false);
            guiController.showOptionsMenuFromGameOver();
        }
    }

    /**
     * Retrieves the scene window and closes the application.
     */
    private void quitGame() {
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.getGamePanel().getScene().getWindow().hide();
        } else {
            System.exit(0);
        }
    }
}