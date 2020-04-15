package com.againfly.cccbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileListener {

//    public static final String projectPath = "/Users/ankang/NewProject/";
    public static final String projectPath = "/Users/ankang/git/saisheng/slgrpg/";

    public static final String listenPath =  projectPath + "assets/";

    public static final String descPath = projectPath + "temp/quick-scripts/assets/";

    public static final String tempPath = projectPath + "temp/temp-build/";

    private static final List<String> listenFiles = new ArrayList<>();

    private static final Map<String, Long> modifyTimes = new HashMap<>();

    public static void main(String args[]){



//        System.exit(0);

        init();
        System.out.println("init success");

        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String path: listenFiles){
                    File temp = new File(path);
                    long time = temp.lastModified();
                    if(modifyTimes.get(path) == time){
                        continue;
                    }
                    System.out.println(temp.getAbsolutePath() + ", 文件更新");
                    modifyTimes.put(path, time);

                    FileDisplay.display(temp.getAbsolutePath());
                }
            }
        }).start();
        System.out.println("start listener");


        System.out.println("cmd model run...");

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
                init();
            }else{
                System.out.println("无效命令, 刷新命令: f or flush");
            }
        }
    }

    private static void init(){
        File tempDir = new File(tempPath);
        if(!tempDir.exists()){
            tempDir.mkdirs();
            System.out.println("临时编译目录已生成:" + tempDir.getAbsolutePath());
        }

        File file = new File(listenPath);
        listenFiles.clear();
        modifyTimes.clear();

        flashFiles(file, listenFiles);

        for(String path : listenFiles){
            File temp = new File(path);
            long time = temp.lastModified();
            modifyTimes.put(path, time);
        }
        System.out.println("listener file flush success, file count:" + listenFiles.size());
    }


    public static void flashFiles(File f, List<String> files){
        if(f == null){
            return;
        }
        if(f.isDirectory()){
            File[] fileArray=f.listFiles();
            if(fileArray==null){
                return;
            }
            for (int i = 0; i < fileArray.length; i++) {
                flashFiles(fileArray[i], files);
            }
        }else{
            String path = f.getAbsolutePath();
            if(!path.endsWith(".ts")) return;
            files.add(path);
        }
    }
}
