package com.againfly.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * https://blog.csdn.net/u010939285/article/details/81231221
 */
public class ClientMain {

    private static Bootstrap boot = null;
    private static Channel server = null;

    public static void run(String sid){
        try{
            init();
            connection(sid);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void ping(){
        if(null == server) return;
        System.out.println("client send: 2");
        server.writeAndFlush(new TextWebSocketFrame("2"));
    }

    public static void creatPintInfo(int time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        }, time, time);
    }

    private static void init() throws Exception{
        if(null != boot){
            return;
        }

        EventLoopGroup group=new NioEventLoopGroup();
        boot = new Bootstrap();
        boot.option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .group(group)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new HttpClientCodec(),
                                new HttpObjectAggregator(1024 * 1024 * 10));
                        p.addLast("hookedHandler", new WebSocketClientHandler());
                    }
                });
    }

    private static void connection(String sid) throws Exception{
        URI websocketURI = new URI("ws://localhost:7456/socket.io/?EIO=3&transport=websocket&sid=" + sid);
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        //进行握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String)null, true,httpHeaders);
        System.out.println("connect");
        final Channel channel = boot.connect(websocketURI.getHost(),websocketURI.getPort()).sync().channel();
        WebSocketClientHandler handler = (WebSocketClientHandler)channel.pipeline().get("hookedHandler");
        handler.setHandshaker(handshaker);
        handshaker.handshake(channel);
        //阻塞等待是否握手成功
        handler.handshakeFuture().sync();

        TextWebSocketFrame frame = new TextWebSocketFrame("2probe");
        channel.writeAndFlush(frame);

        server = channel;
        creatPintInfo(25000);
    }


}
