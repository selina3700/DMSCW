package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Defines the game layout
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;

        //Load the layout and its controller
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);

        //Load FXML into parent node
        Parent root = fxmlLoader.load();

        //Get the controller
        GuiController c = fxmlLoader.getController();

        //Title
        primaryStage.setTitle("TetrisJFX");

        //Scene/Window
        Scene scene = new Scene(root, 400, 550);
        scene.getStylesheets().add(getClass().getResource("/window_style.css").toExternalForm());
        primaryStage.setScene(scene);

        //Disable Maximizing and Resizing
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);

        primaryStage.show();

        URL css = getClass().getResource("/window_style.css");
        System.out.println("CSS loaded from: " + css);
        scene.getStylesheets().add(css.toExternalForm());


        new GameController(c);
    }

    //Launch Application
    public static void main(String[] args) {
        launch(args);
    }
}
