package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PauseMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;
    @FXML private ImageView resumeButton, restartButton, quitButton, mainMenuButton, optionsButton;

    private GuiController guiController;

    public void setGuiController(GuiController controller) {
        this.guiController = controller;
    }

    @FXML
    private void initialize() {
        // Load logo
        setImage(logoImage, "/Tetris.png");

        // Setup buttons
        setupButton(resumeButton, "/Resume_Button.png", "/Resume_After.png", this::resumeGame);
        setupButton(restartButton, "/Restart_Button.png", "/Restart_After.png", this::restartGame);
        setupButton(quitButton, "/Quit Button.png", "/Quit_After.png", this::quitGame);
        setupButton(mainMenuButton, "/Main_Menu_Button.png", "/Main_Menu_After.png", this::mainMenu);
        setupButton(optionsButton, "/Options Button.png", "/Options_After_Button.png", this::optionsMenu);
    }

    // Helper method to safely load images
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
        Image img = loadImage(path);
        if (img != null) view.setImage(img);
    }

    private void setupButton(ImageView view, String normalPath, String hoverPath, Runnable action) {
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

    private void mainMenu(){ //Temporary
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.getGamePanel().getScene().getWindow().hide();
        }
    }

    private void optionsMenu(){ //Temporary
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.getGamePanel().getScene().getWindow().hide();
        }
    }
}