package com.againfly.cccbuilder;

import com.againfly.cccbuilder.listener.CommandListener;
import com.againfly.cccbuilder.listener.DepsListener;
import com.againfly.cccbuilder.listener.FileListener;
import com.againfly.server.HttpServer;

import java.io.File;

public class Main {

    public static String projectPath = null;

    public static String listenPath =  projectPath + "/assets/";

    public static String descPath = projectPath + "/temp/quick-scripts/assets/";

    public static String tempPath = projectPath + "/temp/temp-build/";

    public static boolean initListener = false;

    public static boolean isWin = false;


    public static String top = "(function() {\"use strict\";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/___abs_path___';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {\"use strict\";\n" +
            "cc._RF.push(module, '___s_uuid___', '___name___', __filename);\n" +
            "// script/Game.ts\n" +
            "\n" +
            "Object.defineProperty(exports, \"__esModule\", { value: true });";

    public static String bot = "\n" +
            "cc._RF.pop();\n" +
            "        }\n" +
            "        if (CC_EDITOR) {\n" +
            "            __define(__module.exports, __require, __module);\n" +
            "        }\n" +
            "        else {\n" +
            "            cc.registerModuleFunc(__filename, function () {\n" +
            "                __define(__module.exports, __require, __module);\n" +
            "            });\n" +
            "        }\n" +
            "        })();";

    public static void main(String[] args) {

        String os = System.getProperty("os.name").toLowerCase();
        isWin = os.startsWith("win");


        DepsListener.flushCocosSettingsDeps();

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
        new Thread(new HttpServer()).start();
    }

    public static void updateProjectPath(String path) {
        projectPath = path;
        listenPath = projectPath + "/assets/";
        descPath = projectPath + "/temp/quick-scripts/assets/";
        tempPath = projectPath + "/temp/temp-build/";

        String nextLine = System.getProperty("line.separator");

        listenPath = listenPath.replaceAll("/", java.io.File.separator);
        descPath = descPath.replaceAll("/", java.io.File.separator);
        tempPath = tempPath.replaceAll("/", java.io.File.separator);

        top = top.replaceAll("\\n", nextLine);
        bot = bot.replaceAll("\\n", nextLine);
    }
}
