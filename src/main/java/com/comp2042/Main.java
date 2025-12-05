package com.comp2042;

import com.comp2042.controllers.GameController;
import com.comp2042.controllers.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * This is the main application for TetrisJFX
 * <p>
 *     This class acts as the **entry point ** for the JavaFX application and is responsible for:
 *     <ul>
 *         <li>Initializing the primary Stage and Scene.</li>
 *         <li>Loading the main FXML layout.</li>
 *         <li>Managing the initial setup and switching of scenes.</li>
 *     </ul>
 * </p>
 */
public class Main extends Application {

    private static Stage primaryStage;
    private static Scene scene;
    private static GuiController guiController;
    private static Parent gameRoot;
    private static int currentLevel = 1;
    private static GameController gameController;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        URL location = getClass().getResource("/gameLayout.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        gameRoot = loader.load();

        guiController = loader.getController();
        gameController = new GameController(guiController, currentLevel);

        scene = new Scene(gameRoot, 450, 550);
        scene.getStylesheets().add(getClass().getResource("/window_style.css").toExternalForm());

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.show();

        guiController.showMainMenu();
    }

    /**
     * Initializes and starts a new game session
     * <p>
     *     Reloads the FXML layout to ensure a fresh UI state and creates a new GameController,
     *     and sets the game scene's root to the newly loaded layout.
     * </p>
     * @param level The Starting level of the new game
     * @throws IOException FXML cannot be loaded
     */
    public static void startGame(int level) throws IOException {
        currentLevel = level;

        // Reload the FXML to get a fresh GuiController
        URL location = Main.class.getResource("/gameLayout.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        gameRoot = loader.load();
        guiController = loader.getController();
        gameController = new GameController(guiController, currentLevel);

        scene.setRoot(gameRoot);
    }

    /**
     * Returns the current instance of the GuiController
     * for interacting with the game's graphical elements.
     * @return The active {@code GuiController} instance
     */
    public static GuiController getGuiController() {
        return guiController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}