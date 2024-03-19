package com.example.final_project_fxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader homeLoader = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        Parent homeRoot = homeLoader.load();
        HomeController homeController = homeLoader.getController();
        SharedController sharedController = new SharedController();
        homeController.setSharedController(sharedController);

        Scene homeScene = new Scene(homeRoot);

        stage.setTitle("Restaurant");
        stage.setScene(homeScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}