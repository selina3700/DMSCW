package com.comp2042.managers;

import com.comp2042.controllers.GuiController;
import com.comp2042.menu.ControlsMenu;
import com.comp2042.menu.OptionsMenu;
import com.comp2042.menu.PauseMenu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Manages all menu overlays and transitions in the game.
 * <p>
 *     This class handles loading and displaying of all primary game menus such as
 *     pause menu, main menu, options menu, and controls menu.
 * </p>
 */
public class MenuManager {

    private final GuiController guiController;

    // Menu references
    private StackPane pauseMenu;
    private StackPane mainMenu;
    private Pane currentOptionsMenu;
    private Pane currentControlsMenu;

    // State tracking
    private boolean isMainMenuOpen = false;

    /**
     * Constructor for MenuManager
     *
     * @param guiController The GUI controller to interact with
     */
    public MenuManager(GuiController guiController) {
        this.guiController = guiController;
    }


    /**
     * Shows the pause menu overlay
     */
    public void showPauseMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pauseMenu.fxml"));
            pauseMenu = loader.load();

            PauseMenu controller = loader.getController();
            controller.setGuiController(guiController);

            Pane root = (Pane) guiController.getGamePanel().getScene().getRoot();

            pauseMenu.setPrefSize(root.getWidth(), root.getHeight());
            pauseMenu.prefWidthProperty().bind(root.widthProperty());
            pauseMenu.prefHeightProperty().bind(root.heightProperty());

            root.getChildren().add(pauseMenu);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides and removes the pause menu
     */
    public void hidePauseMenu() {
        if (pauseMenu != null && guiController.getGamePanel().getScene() != null) {
            Pane root = (Pane) guiController.getGamePanel().getScene().getRoot();
            root.getChildren().remove(pauseMenu);
            pauseMenu = null;
        }
    }

    /**
     * Shows the main menu by replacing the scene root
     */
    public void showMainMenu() {
        try {
            hidePauseMenu();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            StackPane mainMenuPane = loader.load();

            com.comp2042.menu.MainMenu mainMenuController = loader.getController();
            mainMenuController.setPrimaryStage((Stage) guiController.getGamePanel().getScene().getWindow());
            mainMenuController.setGuiController(guiController);

            guiController.getGamePanel().getScene().setRoot(mainMenuPane);

            mainMenu = mainMenuPane;
            isMainMenuOpen = true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading main menu: " + e.getMessage());
        }
    }

    /**
     * Hides the main menu and clears its reference
     */
    public void hideMainMenu() {
        mainMenu = null;
        isMainMenuOpen = false;
    }

    /**
     * Check if main menu is currently open
     */
    public boolean isMainMenuOpen() {
        return isMainMenuOpen;
    }

    /**
     * Shows the options menu overlay (auto-detects parent)
     */
    public void showOptionsMenu() {
        showOptionsMenu(null);
    }

    /**
     * Shows the options menu overlay with specific parent root
     *
     * @param specificRoot The specific root pane to attach to (null for auto-detect)
     */
    public void showOptionsMenu(Pane specificRoot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/optionsMenu.fxml"));
            Pane optionsPane = loader.load();

            OptionsMenu controller = loader.getController();
            controller.setGuiController(guiController);

            javafx.scene.Parent rootParent = specificRoot;

            if (rootParent == null) {
                rootParent = findActiveRoot();
            }

            if (rootParent == null) {
                System.out.println("ERROR: Could not find root to attach Options Menu.");
                return;
            }

            // Attach the menu
            Pane overlayParent = ensurePaneRoot(rootParent);

            // Resize logic
            optionsPane.setPrefSize(overlayParent.getWidth(), overlayParent.getHeight());
            optionsPane.prefWidthProperty().bind(overlayParent.widthProperty());
            optionsPane.prefHeightProperty().bind(overlayParent.heightProperty());

            overlayParent.getChildren().add(optionsPane);
            currentOptionsMenu = optionsPane;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows options menu specifically from game over screen
     */
    public void showOptionsMenuFromGameOver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/optionsMenu.fxml"));
            Pane optionsPane = loader.load();

            OptionsMenu controller = loader.getController();
            controller.setGuiController(guiController);
            controller.setOpenedFromGameOver(true);

            Pane root = (Pane) guiController.getGamePanel().getScene().getRoot();

            optionsPane.setPrefSize(root.getWidth(), root.getHeight());
            optionsPane.prefWidthProperty().bind(root.widthProperty());
            optionsPane.prefHeightProperty().bind(root.heightProperty());

            root.getChildren().add(optionsPane);
            currentOptionsMenu = optionsPane;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides and removes the options menu
     */
    public void hideOptionsMenu() {
        if (currentOptionsMenu != null && currentOptionsMenu.getParent() instanceof Pane) {
            ((Pane) currentOptionsMenu.getParent()).getChildren().remove(currentOptionsMenu);
            currentOptionsMenu = null;
            System.out.println("Options menu hidden");
        }
    }

    /**
     * Shows controls menu from another menu (like pause or main menu)
     *
     * @param parentMenu The parent menu to hide while showing controls
     */
    public void showControlsMenuFromMenu(StackPane parentMenu) {
        System.out.println("=== showControlsMenuFromMenu() called ===");
        System.out.println("parentMenu is null? " + (parentMenu == null));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/controlsMenu.fxml"));
            StackPane controlsPane = loader.load();
            System.out.println("Controls FXML loaded successfully");

            ControlsMenu controller = loader.getController();
            System.out.println("Controller obtained: " + (controller != null));

            controller.setGuiController(guiController);
            controller.setParentMenu(parentMenu);

            // Hide parent menu
            parentMenu.setVisible(false);
            System.out.println("Parent menu hidden");

            Pane overlayParent = (Pane) parentMenu.getParent();
            System.out.println("Overlay parent: " + (overlayParent != null ? overlayParent.getClass().getSimpleName() : "NULL"));

            controlsPane.prefWidthProperty().bind(overlayParent.widthProperty());
            controlsPane.prefHeightProperty().bind(overlayParent.heightProperty());

            overlayParent.getChildren().add(controlsPane);
            System.out.println("Controls pane added to scene");

            currentControlsMenu = controlsPane;
        } catch (Exception e) {
            System.out.println("ERROR loading controls menu:");
            e.printStackTrace();
        }
    }

    /**
     * Sets the current controls menu reference
     */
    public void setCurrentControlsMenu(Pane pane) {
        currentControlsMenu = pane;
    }

    /**
     * Finds the currently active root pane
     */
    private javafx.scene.Parent findActiveRoot() {
        if (mainMenu != null && mainMenu.getScene() != null) {
            return mainMenu;
        }
        else if (guiController.getGamePanel() != null && guiController.getGamePanel().getScene() != null) {
            return guiController.getGamePanel().getScene().getRoot();
        }
        else {
            for (Window window : Window.getWindows()) {
                if (window.isShowing() && window instanceof Stage) {
                    Scene scene = ((Stage) window).getScene();
                    if (scene != null) {
                        return scene.getRoot();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Ensures the root is a Pane, wrapping it if necessary
     */
    private Pane ensurePaneRoot(javafx.scene.Parent rootParent) {
        if (rootParent instanceof Pane) {
            return (Pane) rootParent;
        } else {
            StackPane wrapper = new StackPane(rootParent);
            rootParent.getScene().setRoot(wrapper);
            return wrapper;
        }
    }

    public StackPane getMainMenu() {
        return mainMenu;
    }
}