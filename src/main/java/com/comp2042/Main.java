package com.comp2042;

import com.comp2042.controllers.GameController;
import com.comp2042.controllers.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    private static Stage primaryStage;
    private static Scene scene;
    private static GuiController guiController;
    private static Parent gameRoot;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        URL location = getClass().getResource("/gameLayout.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        gameRoot = loader.load();

        guiController = loader.getController();
        new GameController(guiController);

        scene = new Scene(gameRoot, 450, 550);
        scene.getStylesheets().add(getClass().getResource("/window_style.css").toExternalForm());

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.show();

//        guiController.onSceneLoaded();
        guiController.showMainMenu();
    }

    public static void startGame() {
        scene.setRoot(gameRoot);
        guiController.newGame(null);
    }

    public static GuiController getGuiController() {
        return guiController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}