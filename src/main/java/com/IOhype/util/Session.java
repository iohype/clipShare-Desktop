package com.IOhype.util;

import com.IOhype.model.AppConfig;
import com.IOhype.model.ClipProps;

import java.net.InetAddress;

public class Session {
    public static InetAddress inetAddress; // network details of system
    public static AppConfig appConfig; //app configurations of the system
    public static ClipProps clipProps; //clip board properties
    public static boolean edit_port = true;

}
