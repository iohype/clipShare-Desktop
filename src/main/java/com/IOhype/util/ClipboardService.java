package com.IOhype.util;


import javafx.application.Platform;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ClipboardService implements ClipboardOwner, Runnable {
    private final Clipboard SYSTEM_CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard(); //get system clipboard
    private final Consumer<String> bufferConsumer;


    public ClipboardService(Consumer<String> bufferConsumer) {
        this.bufferConsumer = bufferConsumer;
    }

    //listen to clipboard changes
    public void ClipBoardListener() {
        this.SYSTEM_CLIPBOARD.addFlavorListener( listener -> {
            try {
                Transferable contents = SYSTEM_CLIPBOARD.getContents( this );
                getOwnership( contents );
                String clip = (String) SYSTEM_CLIPBOARD.getData( DataFlavor.stringFlavor );
                Platform.runLater( () -> Session.clipProps.setClipString( clip ) );
                System.out.println( "Copied: " + Session.clipProps.getClipString() );

                RestCall restCall = new RestCall();
                System.out.println( restCall.putClipToServer( Session.clipProps.getIpAddress(), Session.clipProps.getClipString() ) ); // push to clip text to server

                if (Session.appConfig.isBeep()) {
                    Toolkit.getDefaultToolkit().beep();
                }


            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
        } );
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable notUsed) {
        Transferable contents = clipboard.getContents( this );
        getOwnership( contents );
        if (contents.isDataFlavorSupported( DataFlavor.stringFlavor )) {
            try {
                String string = (String) contents.getTransferData( DataFlavor.stringFlavor );
                bufferConsumer.accept( string );

            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void run() {
        Transferable transferable = SYSTEM_CLIPBOARD.getContents( this );
        getOwnership( transferable );
    }

    private void getOwnership(Transferable transferable) {
        SYSTEM_CLIPBOARD.setContents( transferable, this );
    }

    public void setBuffer(String buffer) {
        getOwnership( new StringSelection( buffer ) );
    }


}
