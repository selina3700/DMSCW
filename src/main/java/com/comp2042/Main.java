package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    private static Stage primaryStage;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // ---------------------------------------------------------
        // FIX: Load the GAME (GuiController) first, not the Menu
        // ---------------------------------------------------------
        URL location = getClass().getResource("/gameLayout.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();

        // Get the GuiController
        GuiController c = loader.getController();

        // Initialize the Game Logic (Connects GameController to GuiController)
        new GameController(c);

        // Set up the Scene with the game root temporarily
        scene = new Scene(root, 450, 550);
        scene.getStylesheets().add(getClass().getResource("/window_style.css").toExternalForm());

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(scene);

        // Disable Maximizing and Resizing
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);

        primaryStage.show();

        // ---------------------------------------------------------
        // NOW: Immediately overlay the Main Menu
        // This ensures the MainMenu receives the 'c' (GuiController) reference
        // ---------------------------------------------------------
        c.showMainMenu();
    }

    // Method to start the game (Called when 'Start' is clicked in Menu)
    public static void startGame() {
        try {
            // Load the game layout
            URL location = Main.class.getClassLoader().getResource("gameLayout.fxml");
            ResourceBundle resources = null;

            FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
            Parent root = fxmlLoader.load();

            // Get the controller
            GuiController c = fxmlLoader.getController();

            // Replace the scene with the game
            scene.setRoot(root);

            // Initialize the game
            new GameController(c);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error starting game: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}