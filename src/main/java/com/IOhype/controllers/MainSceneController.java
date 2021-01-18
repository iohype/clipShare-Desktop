package com.IOhype.controllers;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import com.IOhype.MainApp;
import com.IOhype.model.ClipProps;
import com.IOhype.util.Alerts;
import com.IOhype.util.Constants;
import com.IOhype.util.Helper;
import com.IOhype.util.Session;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Label appTitleLbl;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton minimiseBtn;

    @FXML
    private HBox titleBar;

    @FXML
    private StackPane clientConnectionPane;

    @FXML
    private Label clipboard_client;

    @FXML
    private Label ipAddress_client;

    @FXML
    private JFXButton disconnectBtn;

    @FXML
    private StackPane serverPane;

    @FXML
    private Label ipAddress_server;

    @FXML
    private Label clipboard_server;

    @FXML
    private Label port_serverLbl;

    @FXML
    private JFXButton stopServerConnection;

    @FXML
    private StackPane homePane;

    @FXML
    private JFXButton openAsHostBtn;

    @FXML
    private JFXButton connectToHostBtn;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label footerTagLbl;

    @FXML
    private VBox navBtnPane;

    @FXML
    private Label settingsLbl;

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
    private Label fontLbl5;

    @FXML
    private Label fontLbl6;

    @FXML
    private Label fontLbl7;

    @FXML
    private Label fontLbl8;

    @FXML
    private Label fontLbl9;

    @FXML
    private Label fontLbl10;

    @FXML
    private Label fontLbl11;

    @FXML
    private Label fontLbl12;

    @FXML
    private Label fontLbl13;

    @FXML
    private Label fontLbl14;

    @FXML
    private Label fontLbl15;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label helpLbl;

    private Thread serverThread;

    private Thread clientThread;

    private Timeline timeline;

    private double xOffset, yOffset;

    private Alerts alerts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initialize UI content
        serverPane.setOpacity( 0 );
        clientConnectionPane.setOpacity( 0 );
        ipAddress_server.setText( null );
        timeline = null;
        footerTagLbl.setText( "IOhype " + LocalDate.now().getYear() );
        setFont();
        progressIndicator.setVisible( false );
        alerts = new Alerts();

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
                root.getStyleClass().add( "pane-fade-color" );
                homePane.getStyleClass().add( "pane-fade-color" );
                clientConnectionPane.getStyleClass().add( "pane-fade-color" );
                serverPane.getStyleClass().add( "pane-fade-color" );
                MainApp.settingsStage().showAndWait();
                root.getStyleClass().remove( "pane-fade-color" );
                homePane.getStyleClass().remove( "pane-fade-color" );
                clientConnectionPane.getStyleClass().remove( "pane-fade-color" );
                serverPane.getStyleClass().remove( "pane-fade-color" );
                stackPane.setEffect( null );
                setDarkMode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );

        helpLbl.setOnMouseClicked( event -> {
            try {
                BoxBlur blur = new BoxBlur( 10, 10, 10 );
                stackPane.setEffect( blur );
                root.getStyleClass().add( "pane-fade-color" );
                homePane.getStyleClass().add( "pane-fade-color" );
                clientConnectionPane.getStyleClass().add( "pane-fade-color" );
                serverPane.getStyleClass().add( "pane-fade-color" );
                MainApp.infoStage().showAndWait();
                root.getStyleClass().remove( "pane-fade-color" );
                homePane.getStyleClass().remove( "pane-fade-color" );
                clientConnectionPane.getStyleClass().remove( "pane-fade-color" );
                serverPane.getStyleClass().remove( "pane-fade-color" );
                stackPane.setEffect( null );
                setDarkMode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );

        //show information about software usage
        //perform heavy tasks such as getting network connection info
        Platform.runLater( () -> {
            try {
                Session.clipProps = new ClipProps( new SimpleStringProperty( "Clipboard data" ), 0, null );
                Session.appConfig = Helper.getAppConfig();
                setDarkMode(); //set Dark mode if enabled

                //bind labels to clip text
                this.clipboard_server.textProperty().bind( Session.clipProps.clipStringProperty() );
                this.clipboard_client.textProperty().bind( Session.clipProps.clipStringProperty() );


            } catch (IOException e) {
                e.printStackTrace();
            }

        } );


    }

    @FXML
    private void HandleClientConnection(ActionEvent event) throws IOException {
        BoxBlur blur = new BoxBlur( 7, 7, 7 );
        stackPane.setEffect( blur );
        root.getStyleClass().add( "pane-fade-color" );
        homePane.getStyleClass().add( "pane-fade-color" );
        boolean result = MainApp.connectionTestStage();
        root.getStyleClass().remove( "pane-fade-color" );
        homePane.getStyleClass().remove( "pane-fade-color" );
        stackPane.setEffect( null );

        if (result) {
            Session.edit_port = false;
            sceneChange( homePane, clientConnectionPane );
            ipAddress_client.setText( Session.clipProps.getIpAddress() );
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
        Session.edit_port = true;
        timeline.stop(); //stop the timeline from executing the keyframe action
        Helper.killServer(); //kill the pbgopy server from receiving requests
        serverThread.interrupt(); //stop the server thread from handling the server actions
        serverThread = null;
        sceneChange( serverPane, homePane ); //change scene back to home scene
        shakeAnimateNavPane();
    }

    @FXML
    private void HandleDisconnectClientConnect(ActionEvent event) {
        Session.edit_port = true;
        timeline.stop(); //stop the timeline from executing the keyframe action
        clientThread.interrupt(); // stop the thread handling the client actions
        clientThread = null;
        sceneChange( clientConnectionPane, homePane ); //change scene back to home scene
        shakeAnimateNavPane();
    }

    @FXML
    private void HandleHostConnection(ActionEvent event) {
        progressIndicator.setVisible( true ); //show
        Thread networkThread = new Thread( () -> {
            try {
                if (Helper.getSystemNetworkConfig() == null) {
                    Platform.runLater( () -> {
                        alerts.Notification( "NETWORK NOT FOUND", "Ensure device is connected to a network" );
                        progressIndicator.setVisible( false );
                    } );
                } else {
                    Session.inetAddress = Helper.getSystemNetworkConfig();
                    Session.clipProps.setIpAddress( Session.inetAddress.getHostAddress() );
                    Session.appConfig.setPort( Helper.getAppConfig().getPort() );
                    Session.edit_port = false;

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
                    Platform.runLater( () -> {
                        port_serverLbl.setText( String.valueOf( Session.appConfig.getPort() ) );
                        ipAddress_server.setText( Session.inetAddress.getHostAddress() );
                        sceneChange( homePane, serverPane );   //change scene
                        progressIndicator.setVisible( false );
                    } );
                    serverThread.start(); //start server Thread
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );
        networkThread.start();

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

    private void setFont() {
        Platform.runLater( () -> {
            Font regularFontLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Regular.ttf" ), 15 );
            Font regularFontSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Regular.ttf" ), 13 );
            Font semiBoldSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 13 );
            Font semiBoldLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-SemiBold.ttf" ), 15 );
            Font italicFontSmall = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Italic.ttf" ), 13 );
            Font italicFontLarge = Font.loadFont( MainApp.class.getResourceAsStream( "/fonts/Montserrat/Montserrat-Italic.ttf" ), 15 );
            openAsHostBtn.setFont( semiBoldLarge );
            connectToHostBtn.setFont( semiBoldLarge );
            appTitleLbl.setFont( semiBoldSmall );
            settingsLbl.setFont( semiBoldSmall );
            helpLbl.setFont( semiBoldSmall );
            footerTagLbl.setFont( regularFontSmall );
            fontLbl.setFont( italicFontLarge );
            fontLbl1.setFont( italicFontLarge );
            fontLbl12.setFont( semiBoldLarge );
            fontLbl3.setFont( italicFontLarge );
            fontLbl4.setFont( italicFontLarge );
            fontLbl5.setFont( regularFontLarge );
            //server page
            fontLbl11.setFont( italicFontLarge );
            fontLbl2.setFont( semiBoldLarge );
            fontLbl13.setFont( italicFontLarge );
            fontLbl14.setFont( regularFontSmall );
            fontLbl15.setFont( regularFontSmall );
            ipAddress_server.setFont( semiBoldLarge );
            port_serverLbl.setFont( semiBoldLarge );
            clipboard_server.setFont( regularFontSmall );
            //client page
            fontLbl6.setFont( italicFontLarge );
            fontLbl7.setFont( italicFontLarge );
            fontLbl8.setFont( semiBoldLarge );
            fontLbl9.setFont( italicFontLarge );
            fontLbl10.setFont( regularFontSmall );
            ipAddress_client.setFont( semiBoldLarge );
            clipboard_client.setFont( regularFontSmall );
        } );
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
