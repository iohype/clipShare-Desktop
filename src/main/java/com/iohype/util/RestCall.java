package com.iohype.util;

import okhttp3.*;

import java.io.IOException;

public class RestCall {

    private final OkHttpClient client = new OkHttpClient();

    //get clipboard from server
    public String getServerClip(String ipAddress) throws IOException {
        String portNumber = String.valueOf(Session.appConfig.getPort());
        String getClipRoute = "http://"+ipAddress+":"+portNumber+"/";
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
            response.close();
            return "Failed to get content";

        }

    }

    //put clipboard to server
    public String putClipToServer(String ipAddress, String clipText) throws IOException {
        String portNumber = String.valueOf(Session.appConfig.getPort());
        String putClipRoute = "http://"+ipAddress+":"+portNumber+"/";
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
    public double getServerLastUpdatedTime(String ipAddress) throws IOException {
        String portNumber = String.valueOf(Session.appConfig.getPort());
        String getClipRoute = "http://"+ipAddress+":"+portNumber+"/lastupdated";
        Request request = new Request.Builder()
                .url( getClipRoute )
                .build();
        Response response = client.newCall( request ).execute();

        if (response.isSuccessful()){
            double timestamp =  Double.parseDouble(response.body().string());
            response.close();
            return timestamp;
        }
        else {
            response.close();
            return 0;
        }

    }

}
