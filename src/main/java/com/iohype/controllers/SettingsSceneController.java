package com.iohype.controllers;

import com.iohype.MainApp;
import com.iohype.model.AppConfig;
import com.iohype.util.Alerts;
import com.iohype.util.Constants;
import com.iohype.util.Helper;
import com.iohype.util.Session;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

public class SettingsSceneController implements Initializable {
    @FXML
    private HBox titleBar;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private TextField portField;

    @FXML
    private JFXToggleButton themeToggle;

    @FXML
    private JFXToggleButton beepToggle;

    @FXML
    private JFXButton updateSettingsBtn;

    @FXML
    private JFXButton defaultSettingsBtn;

    @FXML
    private Label fontLbl;

    @FXML
    private Label fontLbl1;

    @FXML
    private AnchorPane root;

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alerts = new Alerts(); // initialize alerts

        portField.setOnKeyTyped( event -> {
            String ch = event.getCharacter();
            char CH = ch.charAt( 0 );
            if (!Character.isDigit( CH )) {
                event.consume();
            }
        } );
        closeBtn.setOnAction( event -> closeBtn.getScene().getWindow().hide() );

        themeToggle.setOnAction( event -> {
            if (themeToggle.isSelected()) {
                themeToggle.setText( "Dark Theme" );
                root.getStylesheets().add( MainApp.class.getResource( "/styles/dark_mode.css" ).toExternalForm() );
            } else {
                themeToggle.setText( "Light Theme" );
                if (root.getStylesheets().contains( MainApp.class.getResource( "/styles/dark_mode.css" ).toExternalForm() )) {
                    root.getStylesheets().remove( MainApp.class.getResource( "/styles/dark_mode.css" ).toExternalForm() );
                }
            }
        } );

        beepToggle.setOnAction( event -> {
            if (beepToggle.isSelected()) {
                beepToggle.setText( "Beep" );
            } else {
                beepToggle.setText( "Mute" );
            }
        } );

        Platform.runLater( () -> {
            try {
                AppConfig appConfig = Helper.getAppConfig();
                portField.setText( String.valueOf( appConfig.getPort() ) );
                themeToggle.setSelected( appConfig.isDark_mode() );
                beepToggle.setSelected( appConfig.isBeep() );
            } catch ( BackingStoreException e) {
                e.printStackTrace();
            }
        } );

        //initialize fields based
        if (Session.edit_port) {
            portField.setEditable( true );
            defaultSettingsBtn.setDisable( false );
        } else {
            portField.setEditable( false );
            defaultSettingsBtn.setDisable( true );
        }

        closeBtn.setFocusTraversable( false );
        setFont(); //initialize external fonts to system
        setDarkMode(); // initialize darkMode
    }

    @FXML
    private void HandleSetToDefault(ActionEvent event) throws IOException {
        /*
        set session values of app config
         */
        Session.appConfig.setBeep( false );
        Session.appConfig.setDark_mode( false );
        Session.appConfig.setPort( Constants.DEFAULT_PORT );
        AppConfig appConfig = new AppConfig( false, false, Constants.DEFAULT_PORT );
        Helper.setAppConfig( appConfig ); //set default config
        alerts.Notification( "DEFAULT SET", "Default configurations saved" ); //show alerts

        portField.setText( String.valueOf( Session.appConfig.getPort() ) );
        beepToggle.setSelected( Session.appConfig.isBeep() );
        themeToggle.setSelected( Session.appConfig.isDark_mode() );
    }

    @FXML
    private void HandleUpdateSettings(ActionEvent event) throws IOException {
        boolean dark_mode = themeToggle.isSelected();
        boolean beep = beepToggle.isSelected();

        if (portField.getText().isEmpty()) {
            alerts.Notification( "EMPTY_FIELD", "Port field cannot be empty" );
        } else {
            String port = portField.getText();
            if ((Integer.parseInt( port ) > Constants.MAX_PORT) || (Integer.parseInt( port ) <= Constants.MIN_PORT)) {
                alerts.Notification( "INVALID PORT", "RANGE OF PORT IS " + Constants.MIN_PORT + 1 + " - " + Constants.MAX_PORT );
            } else {
                Session.appConfig.setBeep( beep );
                Session.appConfig.setDark_mode( dark_mode );
                Session.appConfig.setPort( Integer.parseInt( port ) );
                Helper.setAppConfig( Session.appConfig ); // set app config to values selected
                alerts.Notification( "CONFIGURATION UPDATED", "App configurations updated" );
            }
        }
    }

    private void setFont() {
        Font regularFontLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Regular.ttf" ), 15 );
        Font regularFontSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Regular.ttf" ), 13 );
        Font semiBoldSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 13 );
        Font semiBoldLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 15 );

        fontLbl.setFont( semiBoldLarge );
        fontLbl1.setFont( semiBoldSmall );
        portField.setFont( regularFontLarge );
        beepToggle.setFont( regularFontSmall );
        themeToggle.setFont( regularFontSmall );

        defaultSettingsBtn.setFont( semiBoldLarge );
        updateSettingsBtn.setFont( semiBoldLarge );
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
