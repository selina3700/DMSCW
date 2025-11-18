package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;
    @FXML private VBox buttonContainer; // This links to the FXML VBox

    private Stage primaryStage;
    private GuiController guiController;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    @FXML
    private void initialize() {
        //Logo
        try {
            logoImage.setImage(new Image(getClass().getResource("/images/Tetris.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Logo not found");
        }

        //Buttons
        ButtonSFX startBtn = createButton("/images/Start_Button.png", "/images/Start_After.png", this::startGame);
        ButtonSFX optionsBtn = createButton("/images/Options Button.png", "/images/Options_After_Button.png", this::optionsMenu);
        ButtonSFX quitBtn = createButton("/images/Quit Button.png", "/images/Quit_After.png", this::quitGame);

        buttonContainer.getChildren().addAll(startBtn, optionsBtn, quitBtn);
    }

    private ButtonSFX createButton(String path, String hoverPath, Runnable action) {
        ButtonSFX btn = new ButtonSFX(path, hoverPath);

        //Fixed Size
        btn.setFitWidth(276);
        btn.setFitHeight(57);

        btn.setOnMouseClicked(e -> action.run());
        return btn;
    }

    private void startGame() {
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