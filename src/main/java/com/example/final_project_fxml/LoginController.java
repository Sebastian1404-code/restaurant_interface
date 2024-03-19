package com.example.final_project_fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginController {
    private SharedController sharedController;

    public void setSharedController(SharedController sharedController) {
        this.sharedController = sharedController;
    }
    @FXML
    private Button Monica;

    @FXML
    private Button Sebi;

    @FXML
    private Button Lorena;

    @FXML
    private Button Bogdan;

    @FXML
    private Button c1;
    @FXML
    private Button c2;
    @FXML
    private Button c3;
    @FXML
    private Button c4;
    @FXML
    private Button c5;
    @FXML
    private Button c6;
    @FXML
    private Button c7;
    @FXML
    private Button c8;
    @FXML
    private Button c9;
    @FXML
    private Button delete;
    @FXML
    private TextField pin;

    boolean answer;

    @FXML
    protected void add_digit(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String p;
        if (pin.getText()==null){
            p=btn.getText();
        }
        else
            p=pin.getText()+btn.getText();
        pin.setText(p);
    }

    @FXML
    protected void erase()
    {
        String aux=(pin.getText() == null || pin.getText().isEmpty())
                ? null
                : (pin.getText().substring(0, pin.getText().length() - 1));
        pin.setText(aux);
    }

    @FXML
    protected void verify(ActionEvent event)
    {
        Button btn = (Button) event.getSource();
        Connection conn=Connection_db.connect_db("restaurant","postgres","Sebi1404");
        String pass=Connection_db.read_pass(conn,"login",btn.getId());
        if(pin.getText().equals(pass))
        {
            answer=true;
            btn.setStyle("-fx-background-color: #ff0000;");
            switchScene("restaurant.fxml",btn);
        }
        else
        {
            answer=false;
            switchScene("home.fxml",btn);
        }

    }
    private void switchScene(String fxmlFile,Button button) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root=fxmlLoader.load();
            if(fxmlLoader.getController() instanceof RestaurantController)
            {
                RestaurantController restaurantController = fxmlLoader.getController();
                restaurantController.setSharedController(sharedController);
                restaurantController.fetchAndDisplayAllOrders();
            }


            Stage newStage=new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();


            Stage currentStage=(Stage) button.getScene().getWindow();
            currentStage.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }








}
