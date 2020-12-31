package com.IOhype.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoSceneController implements Initializable {
    @FXML
    private JFXButton closeBtn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeBtn.setOnAction( event -> closeBtn.getScene().getWindow().hide() );
    }
}
