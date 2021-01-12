package com.IOhype.controllers;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import com.IOhype.MainApp;
import com.IOhype.model.ClipProps;
import com.IOhype.util.*;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {
    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton minimiseBtn;

    @FXML
    private HBox titleBar;

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

    @FXML
    private Label footerTagLbl;

    @FXML
    private HBox navBtnPane;

    @FXML
    private Label settingsLbl;

    private Thread serverThread;

    private Thread clientThread;

    private Timeline timeline;

    private double xOffset, yOffset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initialize UI content
        serverPane.setOpacity( 0 );
        clientConnectionPane.setOpacity( 0 );
        serverName_server.setText( null );
        ipAddress_server.setText( null );
        connectToHostBtn.setDisable( true );
        openAsHostBtn.setDisable( true );
        timeline = null;
        footerTagLbl.setText( "©IOhype " + LocalDate.now().getYear() );

        closeBtn.setOnAction( event -> System.exit( 0 ) ); // action event to close window

        minimiseBtn.setOnAction( event -> {
            Stage stage = (Stage) minimiseBtn.getScene().getWindow();
            stage.setIconified( true );
        } ); //action event to minimize window

        titleBar.setOnMousePressed( event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        } );

        titleBar.setOnMouseDragged( event -> {
            Stage stage = (Stage) minimiseBtn.getScene().getWindow();
            stage.setX( event.getScreenX() - xOffset );
            stage.setY( event.getScreenY() - yOffset );
        } );

        settingsLbl.setOnMouseClicked( event -> {
            try {
                BoxBlur blur = new BoxBlur( 10, 10, 10 );
                stackPane.setEffect( blur );
                MainApp.settingsStage().showAndWait();
                stackPane.setEffect( null );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );

        //show information about software usage
        Platform.runLater( () -> {
            try {
                BoxBlur blur = new BoxBlur( 6, 6, 6 );
                stackPane.setEffect( blur );
                MainApp.infoStage().showAndWait();
                stackPane.setEffect( null );

                //perform heavy tasks such as getting network connection info
                Platform.runLater( () -> {
                    try {
                        assert Session.inetAddress != null;
                        Session.inetAddress = Helper.getSystemNetworkConfig();
                        Session.clipProps = new ClipProps( new SimpleStringProperty(), 0, Session.inetAddress.getHostAddress() );
                        Session.appConfig = Helper.getAppConfig();

                        //bind labels to clip text
                        this.clipboard_server.textProperty().bind( Session.clipProps.clipStringProperty() );
                        this.cllipboard_client.textProperty().bind( Session.clipProps.clipStringProperty() );

                        serverName_server.setText( Session.inetAddress.getCanonicalHostName() );
                        ipAddress_server.setText( Session.inetAddress.getHostAddress() );
                        connectToHostBtn.setDisable( false );
                        openAsHostBtn.setDisable( false );
                        shakeAnimateNavPane();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } );

            } catch (IOException e) {
                e.printStackTrace();
            }
        } );

    }

    @FXML
    private void HandleClientConnection(ActionEvent event) throws IOException {
        BoxBlur blur = new BoxBlur( 7, 7, 7 );
        stackPane.setEffect( blur );
        boolean result = MainApp.connectionTestStage();
        stackPane.setEffect( null );

        if (result) {
            sceneChange( homePane, clientConnectionPane );
            clientThread = new Thread( () -> {
                if (timeline == null) {
                    try {
                        timeline = Helper.serverScheduler( Session.clipProps.getIpAddress() );  // schedule requests from the server
                        timeline.play(); // starts timeline
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } );
            clientThread.start();
        } else {
            shakeAnimateNavPane();
        }
    }

    @FXML
    private void HandleDisconnectServerConnect(ActionEvent event) throws IOException {
        timeline.stop(); //stop the timeline from executing the keyframe action
        Helper.killServer(); //kill the pbgopy server from receiving requests
        serverThread.interrupt(); //stop the server thread from handling the server actions
        serverThread = null;
        sceneChange( serverPane, homePane ); //change scene back to home scene
        shakeAnimateNavPane();
    }

    @FXML
    private void HandleDisconnectClientConnect(ActionEvent event) {
        timeline.stop(); //stop the timeline from executing the keyframe action
        clientThread.interrupt(); // stop the thread handling the client actions
        clientThread = null;
        sceneChange( clientConnectionPane, homePane ); //change scene back to home scene
        shakeAnimateNavPane();
    }

    @FXML
    private void HandleHostConnection(ActionEvent event) throws IOException {
        Session.clipProps.setIpAddress( Session.inetAddress.getHostAddress());
        // initialise server thread
        serverThread = new Thread( () -> {
            try {
                if (Session.appConfig.getPort() == Constants.DEFAULT_PORT) {
                    Helper.spinUpServer();
                } else {
                    Helper.spinUpServerOnPort();
                }// starts up the server;


                if (timeline == null) {
                    timeline = Helper.serverScheduler( Session.clipProps.getIpAddress() ); //schedule up requests to the server
                }
                timeline.play(); // start timeline of scheduled server requests
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );
        serverThread.start(); //start server Thread
        sceneChange( homePane, serverPane );   //change scene
    }

    // change scenes with fade effects
    private void sceneChange(Node oldScene, Node newScene) {
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

    //animate nav Btn pane
    private void shakeAnimateNavPane() {
        Platform.runLater( () -> {
            Shake shake = new Shake( navBtnPane );
            shake.setCycleCount( 2 );
            shake.play();
        } );

    }
}
