package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;
    @FXML private ImageView startButton, quitButton, optionsButton;

    private Stage primaryStage;
    private GuiController guiController;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void initialize() {
        // Load logo
        setImage(logoImage, "/Tetris.png");

        // Setup buttons
        if (startButton != null) {
            setupButton(startButton, "/Start_Button.png", "/Start_After.png", this::startGame);
        }
        if (optionsButton != null) {
            setupButton(optionsButton, "/Options Button.png", "/Options_After_Button.png", this::optionsMenu);
        }
        if (quitButton != null) {
            setupButton(quitButton, "/Quit Button.png", "/Quit_After.png", this::quitGame);
        }
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
        if (view == null) return;
        Image img = loadImage(path);
        if (img != null) view.setImage(img);
    }

    private void setupButton(ImageView view, String normalPath, String hoverPath, Runnable action) {
        if (view == null) {
            System.out.println("ERROR: Cannot setup button - view is null");
            return;
        }

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

    private void startGame() {
        // Call the static method in Main to start the game
        Main.startGame();
    }

    private void quitGame() {
        if (primaryStage != null) {
            primaryStage.close();
        }
    }

    private void optionsMenu() {
        if (guiController != null) {
            guiController.hideMainMenu();
            guiController.showOptionsMenu();
        }
    }
}