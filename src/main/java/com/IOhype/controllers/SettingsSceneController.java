package com.IOhype.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void HandleSetToDefault(ActionEvent event) {

    }

    @FXML
    void HandleUpdateSettings(ActionEvent event) {

    }

}
