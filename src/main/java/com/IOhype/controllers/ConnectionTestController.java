package com.IOhype.controllers;

import com.IOhype.MainApp;
import com.IOhype.util.Alerts;
import com.IOhype.util.Constants;
import com.IOhype.util.Helper;
import com.IOhype.util.Session;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionTestController implements Initializable {
    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField portField;

    @FXML
    private JFXButton testConnectionBtn;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private Label fontLbl;

    @FXML
    private Label fontLbl1;

    @FXML
    private Label fontLbl2;

    @FXML
    private Label fontLbl3;

    @FXML
    private Label fontLbl4;

    private boolean reply;

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alerts = new Alerts();

        this.reply = false;

        this.closeBtn.setOnAction( e -> {
            this.reply = false;
            closeBtn.getScene().getWindow().hide();
        } );

        ipAddressField.setOnKeyTyped( event -> {
            String ch = event.getCharacter();
            char CH = ch.charAt( 0 );
            if (Character.isLetter( CH )) {
                event.consume();
            }
        } );

        portField.setOnKeyTyped( event -> {
            String ch = event.getCharacter();
            char CH = ch.charAt( 0 );
            if (Character.isLetter( CH )) {
                event.consume();
            }
        } );
        closeBtn.setFocusTraversable( false );
//        ipAddressField.requestFocus();
        setFont();
    }


    @FXML
    private void ConnectServerEvent(ActionEvent event) throws IOException {
        if (ipAddressField.getText().isEmpty() || portField.getText().isEmpty()){ // check if fields are empty
            alerts.Notification( "EMPTY_FIELD(S)","Ensure all fields are not empty" );
        }
        else {
            String ipAddress = ipAddressField.getText().trim();
            String port = portField.getText().trim();
            if (Helper.isIpAddress( ipAddress )){
                if ((Integer.parseInt( port ) > Constants.MAX_PORT) || (Integer.parseInt( port ) <= Constants.MIN_PORT)){

                }
                else {
                    if (Helper.isServerReachable( ipAddress, 9090 )) {
                        System.out.println( "Device Connected" );
                        alerts.Notification( "Connection Successful", "Connection to the host is successful" );
                        reply = true;
                        Session.clipProps.setIpAddress( ipAddress );
                        Session.appConfig.setPort( Integer.parseInt(port) );
                        closeBtn.getScene().getWindow().hide();

                    } else {
                        System.out.println( "Device NOT Connected" );
                        alerts.Notification( "Failed Connection", "Connection to the host is not successful" );
                    }
                }
            }
            else {

            }
        }

    }

    public boolean getReply() {
        return reply;
    }

    private void setFont() {
        Font regularFontLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Regular.ttf" ), 15 );
        Font regularFontSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Regular.ttf" ), 13 );
        Font semiBoldSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 13 );
        Font semiBoldLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 15 );

        fontLbl.setFont( regularFontLarge );
        fontLbl1.setFont( semiBoldLarge );
        fontLbl2.setFont( regularFontLarge );
        fontLbl3.setFont( regularFontSmall );
        fontLbl4.setFont( regularFontSmall );

        ipAddressField.setFont( semiBoldSmall );
        portField.setFont( semiBoldSmall );
        testConnectionBtn.setFont( semiBoldLarge );


    }
}
