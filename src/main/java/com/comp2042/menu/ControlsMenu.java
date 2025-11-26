package com.comp2042.menu;

import com.comp2042.controllers.GuiController;
import com.comp2042.sounds.ButtonSFX;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ControlsMenu {

    @FXML private StackPane rootPane;  // Changed from optionsMenuroot

    private Pane parentMenu;
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
        if (rootPane != null && rootPane.getParent() instanceof javafx.scene.layout.Pane) {
            ((javafx.scene.layout.Pane) rootPane.getParent()).getChildren().remove(rootPane);
        }
        guiController.setCurrentControlsMenu(null);

        if (parentMenu != null) {
            parentMenu.setVisible(true);
        }
    }

    public void setParentMenu(Pane parentMenu) {
        this.parentMenu = parentMenu;
    }
}