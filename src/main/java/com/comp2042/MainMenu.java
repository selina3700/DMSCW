package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class MainMenu {

    @FXML private StackPane root;
    @FXML private ImageView logoImage;
    @FXML private VBox buttonContainer;

    private Stage primaryStage;
    private GuiController guiController;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
        syncButtonStates();
    }

    @FXML
    private void initialize() {
        try {
            logoImage.setImage(new Image(getClass().getResource("/images/Tetris.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Logo not found");
        }
        createButtons();
    }

    private void createButtons() {
        ButtonSFX startBtn = createButton("/images/Start_Button.png", "/images/Start_After.png", this::startGame);
        ButtonSFX optionsBtn = createButton("/images/Options Button.png", "/images/Options_After_Button.png", this::optionsMenu);
        ButtonSFX quitBtn = createButton("/images/Quit Button.png", "/images/Quit_After.png", this::quitGame);

        buttonContainer.getChildren().addAll(startBtn, optionsBtn, quitBtn);
    }

    private void syncButtonStates() {
        if (guiController == null || buttonContainer == null) return;
        boolean isMuted = guiController.isSFXMuted();
        for (javafx.scene.Node node : buttonContainer.getChildren()) {
            if (node instanceof ButtonSFX) {
                ((ButtonSFX) node).setSFXMuted(isMuted);
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

    private void startGame() {
        Main.startGame();
    }

    private void quitGame() {
        if (primaryStage != null) primaryStage.close();
    }

    private void optionsMenu() {
        if (guiController != null) {
            guiController.showOptionsMenu(root);
        }
    }
}