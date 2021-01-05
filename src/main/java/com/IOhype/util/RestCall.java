package com.IOhype.util;

import okhttp3.*;

import java.io.IOException;

public class RestCall {

    private final String PORT_NUMBER = "9090";

    public static  String ipAddress = "";

    private final OkHttpClient client = new OkHttpClient();

    //get clipboard from server
    public String getServerClip(String ipAddress) throws IOException {
        String getClipRoute = "http://"+ipAddress+":"+PORT_NUMBER+"/";
        Request request = new Request.Builder()
                .url( getClipRoute )
                .build();
        Response response = client.newCall( request ).execute();
        if (response.isSuccessful()){
            String response_body = response.body().string();
            response.close();
            return response_body;

        }
        else {
            return "Failed to get content";
        }

    }

    //put clipboard to server
    public String putClipToServer(String ipAddress, String clipText) throws IOException {
        String putClipRoute = "http://"+ipAddress+":"+PORT_NUMBER+"/";
        MediaType mediaType = MediaType.parse( "text/x-markdown; charset=utf-8" );
        Request request = new Request.Builder()
                .url( putClipRoute )
                .put( RequestBody.create( clipText,mediaType ))
                .build();
        Response response = client.newCall( request ).execute();
        if (response.isSuccessful()) {
            response.close();
            return "Successful to put";
        }
        else {
            response.close();
            return "Failed to put";
        }

    }

    //get clipboard from server
    public String getServerLastUpdatedTime(String ipAddress) throws IOException {
        String getClipRoute = "http://"+ipAddress+":"+PORT_NUMBER+"/lastupdated";
        Request request = new Request.Builder()
                .url( getClipRoute )
                .build();
        Response response = client.newCall( request ).execute();
        if (response.isSuccessful()){
            return response.body().string();
        }
        else {
            return "Failed";
        }

    }

}
