package com.againfly.cccbuilder.listener;

import com.againfly.cccbuilder.entity.ScriptDeps;
import com.againfly.cccbuilder.util.FileUtil;
import com.againfly.cccbuilder.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 依赖监听
 */
public class DepsListener{
//    public static void main(String[] args) {
//        flushCocosSettingsDeps();
//        flushDepsInfo("/Users/ankang/git/saisheng/slgrpg/temp/quick-scripts/assets/script/Game.js");
//    }


    public static String getScript(){
        StringBuilder sb = new StringBuilder();

        sb.append("window._CCSettings.scripts = ");

        sb.append(JSON.toJSON(depsList));

//        sb.append("\n\n").append("console.log(window._CCSettings.scripts);");

        return sb.toString();
    }

    /**
     * key js script name
     * value js deps info
     */
    private static Map<String, ScriptDeps> nameDepsCache = new ConcurrentHashMap<>();

    private static List<ScriptDeps> depsList = new CopyOnWriteArrayList<>();

    public static void flushCocosSettingsDeps(){
        //延迟1s进行刷新 否则偶尔会报错
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nameDepsCache.clear();
        depsList.clear();

        String localSettings = HttpUtil.get("http://localhost:7456/settings.js");

        if(null == localSettings){
            System.out.println("请启动cocos 客户端 来刷新依赖信息");
            return;
        }

        int start = localSettings.indexOf("scripts: ") + "scripts: ".length();
        int end = localSettings.indexOf("rawAssets");

        localSettings = localSettings.substring(start, end).trim();
        localSettings = localSettings.substring(0, localSettings.length() - 1);

        List<ScriptDeps> list = JSONObject.parseArray(localSettings, ScriptDeps.class);

        for(int i = 0 ; i < list.size(); i++){
            ScriptDeps sd = list.get(i);
            String file = sd.getFile();
            String name = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."));
            name = name.toLowerCase();

            sd.setName(name);
            sd.setIndex(i);
            depsList.add(sd);
            nameDepsCache.put(name, sd);

            if(file.contains("javascript-state-machine")){
                nameDepsCache.put("javascript-state-machine", sd);
            }
        }

        System.out.println("Cocos脚本依赖查询刷新完毕");
    }


    /**
     * 刷新js的依赖信息
     * @param path
     */
    public static void flushDepsInfo(String path){
        File file = new File(path);
        if(!file.exists()) return;
        if(file.isDirectory()) return;
        flushDepsInfo(file);
    }

    public static void flushDepsInfo(File file){
        String name = file.getName();
        boolean isJs = name.endsWith(".js");
        if(!isJs) return;

        System.out.println(name);

        name = name.substring(0, name.lastIndexOf("."));
        name = name.toLowerCase();

        ScriptDeps sd = nameDepsCache.get(name);

        if(null == sd){
            System.err.println("当前文件依赖查询失败: " + name + ", 可能是新文件");
            return;
        }


        Map<String, Integer> deps = sd.getDeps();

        System.out.println("依赖更新前:" + name + "\n" + deps);


        deps.clear();

        String js = FileUtil.fileRead(file.getAbsolutePath());

        int start = -1;
        int end = -1;

        while (-1 != (start = js.indexOf("require(\"", start))){
            end = js.indexOf("\")", start);
            String require = js.substring(start + 9, end);

            start = end;

            String depsName = require;
            int last = require.lastIndexOf("/");
            if(-1 != last){
                depsName = depsName.substring(last + 1);
            }

            depsName = depsName.toLowerCase();


            ScriptDeps depsInfo = nameDepsCache.get(depsName);

            if(null == depsInfo){
                System.err.println("依赖获取失败 : " + depsName);
                continue;
            }

            deps.put(require, depsInfo.getIndex());
        }

        System.out.println("依赖更新后:" + name + "\n" + deps);
    }

}
