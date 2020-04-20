package com.againfly.server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable{
    /**
     * WEB_ROOT is the directory where our html and other files reside.
     * For this package,WEB_ROOT is the "webroot" directory under the
     * working directory.
     * the working directory is the location in the file system
     * from where the java command was invoke.
     */
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private boolean shutdown = false;

    @Override
    public void run() {
        HttpServer server = new HttpServer();
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8059;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        while (!shutdown) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                //create Request object and parse
                Request request = new Request(input);

                //create Response object
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

}