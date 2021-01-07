package com.IOhype.util;

import com.sun.javafx.PlatformUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import static java.lang.System.out;

public class Helper {
//    public static InetAddress getSystemNetworkConfig() throws UnknownHostException {
//        return InetAddress.getLocalHost();
//    }
    // filter through network interface to get connected Inet Address
    public static InetAddress getSystemNetworkConfig() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        ArrayList<InetAddress> inet = new ArrayList<>();
        ArrayList<NetworkInterface> workingInterfaces = new ArrayList<>();
        for (NetworkInterface networkInterface : Collections.list(nets)) {
            if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                out.println("Display Name: " + networkInterface.getDisplayName());
                out.println("Name: " + networkInterface.getName());
                workingInterfaces.add(networkInterface);
            }
        }
        for (NetworkInterface networkInterface : workingInterfaces) {
            Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddressEnumeration)) {
                out.println("InetAddress: " + inetAddress);
                if (!checkStringForLetters(inetAddress.getHostAddress()) && !inetAddress.getHostAddress().equals("192.168.137.1")) {
                    out.println("Ip address found: " + inetAddress.getHostAddress());
                    inet.add(inetAddress);
                    break;
                }
            }
            if (!inet.isEmpty())
                break;
        }

        if (inet.isEmpty())
            return null;
        else
            return inet.get(0);
    }

    //test if server is reachable
    public static boolean isServerReachable(String ipAddress, int portNumber) {

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ipAddress, portNumber));
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    //start up pbgopy server
    public static void spinUpServer() throws IOException {
        Process process = null;
        if (PlatformUtil.isWindows()) {
            File file = new File(new File("").getAbsolutePath() + "\\");
            System.out.println("Running on Windows");
            process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", ".\\pbgopy serve"}, null, file);
        } else if (PlatformUtil.isLinux()) {
            File file = new File(new File("").getAbsolutePath() + "/");
            System.out.println("Running on Linux");
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "./pbgopy serve"}, null, file);
        }

        assert process != null;
//        process.destroy();
    }

    // kill the pbgopy server from running
    public static void killServer() throws IOException {
        Process process = Runtime.getRuntime().exec("killall pbgopy");
//        kill -9 `jps | grep "DataNode" | cut -d " " -f 1`
        printResults(process);
    }

    //automate request schedule to the server
    public static Timeline serverScheduler(String ipAddress) throws IOException {
        RestCall restCall = new RestCall();
        restCall.serverTimestamp = restCall.getServerLastUpdatedTime(ipAddress); // get a copy of the current server timestamp
        ClipboardService clipboardService = new ClipboardService(out::println);
        EventQueue.invokeLater(clipboardService);
        clipboardService.ClipBoardListener(); //listen for clipboard changes
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5),
                e -> {
                    try {

                        if (restCall.getServerLastUpdatedTime(ipAddress) != 0) { //check if timestamp is not set yet
                            if (restCall.getServerLastUpdatedTime(ipAddress) > restCall.serverTimestamp) {
                                clipboardService.setBuffer(restCall.getServerClip(ipAddress));
                                String clip = restCall.getServerClip(ipAddress);
                                Platform.runLater(() -> ClipboardService.clipString.set(clip)); //set clipboard content to show in UI
                                out.println("Data fetched from server");
                                restCall.serverTimestamp = restCall.getServerLastUpdatedTime(ipAddress); // reassign timestamp from server to device timestamp
                            } else {
                                out.println("Data in server is recent");
                            }
                        }

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);

        return timeline;
    }

    //print process resultt o command line
    private static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        line = reader.readLine();
        out.println(line);
        reader.close();
    }

    //check if a string contains letters
    private static boolean checkStringForLetters(String string) {
        char ch;
        boolean letterFlag = false;
        for (int i = 0; i < string.length(); i++) {
            ch = string.charAt(i);
            if (Character.isLetter(ch)) {
                letterFlag = true;
                break;
            }
        }
        return letterFlag;
    }


}

