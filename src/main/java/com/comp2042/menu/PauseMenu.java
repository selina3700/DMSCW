package com.comp2042.menu;

import com.comp2042.controllers.GuiController;
import com.comp2042.sounds.ButtonSFX;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox; // Import HBox

public class PauseMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;

    @FXML private VBox wideButtonContainer;
    @FXML private HBox iconButtonContainer;

    private GuiController guiController;

    public void setGuiController(GuiController controller) {
        this.guiController = controller;
        // FIX: Sync button states immediately when controller is set
        syncButtonStates();
    }

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

    // NEW: Sync all button SFX mute states
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

    private ButtonSFX createIconButton(String path, String hoverPath, double width, double height, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);

        btn.setFitWidth(width);
        btn.setFitHeight(height);

        // FIX: Use addEventHandler instead of setOnMouseClicked to preserve the SFX handler
        btn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> action.run());

        // Sync SFX state
        if (guiController != null) {
            btn.setSFXMuted(guiController.isSFXMuted());
        }

        return btn;
    }

    private void resumeGame() {
        if (guiController != null) {
            guiController.resumeGame();
        }
    }

    private void restartGame() {
        if (guiController != null) {
            guiController.newGame(null);
            guiController.hidePauseMenu();
        }
    }

    private void quitGame() {
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.getGamePanel().getScene().getWindow().hide();
        }
    }

    private void mainMenu() {
        if (guiController != null) {
            guiController.hidePauseMenu();
            guiController.showMainMenu();
        }
    }

    private void optionsMenu(){
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.hidePauseMenu();
            guiController.showOptionsMenu();
        }
    }
}