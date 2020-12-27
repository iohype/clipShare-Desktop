package com.IOhype.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionTestController implements Initializable {
    @FXML
    private TextField ipAddressField;

    @FXML
    private JFXButton testConnectionBtn;

    @FXML
    private JFXButton proceedBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    void ProceedEvent(ActionEvent event) {

    }

    @FXML
    void TestConnectEvent(ActionEvent event) {

    }
}
