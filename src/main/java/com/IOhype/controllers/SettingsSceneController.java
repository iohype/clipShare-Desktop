package com.IOhype.controllers;

import com.IOhype.model.AppConfig;
import com.IOhype.util.Alerts;
import com.IOhype.util.Helper;
import com.IOhype.util.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alerts = new Alerts(); // initialize alerts

        closeBtn.setOnAction( event -> closeBtn.getScene().getWindow().hide() );

        Platform.runLater( () -> {
            try {
                AppConfig appConfig = Helper.getAppConfig();
                portField.setText( String.valueOf( appConfig.getPort() ) );
                themeToggle.setSelected( appConfig.isDark_mode() );
                beepToggle.setSelected( appConfig.isBeep() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );

    }

    @FXML
    private void HandleSetToDefault(ActionEvent event) throws IOException {
        Session.appConfig.setBeep( false );
        Session.appConfig.setDark_mode( false );
        Session.appConfig.setPort( 9090 );
        AppConfig appConfig = new AppConfig( false, false, 9090 );
        Helper.setAppConfig(appConfig); //set default config
        alerts.Notification( "DEFAULT SET","Default configurations saved" );
    }

    @FXML
    private void HandleUpdateSettings(ActionEvent event) throws IOException {
        boolean dark_mode = themeToggle.isSelected();
        boolean beep = beepToggle.isSelected();

        if (portField.getText().isEmpty()){
            alerts.Notification( "EMPTY_FIELD","Port field cannot be empty" );
        }
        else {
            String port = portField.getText();
            Session.appConfig.setBeep( beep );
            Session.appConfig.setDark_mode( dark_mode );
            Session.appConfig.setPort( Integer.parseInt( port ) );
            Helper.setAppConfig( Session.appConfig ); // set app config to values selected
            alerts.Notification( "CONFIGURATION UPDATED","App configurations updated" );
        }
    }

}
