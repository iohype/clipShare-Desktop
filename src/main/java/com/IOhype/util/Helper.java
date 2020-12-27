package com.IOhype.util;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

import static java.lang.System.out;

public class Helper {
    public static InetAddress getSystemNetworkConfig() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static boolean isServerReachable(String ipAddress, int portNumber) throws IOException {

        Socket socket = new Socket();
        try {
            socket.connect( new InetSocketAddress( ipAddress, portNumber ) );
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public static String getIpAddress() throws SocketException {
        String ip = "";
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp())
                continue;
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                ip = address.getHostAddress();
                out.println( networkInterface.getDisplayName() + " " + ip + " " + address.getCanonicalHostName() );


            }

        }
        return ip;
    }

}

