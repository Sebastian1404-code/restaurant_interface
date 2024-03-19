package com.example.final_project_fxml;

import javafx.scene.control.Button;

public class SharedController {
    private Button lastClickedButton;

    public Button getLastClickedButton() {
        return lastClickedButton;
    }

    public void setLastClickedButton(Button button) {
        this.lastClickedButton = button;
    }

}
