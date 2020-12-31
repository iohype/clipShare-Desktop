package com.IOhype.util;


import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ClipboardService implements ClipboardOwner, Runnable {
    private final Clipboard SYSTEM_CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    private final Consumer<String> bufferConsumer;
    private String clipBoardContent;

    public ClipboardService(Consumer<String> bufferConsumer) {
        this.bufferConsumer = bufferConsumer;
    }

    //listen to clipboard changes
    public void ClipBoardListener() {
        this.SYSTEM_CLIPBOARD.addFlavorListener( listener -> {
            try {
                Transferable contents = SYSTEM_CLIPBOARD.getContents( this );
                getOwnership( contents );
                clipBoardContent = (String) SYSTEM_CLIPBOARD.getData( DataFlavor.stringFlavor );
                System.out.println( "Copied: " + clipBoardContent );
                Toolkit.getDefaultToolkit().beep();

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
                Toolkit.getDefaultToolkit();
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

    public String getClipBoardContent(){
        return clipBoardContent;
    }

}
