package com.comp2042.menu;

import com.comp2042.controllers.GuiController;
import com.comp2042.sounds.ButtonSFX;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Controls Menu Overlay
 * <p>
 *     Manages the setup of the close button and ensures that the parent menu (either main menu or
 *     options menu) is restored after the controls menu is hidden.
 * </p>
 */
public class ControlsMenu {

    @FXML private StackPane rootPane;

    private Pane parentMenu;
    private GuiController guiController;
    private ButtonSFX closeButton;

    /**
     * Links to the main application
     * <p>
     *     Used to delegate actions
     * </p>
     * @param controller The main application {@code GuiController} instance.
     */
    public void setGuiController(GuiController controller) {
        this.guiController = controller;

        if (closeButton != null && guiController != null) {
            closeButton.setSFXMuted(guiController.isSFXMuted());
        }
    }

    /**
     * Initializes the controller after the FXML elements are loaded.
     * <p>
     *     Sets up the visual properties and click handler for the close button
     *     and adds it to the root pane.
     * </p>
     */
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

    /**
     * Sets the pane that launched this controls menu.
     * <p>
     *     This reference is used to make the parent menu visible again once the controls menu is closed.
     * </p>
     * @param parentMenu The parent menu pane.
     */
    public void setParentMenu(Pane parentMenu) {
        this.parentMenu = parentMenu;
    }

    /**
     * Menu closing logic.
     */
    private void closeMenu() {
        if (rootPane != null && rootPane.getParent() instanceof javafx.scene.layout.Pane) {
            ((javafx.scene.layout.Pane) rootPane.getParent()).getChildren().remove(rootPane);
        }
        guiController.setCurrentControlsMenu(null);

        if (parentMenu != null) {
            parentMenu.setVisible(true);
        }
    }
}