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

        // Load the main menu first
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Parent menuRoot = menuLoader.load();

        // Get the MainMenu controller and set up the stage reference
        MainMenu mainMenuController = menuLoader.getController();
        mainMenuController.setPrimaryStage(primaryStage);

        // Title
        primaryStage.setTitle("TetrisJFX");

        // Scene/Window with main menu
        scene = new Scene(menuRoot, 450, 550);
        scene.getStylesheets().add(getClass().getResource("/window_style.css").toExternalForm());
        primaryStage.setScene(scene);

        // Disable Maximizing and Resizing
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);

        primaryStage.show();
    }

    // Method to start the game from main menu
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

    // Launch Application
    public static void main(String[] args) {
        launch(args);
    }
}