package com.againfly.cccbuilder.display;

import com.againfly.cccbuilder.Main;
import com.againfly.cccbuilder.entity.FileInfo;
import com.againfly.cccbuilder.listener.DepsListener;
import com.againfly.cccbuilder.util.FileUtil;
import com.againfly.cccbuilder.util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDisplay {

    private static final String NEXT_LINE = System.getProperty("line.separator");

    /**
     * 多文件处理
     * @param filePathList 待处理的文件列表
     */
    public static void multipleDisplay(List<String> filePathList){
        long time = System.currentTimeMillis();
        List<FileInfo> list = new ArrayList<>();
        List<String> newTsList = new ArrayList<>();
        for(String filePath: filePathList){
            FileInfo info = new FileInfo();
            info.setFilePath(filePath);
            String name = getFileName(filePath);
            info.setName(name);
            String outJsPath = filePath
                    .replace(Main.listenPath, Main.descPath)
                    .replace(".ts",".js");
            info.setOutJsPath(outJsPath);
            String uuid = getUuid(outJsPath);
            if(null == uuid){
                System.out.println("uuid获取失败,没有对应已经编译的js文件,需要回到cocos进行编译生成uuid" + filePath);
                continue;
            }
            info.setUuid(uuid);
            String newTs = Main.tempPath + "/" + name + "/" + name + ".ts";
            newTs = new File(newTs).getAbsolutePath();
            info.setNewTs(newTs);

            FileUtil.fileCopy(filePath, newTs);

            newTsList.add(newTs);

            list.add(info);
        }

        List<String> jsPathList = Utils.tsc(newTsList);
        for(int i = 0, len = jsPathList.size(); i < len; i++){
            String js = jsPathList.get(i);
            FileInfo fileInfo = list.get(i);

            String filePath = fileInfo.getFilePath();
            String name = fileInfo.getName();
            String uuid = fileInfo.getUuid();
            String outJsPath = fileInfo.getOutJsPath();
            String newTs = fileInfo.getNewTs();

            String jsContent = FileUtil.fileRead(js);
            fileInfo.setJsContent(jsContent);
            if(null == jsContent){
                System.out.println("编译后的 js 获取失败:" + js);
                continue;
            }

            int topSub = jsContent.indexOf("exports.__esModule = true;") + "exports.__esModule = true;".length();
            jsContent = jsContent.substring(topSub);

            StringBuilder sbTopB = new StringBuilder(Main.top);
            String sbTop = sbTopB.toString();

            String absPath = filePath.replace(Main.listenPath, "").replace(".ts", ".js");
            absPath = absPath.replaceAll("\\\\","/");
//        absPath = absPath.replaceAll("//", "/");
            absPath = absPath.substring(1);
            sbTop = sbTop.replace("___s_uuid___", uuid)
                    .replace("___name___", name.replace(".js", ""))
                    .replace("___abs_path___", absPath);

            jsContent = sbTop + jsContent + Main.bot;

            jsContent = Utils.displayProperty(jsContent);


            FileUtil.fileWrite(jsContent, outJsPath);

            DepsListener.flushDepsInfo(js);

            new File(newTs).delete();
            new File(js).delete();
            new File(newTs.substring(0, newTs.lastIndexOf("/"))).delete();
        }
        System.out.println("->编译完成, 耗时:" + (System.currentTimeMillis() - time) + "ms");



    }

    /**
     * 单文件处理
     * @param filePath 待文件路径
     */
    public static void display(String filePath){
        long time = System.currentTimeMillis();
        String name = getFileName(filePath);

        String outJsPath = filePath.replace(Main.listenPath, Main.descPath).replace(".ts",".js");

        String uuid = getUuid(outJsPath);

        if(null == uuid){
            System.out.println("uuid获取失败,没有对应以编译的js文件,需要回到cocos进行编译" + filePath);
            return;
        }


        String newTs = Main.tempPath + "/" + name + ".ts";
        newTs = new File(newTs).getAbsolutePath();

        System.out.println("拷贝ts到临时目录:" + newTs);

        FileUtil.fileCopy(filePath, newTs);


        String js = tsc(newTs);

        String jsContent = FileUtil.fileRead(js);

        if(null == jsContent){
            System.out.println("编译后的 js 获取失败:" + js);
            return;
        }

        int topSub = jsContent.indexOf("exports.__esModule = true;") + "exports.__esModule = true;".length();
        jsContent = jsContent.substring(topSub);

        StringBuilder sbTopB = new StringBuilder(Main.top);
        String sbTop = sbTopB.toString();

        String absPath = filePath.replace(Main.listenPath, "").replace(".ts", ".js");
        absPath = absPath.replaceAll("\\\\","/");
//        absPath = absPath.replaceAll("//", "/");
        absPath = absPath.substring(1);
        sbTop = sbTop.replace("___s_uuid___", uuid)
                .replace("___name___", name.replace(".js", ""))
                .replace("___abs_path___", absPath);

        jsContent = sbTop + jsContent + Main.bot;


        ByteArrayInputStream is=new ByteArrayInputStream(jsContent.getBytes());
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while (true){
            try {
                if (null == (line = br.readLine())) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(null == line) continue;

            if(!line.contains(".prototype, \"")){
                sb.append(line).append(NEXT_LINE);
                continue;
            }

            line = line.replace("\");", "\", void 0);");
            sb.append(line);

        }

        jsContent = sb.toString();

        FileUtil.fileWrite(jsContent, outJsPath);

        System.out.println(name + "->编译完成, 耗时:" + (System.currentTimeMillis() - time) + "ms");

        DepsListener.flushDepsInfo(js);

        new File(newTs).delete();
        new File(js).delete();
    }

    private static String getFileName(String path){
        File file = new File(path);
        if(file.exists()){
            String name = file.getName();
            return name.substring(0, name.lastIndexOf("."));
        }
        return null;
//        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
    }

    private static String getUuid(String path){
        String orgJsContent = FileUtil.fileRead(path);
        if(null == orgJsContent){
            return null;
        }
        int start = orgJsContent.indexOf("cc._RF.push(module, '") + "cc._RF.push(module, '".length();
        int end = orgJsContent.indexOf("'", start);
        return  orgJsContent.substring(start, end);
    }

    private static String tsc(String path){
        Runtime rt = Runtime.getRuntime();
        Process ps = null;
        try {
            if(Main.isWin){
                ps = rt.exec("powershell tsc " + path);
//                ps = rt.exec("cmd /c tsc " + path);
            }else{
                ps = rt.exec("tsc " + path);
            }
            ps.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String jsPath = path.substring(0, path.lastIndexOf(".ts")) + ".js";
        int exitValue = ps.exitValue();
        ps.destroy();

        System.out.println("生成编译JS文件:" + jsPath);
        return jsPath;
    }




}
