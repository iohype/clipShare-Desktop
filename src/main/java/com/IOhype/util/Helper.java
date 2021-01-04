package com.IOhype.util;

import javafx.application.Platform;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

import static java.lang.System.out;

public class Helper {
//    public static InetAddress getSystemNetworkConfig() throws UnknownHostException {
//        return InetAddress.getLocalHost();
//    }


    public static InetAddress getSystemNetworkConfig() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        ArrayList<InetAddress> inet = new ArrayList<>();
        ArrayList<NetworkInterface> workingInterfaces = new ArrayList<>();
        for (NetworkInterface networkInterface : Collections.list( nets )) {
            if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                out.println( "Display Name: " + networkInterface.getDisplayName() );
                out.println( "Name: " + networkInterface.getName() );
                workingInterfaces.add( networkInterface );
            }
        }
        for (NetworkInterface networkInterface : workingInterfaces) {
            Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
            for (InetAddress inetAddress : Collections.list( inetAddressEnumeration )) {
                out.println( "InetAddress: " + inetAddress );
                if (!checkStringForLetters( inetAddress.getHostAddress() ) && !inetAddress.getHostAddress().equals( "192.168.137.1" )) {
                    out.println( "Ip address found: " + inetAddress.getHostAddress() );
                    inet.add( inetAddress );
                    break;
                }
            }
            if (!inet.isEmpty())
                break;
        }

        if (inet.isEmpty())
            return null;
        else
            return inet.get( 0 );
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

    public static void spinUpServer() throws IOException {
        File file = new File( new File( "" ).getAbsolutePath() + "\\" );
        Process process = Runtime.getRuntime().exec( new String[]{"cmd", "/c", ".\\pbgopy serve"}, null, file );
        printResults( process );
    }

    public static void serverScheduler(String ipAddress) {
        RestCall restCall = new RestCall();
        ClipboardService clipboardService = new ClipboardService( System.out::println );
        EventQueue.invokeLater( clipboardService );
        clipboardService.ClipBoardListener();

        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (!restCall.getServerClip( ipAddress ).contains( "The data not found" )) {
                        clipboardService.setBuffer( restCall.getServerClip( ipAddress ) );
                        String clip = restCall.getServerClip( ipAddress );
                        Platform.runLater( () -> ClipboardService.clipString.set( clip ) );
                        out.println( "gotten from server" );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule( myTask, 0, 5000 );
    }

    private static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        String line = "";
        while ((line = reader.readLine()) != null) {
            out.println( line );
        }
    }

    private static boolean checkStringForLetters(String string) {
        char ch;
        boolean letterFlag = false;
        for (int i = 0; i < string.length(); i++) {
            ch = string.charAt( i );
            if (Character.isLetter( ch )) {
                letterFlag = true;
                break;
            }
        }
        return letterFlag;
    }

}

