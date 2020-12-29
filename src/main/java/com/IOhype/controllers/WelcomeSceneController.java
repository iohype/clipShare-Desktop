package com.IOhype.controllers;

import com.IOhype.MainApp;
import com.IOhype.util.ClipboardService;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeSceneController implements Initializable {
    @FXML
    private JFXButton closeBtn;

    @FXML
    private VBox clientConnectionPane;

    @FXML
    private Label serverNameLbl_client;

    @FXML
    private Label cllipboard_client;

    @FXML
    private JFXButton disconnectBtn;

    @FXML
    private VBox serverPane;

    @FXML
    private Label serverName_server;

    @FXML
    private Label clipboard_server;

    @FXML
    private JFXButton stopServerConnection;

    @FXML
    private VBox homePane;

    @FXML
    private JFXButton openAsHostBtn;

    @FXML
    private JFXButton connectToHostBtn;

    @FXML
    private StackPane stackPane;

    private ClipboardService clipboardService = new ClipboardService( System.out::println );


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverPane.setOpacity( 0 );
        clientConnectionPane.setOpacity( 0 );

        closeBtn.setOnAction( event -> System.exit( 0 ) );
        EventQueue.invokeLater( clipboardService );
        clipboardService.ClipBoardListener();
    }

    @FXML
    private void HandleClientConnection(ActionEvent event) throws IOException {
        BoxBlur blur = new BoxBlur( 7, 7, 7 );
        stackPane.setEffect( blur );
        MainApp.connectionTestStage();
        stackPane.setEffect( null );
    }

    @FXML
    void HandleDisconnectServerConnect(ActionEvent event) {

    }

    @FXML
    void HandleDisconnectClientConnect(ActionEvent event) {

    }

    @FXML
    void HandleHostConnection(ActionEvent event) {

    }


}
