package com.example.democlinica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HelloApplication.primaryStage = primaryStage;

        try {
            changeScene("hello-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void changeScene(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource(fxmlFileName));
        Parent pane = loader.load();
        primaryStage.setScene(new Scene(pane));
    }
}
