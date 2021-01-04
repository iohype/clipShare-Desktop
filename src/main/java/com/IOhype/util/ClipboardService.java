package com.IOhype.util;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.function.Consumer;

public class ClipboardService implements ClipboardOwner, Runnable {
    private final Clipboard SYSTEM_CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    private final Consumer<String> bufferConsumer;
    public static SimpleStringProperty clipString = new SimpleStringProperty();
    private Timestamp timestamp;

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
                Platform.runLater( () -> clipString.set( clip ) );
                System.out.println( "Copied: " + clipString.get() );
                Toolkit.getDefaultToolkit().beep();

                RestCall restCall = new RestCall();
                System.out.println( restCall.putClipToServer( RestCall.ipAddress, clipString.get() ) );

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

    public Timestamp getTimestamp() {
        if (timestamp == null) {
            return new Timestamp( System.currentTimeMillis() );
        } else {
            return timestamp;
        }

    }

}
