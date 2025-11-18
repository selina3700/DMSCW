package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox; // Import HBox

public class PauseMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;

    // Two separate containers from FXML
    @FXML private VBox wideButtonContainer;
    @FXML private HBox iconButtonContainer;

    private GuiController guiController;

    public void setGuiController(GuiController controller) {
        this.guiController = controller;
    }

    @FXML
    private void initialize() {

        try {
            logoImage.setImage(new Image(getClass().getResource("/images/Tetris.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Logo not found");
        }

        ButtonSFX optionsBtn = createButton("/images/Options Button.png", "/images/Options_After_Button.png", 276, 57, this::optionsMenu);
        ButtonSFX quitBtn = createButton("/images/Quit Button.png", "/images/Quit_After.png", 276, 57, this::quitGame);

        wideButtonContainer.getChildren().addAll(optionsBtn, quitBtn);

        ButtonSFX homeBtn = createButton("/images/Main_Menu_Button.png", "/images/Main_Menu_After.png", 65, 65, this::mainMenu);
        ButtonSFX restartBtn = createButton("/images/Restart_Button.png", "/images/Restart_After.png", 65, 65, this::restartGame);
        ButtonSFX resumeBtn = createButton("/images/Resume_Button.png", "/images/Resume_After.png", 65, 65, this::resumeGame);

        iconButtonContainer.getChildren().addAll(homeBtn, restartBtn, resumeBtn);
    }

    private ButtonSFX createButton(String path, String hoverPath, double width, double height, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);

        btn.setFitWidth(width);
        btn.setFitHeight(height);

        btn.setOnMouseClicked(e -> action.run());
        return btn;
    }

    private void resumeGame() {
        if (guiController != null) {
            guiController.resumeGame();
            guiController.hidePauseMenu();
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
            guiController.getGamePanel().getScene().getWindow().hide();
        }
    }
}