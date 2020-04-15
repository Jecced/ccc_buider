package com.againfly.cccbuilder.listener;

import com.againfly.cccbuilder.util.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandListener implements Runnable {

    @Override
    public void run() {
        System.out.println("cmd model tsc...");

        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String cmd = null;
        while (true){
            try {
                if (null == (cmd = br.readLine())) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            cmd = cmd.trim();
            if("flush".equalsIgnoreCase(cmd) || "f".equalsIgnoreCase(cmd)){
                FileListener.init();
            }if("update".equalsIgnoreCase(cmd) || "u".equalsIgnoreCase(cmd)){
                System.out.println(HttpUtil.get("http://localhost:7456/update-db"));
                FileListener.init();
            }

            else{
                System.out.println("无效命令, 刷新命令: f or flush");
            }
        }
    }
}