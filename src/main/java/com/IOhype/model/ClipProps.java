package com.IOhype.model;

import javafx.beans.property.SimpleStringProperty;

public class ClipProps {
    private SimpleStringProperty clipString;
    private double serverTimeStamp;
    private String ipAddress;

    public ClipProps(SimpleStringProperty clipString, double serverTimeStamp, String ipAddress) {
        this.clipString = clipString;
        this.serverTimeStamp = serverTimeStamp;
        this.ipAddress = ipAddress;
    }

    public String getClipString() {
        return clipString.get();
    }

    public SimpleStringProperty clipStringProperty() {
        return clipString;
    }

    public void setClipString(String clipString) {
        this.clipString.set( clipString );
    }

    public double getServerTimeStamp() {
        return serverTimeStamp;
    }

    public void setServerTimeStamp(double serverTimeStamp) {
        this.serverTimeStamp = serverTimeStamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
