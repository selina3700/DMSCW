package com.comp2042.menu;

import com.comp2042.controllers.GuiController;
import com.comp2042.Main;
import com.comp2042.models.LevelSelectorButton;
import com.comp2042.sounds.ButtonSFX;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Controller for the Main Menu screen
 * <p>
 *      Initializes the main menu visuals, level selection component and delegates
 *      user action to the core application logic via {@code Main} and {@code GuiController}.
 * </p>
 */
public class MainMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;
    @FXML private VBox buttonContainer;

    private Stage primaryStage;
    private GuiController guiController;
    private LevelSelectorButton levelSelector;
    private int selectedLevel = 1;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Sets the main application controller to enable delegation of game control actions
     * <p>
     *     This method also synchronizes the SFX mute state for all interactive buttons.
     * </p>
     * @param guiController The main {@code GuiController} instance.
     */
    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
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
        logoImage.setImage(new Image(getClass().getResource("/images/Tetris.png").toExternalForm()));
        createButtons();
    }

    private void createButtons() {
        ButtonSFX startBtn = createButton("/images/Start_Button.png", "/images/Start_After.png", this::startGame);

        // Create level selector button
        levelSelector = new LevelSelectorButton();
        levelSelector.setOnLevelChange(level -> selectedLevel = level);
        if (guiController != null) {
            levelSelector.setSFXMuted(guiController.isSFXMuted());
        }

        ButtonSFX optionsBtn = createButton("/images/Options Button.png", "/images/Options_After_Button.png", this::optionsMenu);
        ButtonSFX quitBtn = createButton("/images/Quit Button.png", "/images/Quit_After.png", this::quitGame);

        buttonContainer.getChildren().addAll(startBtn, levelSelector, optionsBtn, quitBtn);
    }

    private void syncButtonStates() {
        if (guiController == null || buttonContainer == null) return;
        boolean isMuted = guiController.isSFXMuted();
        for (javafx.scene.Node node : buttonContainer.getChildren()) {
            if (node instanceof ButtonSFX) {
                ((ButtonSFX) node).setSFXMuted(isMuted);
            } else if (node instanceof LevelSelectorButton) {
                ((LevelSelectorButton) node).setSFXMuted(isMuted);
            }
        }
    }

    private ButtonSFX createButton(String path, String hoverPath, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);
        btn.setFitWidth(276);
        btn.setFitHeight(57);
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        if (guiController != null) {
            btn.setSFXMuted(guiController.isSFXMuted());
        }
        return btn;
    }

    /**
     * Initiates the game start sequence
     * <p>
     *      This stops the current menu BGM, calls {@code Main.startGame} with the currently selected level,
     *      and starts the game BGM on the newly created game controller instance.
     * </p>
     */
    private void startGame() {
        try {
            // start game
            Main.startGame(selectedLevel);

            // start new BGM from the game controller
            GuiController newController = Main.getGuiController();
            if (newController != null) {
                newController.startBgm();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the main application window
     */
    private void quitGame() {
        if (primaryStage != null) primaryStage.close();
    }

    /**
     * Delegates control to the {@code GuiController} to show the options menu overlay.
     * The options menu is shown, attached to the current main menu {@code root} pane.
     */
    private void optionsMenu() {
        if (guiController != null) {
            guiController.showOptionsMenu(root);
        }
    }

    /**
     * Returns the starting level currently selected by the user via the {@code LevelSelectorButton}
     * @return The selected starting game level
     */
    public int getSelectedLevel() {
        return selectedLevel;
    }
}