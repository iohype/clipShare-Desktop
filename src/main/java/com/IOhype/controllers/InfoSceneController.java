package com.IOhype.controllers;

import com.IOhype.util.Alerts;
import com.IOhype.util.Helper;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoSceneController implements Initializable {
    @FXML
    private JFXButton closeBtn;
    @FXML
    private JFXButton proceedBtn;

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initialise values to variables
        alerts = new Alerts();

        // set button actions
        closeBtn.setOnAction(event -> {
            System.exit(0); // exit the a
        });

        proceedBtn.setOnAction(e -> Platform.runLater(() -> {
            try {
                if (Helper.getSystemNetworkConfig() == null) {
                    Alerts alerts = new Alerts();
                    alerts.Notification("NETWORK NOT FOUND", "Ensure device is connected to a network");
                } else {
                    closeBtn.getScene().getWindow().hide();
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
        }));
    }
}
