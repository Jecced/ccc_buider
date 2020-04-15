package com.againfly.cccbuilder.listener;

import com.againfly.cccbuilder.Main;
import com.againfly.cccbuilder.display.FileDisplay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileListener implements Runnable{

    private static final List<String> listenFiles = new ArrayList<>();

    private static final Map<String, Long> modifyTimes = new HashMap<>();

    @Override
    public void run() {
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

                    if(!temp.exists()){
                        System.out.println("监听到文件被移除:" + path);
                        System.out.println("重新刷新");
                        init();
                        break;
                    }

                    long time = temp.lastModified();
                    if(modifyTimes.get(path) == time){
                        continue;
                    }
                    System.out.println(temp.getAbsolutePath() + ", file update");
                    modifyTimes.put(path, time);

                    FileDisplay.display(temp.getAbsolutePath());
                }
            }
        }).start();
        System.out.println("start listener");
    }


    public static void init(){
        File tempDir = new File(Main.tempPath);
        if(!tempDir.exists()){
            tempDir.mkdirs();
            System.out.println("临时编译目录已生成:" + tempDir.getAbsolutePath());
        }

        File file = new File(Main.listenPath);
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


    private static void flashFiles(File f, List<String> files){
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
