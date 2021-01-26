package com.againfly.b;

import java.io.*;

/**
 * Created by Jecced on 2018/11/17.
 * 缩放spine图集大小
 */
public class ScaleAtlas {

    public static void main(String[] args)throws Exception {



        String input = "/Users/ankang/saisheng/ad/replace/ready/spine/21e676d9927a178dd939f3bb3529c796.atlas";

        String out = "/Users/ankang/saisheng/ad/replace/ready/spine/21e676d9927a178dd939f3bb3529c796.atlas";

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
        String line = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();
        while(null != (line = br.readLine())){
            count ++;
            if(line.startsWith("  xy: ")){
                line = "  xy: " + scalc(line);
            }
            if(line.startsWith("  size: ")){
                line = "  size: " + scalc(line);
            }
            if(line.startsWith("  orig: ")){
                line = "  orig: " + scalc(line);
            }

            sb.append(line).append("\n");

        }
        out(sb.toString(), out);
    }

    public static void out(String text, String out){
        File fp = new File(out);
        PrintWriter pfp= null;
        try {
            pfp = new PrintWriter(fp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pfp.print(text);
        pfp.flush();
        pfp.close();
    }

    public static String scalc(String line){
        int start = line.indexOf(":") + 2;
        int end = line.indexOf(",");
        String firstStr = line.substring(start, end);
        start = end + 2;
        String endStr = line.substring(start);
        int a = Integer.valueOf(firstStr) * 2;
        int b = Integer.valueOf(endStr) * 2;
        return a + ", " + b;
    }
}