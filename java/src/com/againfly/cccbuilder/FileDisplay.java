package com.againfly.cccbuilder;

import java.io.*;

public class FileDisplay {

    private static final String top = "(function() {\"use strict\";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/___abs_path___';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {\"use strict\";\n" +
            "cc._RF.push(module, '___s_uuid___', '___name___', __filename);\n" +
            "// script/Game.ts\n" +
            "\n" +
            "Object.defineProperty(exports, \"__esModule\", { value: true });";

    private static final String bot = "\n" +
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


    private static final String basePath = FileListener.listenPath;

    private static final String tempPath = FileListener.tempPath;

    private static final String descPath = FileListener.descPath;


    public static void display(String filePath){
        long time = System.currentTimeMillis();
        String name = getFileName(filePath);

        String outJsPath = filePath.replace(basePath, descPath).replace(".ts",".js");


        String uuid = getUuid(outJsPath);

        if(null == uuid){
            System.out.println("uuid获取失败,没有对应以编译的js文件,需要回到cocos进行编译" + filePath);
            return;
        }


        String newTs = tempPath + name + ".ts";

        fileCopy(filePath, newTs);


        String js = run(newTs);

        String jsContent = fileRead(js);

        if(null == jsContent){
            System.out.println("编译后的 js 获取失败:" + js);
            return;
        }

        int topSub = jsContent.indexOf("exports.__esModule = true;") + "exports.__esModule = true;".length();
        jsContent = jsContent.substring(topSub);

        StringBuilder sbTopB = new StringBuilder(FileDisplay.top);
        String sbTop = sbTopB.toString();
        sbTop = sbTop.replace("___s_uuid___", uuid)
                .replace("___name___", name.replace(".js", ""))
                .replace("___abs_path___", filePath.replace(basePath, "").replace(".ts", ".js"));

        jsContent = sbTop + jsContent + bot;


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
                sb.append(line).append("\n");
                continue;
            }

            line = line.replace("\");", "\", void 0);");
            sb.append(line);

        }

        jsContent = sb.toString();





        fileWrite(jsContent, outJsPath);

        System.out.println(name + "->编译完成, 耗时:" + (System.currentTimeMillis() - time) + "ms");

        new File(newTs).delete();
        new File(js).delete();
    }

    private static String getFileName(String path){
        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
    }

    private static String getUuid(String path){
        String orgJsContent = fileRead(path);
        if(null == orgJsContent){
            return null;
        }
        int start = orgJsContent.indexOf("cc._RF.push(module, '") + "cc._RF.push(module, '".length();
        int end = orgJsContent.indexOf("'", start);
        return  orgJsContent.substring(start, end);
    }

    /**
     * /Users/ankang/git/saisheng/slgrpg/temp/quick-scripts/assets/script/feature/battleoverride/card
     * /Users/ankang/git/saisheng/slgrpg/temp/quick-scripts/assets/scriptfeature/battleoverride/card/
     * tsc 编译文件
     */
    public static String run(String path){
        Runtime rt = Runtime.getRuntime();
        Process ps = null;
        try {
            ps = rt.exec("tsc " + path);
            ps.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        int i = ps.exitValue();
        String jsPath = null;
            jsPath = path.substring(0, path.lastIndexOf(".ts")) + ".js";
        if(null != ps){
            ps.destroy();
        }
        return jsPath;
    }


    public static void fileCopy(String source, String dest) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(new File(source));
            out = new FileOutputStream(new File(dest));

            byte[] buffer = new byte[1024 * 10];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {

        } finally {
            try{
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String fileRead(String path) {
        try{
            File file = new File(path);
            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s =bReader.readLine()) != null) {
                sb.append(s + "\n");
            }
            bReader.close();
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void fileWrite(String text, String path){
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(path, false);
            fwriter.write(text);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
