package com.iohype.model;

public class AppConfig {
    private boolean dark_mode;
    private boolean beep;
    private int port;

    public AppConfig(boolean dark_mode, boolean beep, int port) {
        this.dark_mode = dark_mode;
        this.beep = beep;
        this.port = port;
    }

    public boolean isDark_mode() {
        return dark_mode;
    }

    public void setDark_mode(boolean dark_mode) {
        this.dark_mode = dark_mode;
    }

    public boolean isBeep() {
        return beep;
    }

    public void setBeep(boolean beep) {
        this.beep = beep;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
