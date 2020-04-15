package com.againfly.cccbuilder;

import com.againfly.cccbuilder.listener.CommandListener;
import com.againfly.cccbuilder.listener.FileListener;

public class Main {

    public static String projectPath = null;

    public static String listenPath =  projectPath + "/assets/";

    public static String descPath = projectPath + "/temp/quick-scripts/assets/";

    public static String tempPath = projectPath + "/temp/temp-build/";

    public static void main(String[] args) {

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
