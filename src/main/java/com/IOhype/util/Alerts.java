package com.IOhype.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Alerts {
    public JFXButton acceptBtn;
    public JFXButton cancelBtn;
    private JFXDialogLayout dialogLayout;
    private JFXDialog dialog;

    public void Notification(String title, String text) {
        Notifications notificationBuilder = Notifications.create()
                .title( title )
                .text( text )
                .graphic( null )
                .hideAfter( Duration.seconds( 3 ) )
                .position( Pos.TOP_RIGHT );
        notificationBuilder.showInformation();
        notificationBuilder.hideCloseButton();

    }

    public void materialConfirmAlert(StackPane rootPane, Node nodeBlur, String Heading, String message) {
        BoxBlur blur = new BoxBlur( 3, 3, 3 );
        dialogLayout = new JFXDialogLayout();
        dialog = new JFXDialog( rootPane, dialogLayout, JFXDialog.DialogTransition.TOP );

        nodeBlur.setEffect( blur );
        nodeBlur.setDisable( true );

        cancelBtn = new JFXButton( "Cancel" );
        cancelBtn.setStyle( " -fx-background-color: red; -fx-background-radius: 10px 0 10px 0;\n" +
                "    -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;" );
        cancelBtn.setPrefSize( 103, 29 );

        acceptBtn = new JFXButton( "Accept" );
        acceptBtn.setStyle( "-fx-background-color: dodgerblue; -fx-background-radius: 10px 0 10px 0;\n" +
                "    -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-Pref-Width: 103px;" );
        acceptBtn.setPrefSize( 103, 29 );

        List<JFXButton> controls = Arrays.asList( cancelBtn, acceptBtn );
        controls.forEach( controlButton -> {
            controlButton.getStyleClass().add( "dialogButton" );
            controlButton.addEventHandler( ActionEvent.ACTION, (ActionEvent event) -> {
                dialog.close();
                nodeBlur.setDisable( false );
                nodeBlur.setEffect( null );
            } );
        } );
        Label headingLabel = new Label( Heading );
        headingLabel.setStyle( " -fx-font-weight: bold; -fx-font-size: 16px; " );
        Label messageLabel = new Label( message );
        messageLabel.setWrapText( true );
        messageLabel.setPrefSize( 500, 156 );
        dialogLayout.setHeading( headingLabel );
        dialogLayout.setBody( messageLabel );
        dialogLayout.setPrefSize( 600, 156 );
        dialog.setPrefSize( 600, 156 );
        dialogLayout.setActions( controls );
        dialog.show();
        dialog.setOnDialogClosed( (JFXDialogEvent event1) -> {
            nodeBlur.setEffect( null );
            nodeBlur.setDisable( false );
        } );

    }

    public void materialInfoAlert(StackPane rootPane, Node nodeBlur, String Heading, String message) {
        BoxBlur blur = new BoxBlur( 3, 3, 3 );
        dialogLayout = new JFXDialogLayout();
        dialog = new JFXDialog( rootPane, dialogLayout, JFXDialog.DialogTransition.TOP );

        nodeBlur.setEffect( blur );
        nodeBlur.setDisable( true );

        cancelBtn = new JFXButton( "Dismiss" );
        cancelBtn.setStyle( " -fx-background-color: red; -fx-background-radius: 10px 0 10px 0;\n" +
                "    -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;" );
        cancelBtn.setPrefSize( 103, 29 );

        List<JFXButton> controls = Collections.singletonList( cancelBtn );
        controls.forEach( controlButton -> {
            controlButton.getStyleClass().add( "dialogButton" );
            controlButton.addEventHandler( ActionEvent.ACTION, (ActionEvent event) -> {
                dialog.close();
                nodeBlur.setDisable( false );
                nodeBlur.setEffect( null );
            } );
        } );

        Label headingLabel = new Label( Heading );
        headingLabel.setStyle( " -fx-font-weight: bold; -fx-font-size: 16px; " );
        Label messageLabel = new Label( message );
        messageLabel.setWrapText( true );
        messageLabel.setPrefSize( 500, 156 );
        dialogLayout.setHeading( headingLabel );
        dialogLayout.setBody( messageLabel );
        dialogLayout.setPrefSize( 600, 156 );
        dialog.setPrefSize( 600, 156 );
        dialogLayout.setActions( controls );
        dialog.show();
        dialog.setOnDialogClosed( (JFXDialogEvent event1) -> {
            nodeBlur.setEffect( null );
            nodeBlur.setDisable( false );
        } );
    }

}
