package com.IOhype.controllers;

import com.IOhype.MainApp;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoSceneController implements Initializable {

    @FXML
    private Label headerLbl;

    @FXML
    private Label fontLbl;

    @FXML
    private Label fontLbl1;

    @FXML
    private Label fontLbl2;

    @FXML
    private Label fontLbl3;

    @FXML
    private Label fontLbl5;

    @FXML
    private Label fontLbl6;

    @FXML
    private Label fontLbl7;

    @FXML
    private Label fontLbl8;

    @FXML
    private JFXButton dismissBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set dismiss button active
        dismissBtn.setOnAction( e -> Platform.runLater( () -> dismissBtn.getScene().getWindow().hide() ) );
        setFont();
    }

    private void setFont() {
        Font regularFontSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Regular.ttf" ), 13 );
        Font semiBoldSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 13 );
        Font semiBoldLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 15 );

        headerLbl.setFont( semiBoldLarge );
        fontLbl.setFont( regularFontSmall );
        fontLbl1.setFont( regularFontSmall );
        fontLbl2.setFont( regularFontSmall );
        fontLbl3.setFont( semiBoldSmall );
        fontLbl5.setFont( regularFontSmall );
        fontLbl6.setFont( semiBoldSmall );
        fontLbl7.setFont( regularFontSmall );
        fontLbl8.setFont( regularFontSmall );

        dismissBtn.setFont( semiBoldLarge );
    }
}
