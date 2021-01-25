package com.iohype.util;

import com.iohype.model.AppConfig;
import com.iohype.model.ClipProps;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.net.InetAddress;

public class Session {
    public static InetAddress inetAddress; // network details of system
    public static AppConfig appConfig; //app configurations of the system
    public static ClipProps clipProps; //clip board properties
    public static boolean edit_port = true; //property to make port editable
    public static BooleanProperty isClientConnected = new SimpleBooleanProperty(false);

}
