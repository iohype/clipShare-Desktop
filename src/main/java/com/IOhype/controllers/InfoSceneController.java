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

    private final int MAX_TRIAL = 3; // trial count of times permitted to close system if network not found

    private int trialCount; // keep count of times trying to proceed without network

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initialise values to variables
        trialCount = 0;
        alerts = new Alerts();

        // set button actions
        closeBtn.setOnAction(event -> {
            try {
                if (Helper.getSystemNetworkConfig() == null) {
                    if (trialCount < MAX_TRIAL) {
                        alerts.Notification("NETWORK NOT FOUND", "Ensure device is connected to a network");
                        trialCount++;
                    } else {
                        System.exit(0);
                    }
                } else {
                    closeBtn.getScene().getWindow().hide();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }

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
