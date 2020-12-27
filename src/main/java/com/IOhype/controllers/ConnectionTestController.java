package com.IOhype.controllers;

import com.IOhype.util.Alerts;
import com.IOhype.util.Helper;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionTestController implements Initializable {
    @FXML
    private TextField ipAddressField;

    @FXML
    private JFXButton testConnectionBtn;

    @FXML
    private JFXButton proceedBtn;

    @FXML
    private JFXButton closeBtn;

    private boolean reply;

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alerts = new Alerts();

        this.reply = false;

        this.closeBtn.setOnAction(e -> {
            this.reply = false;
            closeBtn.getScene().getWindow().hide();
        } );
    }


    @FXML
    private void ProceedEvent(ActionEvent event) throws IOException {
        String ipAddress = ipAddressField.getText().trim();
        if (Helper.isServerReachable( ipAddress, 9090 )) {
            this.reply = true;
            closeBtn.getScene().getWindow().hide();
        } else {
           this.reply = false;
            alerts.Notification( "Failed Connection","Connection to the host is not successful" );
        }
    }

    @FXML
    private void TestConnectEvent(ActionEvent event) throws IOException {
        String ipAddress = ipAddressField.getText().trim();
        if (Helper.isServerReachable( ipAddress, 9090 )) {
            System.out.println( "Device Connected" );
            alerts.Notification( "Connection Successful","Connection to the host is successful" );
        } else {
            System.out.println( "Device NOT Connected" );
            alerts.Notification( "Failed Connection","Connection to the host is not successful" );
        }
    }

    public boolean getReply(){
        return reply;
    }
}
