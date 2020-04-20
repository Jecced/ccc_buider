package com.againfly.cccbuilder.display;

import com.againfly.cccbuilder.Main;
import com.againfly.cccbuilder.listener.DepsListener;
import com.againfly.cccbuilder.util.FileUtil;

import java.io.*;

public class FileDisplay {

    private static final String NEXT_LINE = System.getProperty("line.separator");


//    private static final String basePath = Main.listenPath;

//    private static final String tempPath = Main.tempPath;

//    private static final String descPath = Main.descPath;


    public static void display(String filePath){
        long time = System.currentTimeMillis();
        String name = getFileName(filePath);

        String outJsPath = filePath.replace(Main.listenPath, Main.descPath).replace(".ts",".js");

        String uuid = getUuid(outJsPath);

        if(null == uuid){
            System.out.println("uuid获取失败,没有对应以编译的js文件,需要回到cocos进行编译" + filePath);
            return;
        }


        String newTs = Main.tempPath + name + ".ts";

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
        sbTop = sbTop.replace("___s_uuid___", uuid)
                .replace("___name___", name.replace(".js", ""))
                .replace("___abs_path___", filePath.replace(Main.listenPath, "").replace(".ts", ".js"));

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
        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
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
                ps = rt.exec("cmd /k tsc " + path);
            }else{
                ps = rt.exec("tsc " + path);
            }
            ps.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String jsPath = path.substring(0, path.lastIndexOf(".ts")) + ".js";
        if(null != ps){
            ps.destroy();
        }
        return jsPath;
    }


}
