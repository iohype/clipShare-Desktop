package com.iohype.controllers;

import com.iohype.MainApp;
import com.iohype.util.Alerts;
import com.iohype.util.Constants;
import com.iohype.util.Helper;
import com.iohype.util.Session;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionTestController implements Initializable {
    @FXML
    private AnchorPane root;

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

    @FXML
    private HBox invalidIpPane;

    @FXML
    private Label fontLbl5;

    @FXML
    private HBox addressNotReachablePane;

    @FXML
    private Label fontLbl6;

    @FXML
    private Label fontLbl7;

    @FXML
    private Label fontLbl8;

    @FXML
    private HBox invalidPortPane;

    @FXML
    private Label fontLbl9;

    @FXML
    private ProgressIndicator progressIndicator;

    private boolean reply;

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize components
        alerts = new Alerts();
        closeBtn.setFocusTraversable( false );
        progressIndicator.setVisible( false );
        setFont();
        initializeAlertPanes();
        setDarkMode();

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

    }

    @FXML
    private void ConnectServerEvent(ActionEvent event) {
        if (ipAddressField.getText().isEmpty() || portField.getText().isEmpty()) { // check if fields are empty
            alerts.Notification( "EMPTY_FIELD(S)", "Ensure all fields are not empty" );
        } else {
            String ipAddress = ipAddressField.getText().trim();
            String port = portField.getText().trim();
            if (Helper.isIpAddress( ipAddress )) {
                setAllPaneHidden();
                ipAddressField.setPromptText( "e.g 102.168.43.168" );
                if (ipAddressField.getStyleClass().contains( "error-border" )) {
                    ipAddressField.getStyleClass().remove( "error-border" );
                }
                if (ipAddressField.getStyleClass().contains( "noConnect-border" )) {
                    ipAddressField.getStyleClass().remove( "noConnect-border" );
                }
                if ((Integer.parseInt( port ) > Constants.MAX_PORT) || (Integer.parseInt( port ) <= Constants.MIN_PORT)) {
                    portField.getStyleClass().add( "error-border" );
                    portField.setPromptText( "Invalid input" );
                    portField.setText( null );
                    setAlertPaneShown( invalidPortPane, invalidIpPane, addressNotReachablePane );
                } else {
                    portField.setPromptText( "e.g 9090" );
                    if (portField.getStyleClass().contains( "error-border" )) {
                        portField.getStyleClass().remove( "error-border" ); // change portField border back to normal
                    }
                    progressIndicator.setVisible( true );
                    ipAddressField.setDisable( true );
                    portField.setDisable( true );
                    // rotate the logo pane
                    Thread socketThread = new Thread( () -> {
                        if (Helper.isServerReachable( ipAddress, Integer.parseInt( port ) )) {
                            System.out.println( "Device Connected" );
                            reply = true;
                            Session.clipProps.setIpAddress( ipAddress );
                            Session.appConfig.setPort( Integer.parseInt( port ) );

                            Platform.runLater( () -> {
                                progressIndicator.setVisible( false );
                                ipAddressField.setDisable( false );
                                portField.setDisable( false );
                                alerts.Notification( "Connection Successful", "Connection to the host is successful" );
                                closeBtn.getScene().getWindow().hide();
                            } );

                        } else {
                            System.out.println( "Device NOT Connected" );
                            Platform.runLater( () -> {
                                ipAddressField.getStyleClass().add( "noConnect-border" );
                                portField.getStyleClass().add( "noConnect-border" );
                                progressIndicator.setVisible( false );
                                ipAddressField.setDisable( false );
                                portField.setDisable( false );
                                setAlertPaneShown( addressNotReachablePane, invalidPortPane, invalidIpPane );
                            } );
                        }
                    } );
                    socketThread.start();

                }
            } else {
                setAlertPaneShown( invalidIpPane, invalidPortPane, addressNotReachablePane );
                ipAddressField.getStyleClass().add( "error-border" );
                ipAddressField.setText( null );
                ipAddressField.setPromptText( "Bad input" );
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
        fontLbl5.setFont( regularFontSmall );
        fontLbl6.setFont( regularFontSmall );
        fontLbl7.setFont( regularFontSmall );
        fontLbl8.setFont( regularFontSmall );
        fontLbl9.setFont( regularFontSmall );

        ipAddressField.setFont( semiBoldSmall );
        portField.setFont( semiBoldSmall );
        testConnectionBtn.setFont( semiBoldLarge );


    }

    private void initializeAlertPanes() {
        invalidIpPane.setVisible( false );
        invalidPortPane.setVisible( false );
        addressNotReachablePane.setVisible( false );
    }

    private void setAlertPaneShown(Node nodeToShow, Node nodeHidden1, Node nodeHidden2) {
        nodeToShow.setVisible( true );
        nodeToShow.toFront();
        nodeHidden1.setVisible( false );
        nodeHidden2.setVisible( false );
    }

    private void setAllPaneHidden() {
        addressNotReachablePane.setVisible( false );
        invalidPortPane.setVisible( false );
        invalidIpPane.setVisible( false );
    }

    private void setDarkMode() {
        String dark_mode = MainApp.class.getResource( "/styles/dark_mode.css" ).toExternalForm();
        if (Session.appConfig.isDark_mode()) {
            root.getStylesheets().add( dark_mode );
        } else {
            if (root.getStylesheets().contains( dark_mode )) {
                root.getStylesheets().remove( dark_mode );
            }
        }

    }
}
