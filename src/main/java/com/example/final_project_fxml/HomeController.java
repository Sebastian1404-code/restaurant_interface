package com.example.final_project_fxml;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private SharedController sharedController;
    public void setSharedController(SharedController sharedController) {
        this.sharedController = sharedController;
    }

    @FXML
    private Button b1;

    @FXML
    private Button b2;

    @FXML
    private Button b3;

    @FXML
    private Button b4;

    @FXML
    private Button b5;

    @FXML
    private Button b6;

    @FXML
    private Button b7;

    @FXML
    private Button b8;


    @FXML
    protected void initialize() throws IOException {
        if(sharedController==null)
        {
            sharedController = new SharedController();

        }
    }

    @FXML
    protected void onClick(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        sharedController.setLastClickedButton(clickedButton);
        System.out.println(sharedController.getLastClickedButton().getId());



        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController=fxmlLoader.getController();
        loginController.setSharedController(sharedController);

        Stage currentStage=(Stage) clickedButton.getScene().getWindow();

        currentStage.setScene(scene);
        currentStage.show();
    }




}
