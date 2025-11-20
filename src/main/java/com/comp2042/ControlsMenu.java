package com.comp2042;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ControlsMenu {

    @FXML private StackPane rootPane;

    private GuiController guiController;
    private ButtonSFX closeButton;

    public void setGuiController(GuiController controller) {
        this.guiController = controller;

        if (closeButton != null && guiController != null) {
            closeButton.setSFXMuted(guiController.isSFXMuted());
        }
    }

    @FXML
    private void initialize() {
        closeButton = new ButtonSFX("/images/Close_Button.png", "/images/Close_After.png");

        closeButton.setFitWidth(50);
        closeButton.setFitHeight(50);

        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);

        StackPane.setMargin(closeButton, new Insets(5, 5, 0, 0));

        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> closeMenu());

        rootPane.getChildren().add(closeButton);
    }

    private void closeMenu() {
        if (guiController != null) {
            guiController.showOptionsMenu();
        }
    }
}