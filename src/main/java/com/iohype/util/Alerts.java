package com.iohype.util;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.awt.*;

public class Alerts {

    public void Notification(String title, String text) {
        Notifications notificationBuilder = Notifications.create()
                .title( title )
                .text( text )
                .graphic( null )
                .hideAfter( Duration.seconds( 3 ) )
                .position( Pos.TOP_RIGHT );
        if (Session.appConfig.isDark_mode()) {
            notificationBuilder.darkStyle();
        }
        notificationBuilder.showInformation();
        notificationBuilder.hideCloseButton();

    }

    public void displayTray(String title, String message) throws AWTException {
        if (SystemTray.isSupported()){
            //Obtain only one instance of the SystemTray object
            SystemTray tray = SystemTray.getSystemTray();

            //If the icon is a file
            Image image = Toolkit.getDefaultToolkit().createImage( "icon.png" );
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

            TrayIcon trayIcon = new TrayIcon( image, "Tray Demo" );
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize( true );
            //Set tooltip text for the tray icon
            trayIcon.setToolTip( "System tray icon demo" );
            tray.add( trayIcon );

            trayIcon.displayMessage( title, message, TrayIcon.MessageType.INFO );
        }
        else {
            Notification( title, message);
        }

    }

}
