package com.againfly.cccbuilder;

import com.againfly.cccbuilder.listener.CommandListener;
import com.againfly.cccbuilder.listener.DepsListener;
import com.againfly.cccbuilder.listener.FileListener;
import com.againfly.server.HttpServer;
import com.againfly.websocket.client.ClientHttp;

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
            "        })();\n" +
            "//# sourceMappingURL=%s.js.map";

    public static void main(String[] args) {


        String os = System.getProperty("os.name").toLowerCase();
        isWin = os.startsWith("win");

        System.out.println("=====");

//        String dirPath = System.getProperty("user.dir");
        String dirPath = getPath();
        System.out.println(dirPath);
        System.out.println("======");
        File assetsDir = new File(dirPath + "/assets");

        if(args.length == 0 && assetsDir.exists() && assetsDir.isDirectory()){
            System.out.println("检测到当前目录为cocos 项目目录");
            System.out.println(dirPath);
            args = new String[]{dirPath};
        }

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

        new Thread(new HttpServer()).start();

        new Thread(new CommandListener()).start();

        //启动socket监听线程
        ClientHttp.run();
    }

    public static String getPath() {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(System.getProperty("os.name").contains("dows")){
            path = path.substring(1,path.length());
        }
        if(path.contains("jar")){
            path = path.substring(0,path.lastIndexOf("."));
            return path.substring(0,path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }

    public static void updateProjectPath(String path) {
        projectPath = path;
        listenPath = projectPath + "/assets/";
        descPath = projectPath + "/temp/quick-scripts/assets/";
        tempPath = projectPath + "/temp/temp-build/";

        String nextLine = System.getProperty("line.separator");

        if(isWin){
//            listenPath = listenPath.replaceAll("/", "\\\\");
//            descPath = descPath.replaceAll("/", "\\\\");
//            tempPath = tempPath.replaceAll("/", "\\\\");

            top = top.replaceAll("\\n", nextLine);
            bot = bot.replaceAll("\\n", nextLine);
        }

        projectPath = new File(projectPath).getAbsolutePath();
        listenPath = new File(listenPath).getAbsolutePath();
        descPath = new File(descPath).getAbsolutePath();
        tempPath = new File(tempPath).getAbsolutePath();

        System.out.println("更新路径:");
        System.out.println("项目路径:" + projectPath);
        System.out.println("监听路径:" + listenPath);
        System.out.println("输出路径:" + descPath);
        System.out.println("临时路径:" + tempPath);
    }


}
