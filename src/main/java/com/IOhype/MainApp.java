package com.IOhype;

import com.IOhype.controllers.ConnectionTestController;
import com.IOhype.model.AppConfig;
import com.IOhype.util.Helper;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
//        show the welcome page as initial scene
        welcomePageStage(stage).show();

    }

    public static Stage welcomePageStage(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        String fxmlLocation = "/fxml/welcomeScene.fxml";
        loader.setLocation(MainApp.class.getResource(fxmlLocation));
        Parent root = loader.load();

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Welcome");

        Scene scene = new Scene(root);
        stage.setScene(scene);

        return stage;
    }

    public static Stage infoStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        String fxmlLocation = "/fxml/infoScene.fxml";
        loader.setLocation(MainApp.class.getResource(fxmlLocation));
        AnchorPane root = loader.load();

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Welcome");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(Event::consume); // prevent window from closing from windows event
        return stage;
    }

    public static boolean connectionTestStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        String fxmlLocation = "/fxml/connectionTestScene.fxml";
        loader.setLocation(MainApp.class.getResource(fxmlLocation));

        AnchorPane root = loader.load();
        ConnectionTestController connectionTestController = loader.getController();

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Connection Test");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        return connectionTestController.getReply();
    }

    public static Stage settingsStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        String fxmlLocation = "/fxml/settingsScene.fxml";
        loader.setLocation(MainApp.class.getResource(fxmlLocation));
        AnchorPane root = loader.load();

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Application Configurations");

        Scene scene = new Scene(root);
        stage.setScene(scene);

        return stage;
    }
}
