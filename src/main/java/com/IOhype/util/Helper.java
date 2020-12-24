package com.IOhype.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Helper {
    public static InetAddress getSystemNetworkConfig() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static boolean isServerReachable(String ipAddress, int portNumber) {
        Socket socket = new Socket();
        try {
            socket.connect( new InetSocketAddress( ipAddress, portNumber ) );
            return true;
        }catch (IOException e){
           return false;
        }

    }
}
