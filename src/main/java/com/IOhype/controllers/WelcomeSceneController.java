package com.IOhype.controllers;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import com.IOhype.MainApp;
import com.IOhype.util.Alerts;
import com.IOhype.util.ClipboardService;
import com.IOhype.util.Helper;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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
    private Label ipAddress_server;

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

    private Alerts alerts = new Alerts();

    private Thread serverThread;

    private Thread clientThread;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverPane.setOpacity( 0 );
        clientConnectionPane.setOpacity( 0 );

        closeBtn.setOnAction( event -> System.exit( 0 ) );
        serverName_server.setText( null );
        ipAddress_server.setText( null );

        //show information about software usage
        Platform.runLater( () -> {
            try {
                BoxBlur blur = new BoxBlur( 6, 6, 6 );
                stackPane.setEffect( blur );
                MainApp.infoStage().showAndWait();
                stackPane.setEffect( null );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );


    }

    @FXML
    private void HandleClientConnection(ActionEvent event) throws IOException {
        BoxBlur blur = new BoxBlur( 7, 7, 7 );
        stackPane.setEffect( blur );
        MainApp.connectionTestStage();
        stackPane.setEffect( null );
    }

    @FXML
    private void HandleDisconnectServerConnect(ActionEvent event) {
        serverThread.interrupt();
        sceneChange( serverPane, homePane );
    }

    @FXML
    void HandleDisconnectClientConnect(ActionEvent event) {

    }

    @FXML
    private void HandleHostConnection(ActionEvent event) throws UnknownHostException {
        if (Helper.getSystemNetworkConfig().getHostAddress().equals("") || Helper.getSystemNetworkConfig().toString().isEmpty()){
            alerts.Notification( "CONNECTION NOT FOUND","Ensure you are connected to a network" );
        }
        else {
            serverName_server.setText( Helper.getSystemNetworkConfig().getCanonicalHostName() );
            ipAddress_server.setText( Helper.getSystemNetworkConfig().getHostAddress() );
            // initialise server thread
            serverThread = new Thread( () -> {
                try {
                    Helper.spinUpServer();
                    Helper.serverScheduler( Helper.getSystemNetworkConfig().getHostAddress() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } );
            //start server Thread
            serverThread.start();
            //change scene
            sceneChange( homePane, serverPane );

        }

    }

    // change scenes
    private void sceneChange(Node oldScene, Node newScene){
        FadeOut fadeOut = new FadeOut( oldScene );
        fadeOut.setSpeed( 2.0 );
        fadeOut.play();
        fadeOut.getTimeline().setOnFinished( e -> {
            FadeIn fadeIn = new FadeIn( newScene );
            fadeIn.setSpeed( 2.0 );
            fadeIn.play();
            newScene.toFront();
        } );
    }
}
