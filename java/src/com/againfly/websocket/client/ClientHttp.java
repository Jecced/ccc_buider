package com.againfly.websocket.client;

import com.againfly.cccbuilder.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ClientHttp {

    private static String sid = "";
    private static int pingInterval = 25000;
    private static int pingTimeout = 60000;
    private static int index = 0;


//    public static void main(String[] args) throws InterruptedException {
//        run();
//    }

    public static void run(){
        doGet0();
        doGet1();
        ClientMain.run(sid);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doGet2();
    }


    private static void doGet2(){
        long now = System.currentTimeMillis();
        String path = String.format(
                "http://localhost:7456/socket.io/?EIO=3&transport=polling&t=%d-%d&sid=%s",
                now,
                index,
                sid
        );
        index++;
        System.out.println(path);
        String resp = HttpUtil.get(path);
        System.out.println("doGet2 " + resp);
    }

    private static void doGet1(){
        long now = System.currentTimeMillis();
        String path = String.format(
                "http://localhost:7456/socket.io/?EIO=3&transport=polling&t=%d-%d&sid=%s",
                now,
                index,
                sid
        );
        index++;
        String resp = HttpUtil.get(path);
        System.out.println(resp);
    }

    /** 发起第一次请求 */
    private static void doGet0(){
        long now = System.currentTimeMillis();
        String path = String.format("http://localhost:7456/socket.io/?EIO=3&transport=polling&t=%d-%d", now, index);
        index++;
        String resp = HttpUtil.get(path);
        displayFirstInfo(resp);
    }

    /** 解析首次cocos客户端 websocket io请求 */
    private static void displayFirstInfo(String firstOut){
        firstOut = firstOut.substring(firstOut.indexOf("{"));
        System.out.println(firstOut);
        JSONObject root = JSON.parseObject(firstOut);
        sid = root.getString("sid");
        pingInterval = root.getIntValue("pingInterval");
        pingTimeout = root.getIntValue("pingTimeout");
    }
}
