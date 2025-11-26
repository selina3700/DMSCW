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

public class GameOverPanel extends StackPane {

    @FXML private ImageView gameOverImage;
    @FXML private HBox iconButtonContainer;
    @FXML private VBox quitButtonContainer;

    private GuiController guiController;

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

    public void setGuiController(GuiController controller) {
        this.guiController = controller;
        syncButtonStates();
    }

    @FXML
    private void initialize() {
        // Load the Game Over image
        try {
            gameOverImage.setImage(new Image(getClass().getResource("/images/Game_Over.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Game Over image not found: " + e.getMessage());
        }

        // Create icon buttons (Home, Restart, Settings)
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

        // Create Quit button
        ButtonSFX quitBtn = createIconButton(
                "/images/Quit Button.png",
                "/images/Quit_After.png",
                276, 57,
                this::quitGame
        );

        quitButtonContainer.getChildren().add(quitBtn);
    }

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

    private void restartGame() {
        if (guiController != null) {
            this.setVisible(false);
            // CHANGED: Use newGame() which handles everything including pause button
            guiController.newGame(null);
        }
    }

    private void mainMenu() {
        if (guiController != null) {
            this.setVisible(false);
            // CHANGED: showMainMenu will be handled by GameStateManager
            guiController.showMainMenu();
        }
    }

    private void optionsMenu() {
        if (guiController != null) {
            // Hide game over temporarily while showing options
            this.setVisible(false);
            guiController.showOptionsMenuFromGameOver();
        }
    }

    private void quitGame() {
        if (guiController != null && guiController.getGamePanel() != null) {
            guiController.getGamePanel().getScene().getWindow().hide();
        } else {
            System.exit(0);
        }
    }
}