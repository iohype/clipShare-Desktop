package com.iohype.util;

import com.iohype.model.AppConfig;
import com.sun.javafx.PlatformUtil;
import io.javalin.Javalin;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;

import static com.iohype.util.Session.clipProps;
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

    //test if server is reachable
    public static boolean isServerReachable(String ipAddress, int portNumber) {

        Socket socket = new Socket();
        try {
            socket.connect( new InetSocketAddress( ipAddress, portNumber ) );
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    //start up pbgopy server
    public static void spinUpServer() throws IOException {
        Process process = null;
        if (PlatformUtil.isWindows()) {
            File file = new File( new File( "" ).getAbsolutePath() + "\\" );
            System.out.println( "Running on Windows" );
            process = Runtime.getRuntime().exec( new String[]{"cmd", "/c", ".\\pbgopy serve"}, null, file );
        } else if (PlatformUtil.isLinux()) {
            File file = new File( new File( "" ).getAbsolutePath() + "/" );
            System.out.println( "Running on Linux" );
            process = Runtime.getRuntime().exec( new String[]{"/bin/sh", "-c", "./pbgopy serve"}, null, file );
        }

        assert process != null;
//        process.destroy();
    }

    //start up pbgopy server
    public static void spinUpServerOnPort() throws IOException {
        Process process = null;
        if (PlatformUtil.isWindows()) {
            File file = new File( new File( "" ).getAbsolutePath() + "\\" );
            System.out.println( "Running on Windows" );
            process = Runtime.getRuntime().exec( new String[]{"cmd", "/c", ".\\pbgopy serve --port=" + Session.appConfig.getPort()}, null, file );
        } else if (PlatformUtil.isLinux()) {
            File file = new File( new File( "" ).getAbsolutePath() + "/" );
            System.out.println( "Running on Linux" );
            process = Runtime.getRuntime().exec( new String[]{"/bin/sh", "-c", "./pbgopy serve --port=" + Session.appConfig.getPort()}, null, file );
        }

        assert process != null;
//        process.destroy();
    }

    // kill the pbgopy server from running
    public static void killServer() throws IOException {
        Process process = null;
        if (PlatformUtil.isWindows()) {
            process = Runtime.getRuntime().exec( "taskkill /F /IM pbgopy.exe" );
        } else if (PlatformUtil.isLinux()) {
            process = Runtime.getRuntime().exec( "killall pbgopy" );
        }

//        kill -9 `jps | grep "DataNode" | cut -d " " -f 1`
        printResults( process );
    }

    //automate request schedule to the server
    public static Timeline serverScheduler(String ipAddress) throws IOException {
        RestCall restCall = new RestCall();

        clipProps.setServerTimeStamp( restCall.getServerLastUpdatedTime( ipAddress ) ); // get a copy of the current server timestamp
        ClipboardService clipboardService = new ClipboardService( out::println );
        EventQueue.invokeLater( clipboardService );
        clipboardService.ClipBoardListener(); //listen for clipboard changes
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add( new KeyFrame( Duration.seconds( 5 ),
                e -> {
                    try {
                        if (Helper.isServerReachable( ipAddress, Session.appConfig.getPort() )) {
                            if (restCall.getServerLastUpdatedTime( ipAddress ) != 0) { //check if timestamp is not set yet
                                if (restCall.getServerLastUpdatedTime( ipAddress ) > clipProps.getServerTimeStamp()) {
                                    clipboardService.setBuffer( restCall.getServerClip( ipAddress ) );
                                    String clip = restCall.getServerClip( ipAddress );
                                    Platform.runLater( () -> clipProps.setClipString( clip ) ); //set clipboard content to show in UI
                                    out.println( "Data fetched from server" );
                                    clipProps.setServerTimeStamp( restCall.getServerLastUpdatedTime( ipAddress ) ); // reassign timestamp from server to device timestamp
                                } else {
                                    out.println( "Data in server is recent" );
                                }
                            }
                        } else {
                            Session.isClientConnected.setValue( false );
                        }

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                } )
        );
        timeline.setCycleCount( Animation.INDEFINITE );

        return timeline;
    }

    //print process result of command line
    private static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        String line;
        line = reader.readLine();
        out.println( line );
        reader.close();
    }

    //check if a string contains letters
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

    //create properties file for app data
    public static AppConfig getAppConfig() throws IOException {
        String windowsPath = new File( "" ).getAbsoluteFile() + "\\config.properties";
        String linuxPath = new File( "" ).getAbsoluteFile() + "/config.properties";
        String path = PlatformUtil.isWindows() ? windowsPath : linuxPath;

        if (Files.exists( Paths.get( path ) )) {
            Properties properties = new Properties();
            FileInputStream fileInputStream =  new FileInputStream( path );
            properties.load( fileInputStream );
            return new AppConfig( Boolean.parseBoolean( properties.getProperty( "dark_mode" ) ), Boolean.parseBoolean( properties.getProperty( "beep" ) ), Integer.parseInt( properties.getProperty( "port" ) ) );
        } else {
            Properties properties = new Properties();
            properties.put( "port", "9090" );
            properties.put( "beep", "false" );
            properties.put( "dark_mode", "false" );
            FileOutputStream fileOutputStream =  new FileOutputStream( path );
            properties.store( fileOutputStream, "Updated on " + LocalDate.now() );
            return new AppConfig( Boolean.parseBoolean( properties.getProperty( "dark_mode" ) ), Boolean.parseBoolean( properties.getProperty( "beep" ) ), Integer.parseInt( properties.getProperty( "port" ) ) );
        }

    }

    //set config properties
    public static void setAppConfig(AppConfig appConfig) throws IOException {
        Properties properties = new Properties();
        if (PlatformUtil.isWindows()) {
            properties.load( new FileInputStream( new File( "" ).getAbsoluteFile() + "\\config.properties" ) );
        } else if (PlatformUtil.isLinux()) {
            properties.load( new FileInputStream( new File( "" ).getAbsoluteFile() + "/config.properties" ) );
        }
        properties.put( "dark_mode", String.valueOf( appConfig.isDark_mode() ) );
        properties.put( "beep", String.valueOf( appConfig.isBeep() ) );
        properties.put( "port", String.valueOf( appConfig.getPort() ) );
        if (PlatformUtil.isWindows()) {
            properties.store( new FileOutputStream( new File( "" ).getAbsoluteFile() + "\\config.properties" ), "Updated on " + LocalDate.now() );
        } else if (PlatformUtil.isLinux()) {
            properties.store( new FileOutputStream( new File( "" ).getAbsoluteFile() + "/config.properties" ), "Updated on " + LocalDate.now() );
        }

    }

    //check if IP address format is valid
    public static boolean isIpAddress(String ipAddress) {
        final String zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
        final String IP_REGEXP = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        final Pattern IP_PATTERN = Pattern.compile( IP_REGEXP );
        return IP_PATTERN.matcher( ipAddress ).matches();
    }

    //whip in internal server to run witb endpoints
    public static Javalin startServer() throws IOException {
        Javalin app = Javalin.create();
        app.get( "/", ctx -> ctx.result( Session.serverClipProps.getClipString() ) );
        app.get( "/lastupdated", ctx -> ctx.result( String.valueOf( Session.serverClipProps.getServerTimeStamp() ) ) );
        app.put( "/", ctx -> {
            String clipPosted = ctx.body();
            Session.serverClipProps.setClipString( clipPosted );
            Session.serverClipProps.setServerTimeStamp( new Timestamp( System.currentTimeMillis() ).getTime() );
            ctx.result( "modified" );

        } );
        return app;
    }


}

