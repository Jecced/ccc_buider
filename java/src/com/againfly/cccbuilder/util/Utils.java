package com.againfly.cccbuilder.util;

import com.againfly.cccbuilder.Main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String NEXT_LINE = System.getProperty("line.separator");

    /**
     * 处理所有 ts 中 tsc 处理后 property注解需要处理下的问题
     * @param jsContent ts编译后的js内容
     * @return 返回处理过的js内容
     */
    public static String displayProperty(String jsContent){
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
        return sb.toString();
    }

    /**
     * tsc 编译多个ts文件
     * @param list ts文件路径列表
     * @return ts编译后的js文件路径列表
     */
    public static List<String> tsc(List<String> list){
        List<String> outJsList = new ArrayList<>();
        StringBuilder cmd = new StringBuilder();
        if(Main.isWin){
            cmd.append("powershell ");
        }
        cmd.append("tsc ");
        for(String path : list){
            cmd.append(path).append(" ");
            outJsList.add(path.substring(0, path.lastIndexOf(".ts")) + ".js");
        }
        Runtime rt = Runtime.getRuntime();
        Process ps = null;
        try {
            ps = rt.exec(cmd.toString());
            ps.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        int exitValue = ps.exitValue();
        ps.destroy();

        return outJsList;
    }
}
