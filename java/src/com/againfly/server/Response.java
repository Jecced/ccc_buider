package com.againfly.server;

import com.againfly.cccbuilder.listener.DepsListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private static final int BUFFER_SIZE = 1024 * 10;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
        try {
            this.output.write("HTTP/1.1 200 OK\r\n".getBytes());
            this.output.write("Content-Type:text/html;charset=utf-8\r\n".getBytes());
            this.output.write("\r\n".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
//            File file = new File(HttpServer.WEB_ROOT, request.getUri());
//            if (file.exists()) {
//
//                this.output.write("HTTP/1.1 200 OK\r\n".getBytes());
//                this.output.write("Content-Type:text/javascript;charset=utf-8\r\n".getBytes());
//                this.output.write("Access-Control-Allow-Origin:*\r\n".getBytes());
//                this.output.write("Access-Control-Allow-Headers:Content-Type,Content-Length,Authorization,Origin,Accept,X-Requested-With\r\n".getBytes());
//                this.output.write("Access-Control-Allow-Methods:GET,POST,OPTIONS.PUT,PATCH,DELETE\r\n".getBytes());
//                this.output.write("X-Powered-By:3.2.1\r\n".getBytes());
//                this.output.write("\r\n".getBytes());
//
//                fis = new FileInputStream(file);
//                while ((fis.read(bytes, 0, BUFFER_SIZE)) != -1) {
//                    output.write(bytes, 0, BUFFER_SIZE);
//                }
//            } else {
//
//                //file not found
//                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
//                        "Content-Type:text/html\r\n" +
//                        "Content-Length:23\r\n" +
//                        "\r\n" +
//                        "<h1>File Not Found</h1>";
//                output.write(errorMessage.getBytes());
//            }
            output.write(DepsListener.getScript().getBytes());
            output.flush();
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }
}