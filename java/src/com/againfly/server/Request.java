package com.againfly.server;


import java.io.InputStream;

public class Request {
    private InputStream input;

    private String uri;

    public Request(InputStream input) {
        this.input = input;
        parse();
    }

    public void parse() {
        //Read a set of characters from the socket
        StringBuilder request = new StringBuilder(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        System.err.print(request.toString());
        uri = parseUri(request.toString());
    }

    public String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(" ");
        if (index1 == -1) {
            return null;
        }
        index2 = requestString.indexOf(" ", index1 + 1);
        if (index2 <= index1) {
            return null;
        }
        return requestString.substring(index1 + 1, index2);
    }

    public String getUri() {
        return this.uri;
    }
}