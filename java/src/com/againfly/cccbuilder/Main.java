package com.againfly.cccbuilder;

import com.againfly.cccbuilder.listener.CommandListener;
import com.againfly.cccbuilder.listener.FileListener;

import java.io.File;

public class Main {

    public static String projectPath = null;

    public static String listenPath =  projectPath + "/assets/";

    public static String descPath = projectPath + "/temp/quick-scripts/assets/";

    public static String tempPath = projectPath + "/temp/temp-build/";

    public static boolean initListener = false;

    public static void main(String[] args) {


        if(args.length >= 1){
            String path = args[0].trim();
            File dir = new File(path);
            if(dir.exists() && dir.isDirectory()){
                initListener = true;
                updateProjectPath(path);
                new Thread(new FileListener()).start();
            }
        }

//        new Thread(new FileListener()).start();

        new Thread(new CommandListener()).start();
    }

    public static void updateProjectPath(String path) {
        projectPath = path;
        listenPath = projectPath + "/assets/";
        descPath = projectPath + "/temp/quick-scripts/assets/";
        tempPath = projectPath + "/temp/temp-build/";
    }
}
