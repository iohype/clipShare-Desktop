package com.IOhype.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
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

    public static void spinUpServer() throws IOException {
        File file = new File( new File( "" ).getAbsolutePath() + "\\tools\\" );
        Process process = Runtime.getRuntime().exec( new String[]{"cmd","/c", ".\\pbgopy serve"}, null, file );
        printResults(process);
    }

    private static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        String line = "";
        while ((line = reader.readLine()) != null) {
            out.println( line );
        }
    }
}

