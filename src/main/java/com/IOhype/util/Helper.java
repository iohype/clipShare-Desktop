package com.IOhype.util;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

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

    public static void spinUpServer() throws IOException {
        File file = new File( new File( "" ).getAbsolutePath() + "\\tools\\" );
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


}

