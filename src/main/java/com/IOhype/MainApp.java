package com.IOhype;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger( MainApp.class );

    public static void main(String[] args) throws Exception {
        launch( args );
    }

    public void start(Stage stage) throws Exception {
        //show the welcome page as initial scene
        welcomePageStage( stage ).show();
    }

    public static Stage welcomePageStage(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        String fxmlLocation = "/fxml/welcomeScene.fxml";
        loader.setLocation( MainApp.class.getResource( fxmlLocation ) );
        Parent root = loader.load();

        stage.initStyle( StageStyle.UNDECORATED );
        stage.setTitle( "Welcome" );

        Scene scene = new Scene( root );
        stage.setScene( scene );

        return stage;
   }
}
