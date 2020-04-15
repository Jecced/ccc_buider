package com.againfly.cccbuilder;

import com.againfly.cccbuilder.listener.CommandListener;
import com.againfly.cccbuilder.listener.FileListener;

public class Main {

    public static final String projectPath = "/Users/ankang/git/saisheng/slgrpg/";

    public static final String listenPath =  projectPath + "assets/";

    public static final String descPath = projectPath + "temp/quick-scripts/assets/";

    public static final String tempPath = projectPath + "temp/temp-build/";

    public static void main(String[] args) {
        System.out.println("Hello World!");

        new Thread(new FileListener()).start();

        new Thread(new CommandListener()).start();
    }
}
