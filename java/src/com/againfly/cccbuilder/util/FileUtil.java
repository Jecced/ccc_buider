package com.againfly.cccbuilder.util;

import java.io.*;
import java.util.List;
import java.util.Set;

public class FileUtil {
    public static void creatDir(String path){
        String dirPath = path.substring(0, path.lastIndexOf("/"));
        File dir = new File(dirPath);
        if(dir.exists()) return;
        dir.mkdirs();
    }


    public static void fileCopy(String source, String dest) {
        creatDir(dest);
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
        File file = new File(path);
        try{
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
        creatDir(path);
        FileWriter writer = null;
        try {
            writer = new FileWriter(path, false);
            writer.write(text);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * 递归查询某目录下的所有文件
     */
    public static void recursiveFiles(File f, List<String> files, Set<String> suffix){
        if(f == null){
            return;
        }
        if(f.isDirectory()){
            File[] fileArray=f.listFiles();
            if(fileArray==null){
                return;
            }
            for (int i = 0; i < fileArray.length; i++) {
                recursiveFiles(fileArray[i], files, suffix);
            }
        }else{
            String path = f.getAbsolutePath();
//            if(!path.endsWith(".ts")) return;

            String name = f.getName();
            String endfix = "";
            int start = name.lastIndexOf(".");
            if(-1 != start){
                endfix = name.substring(start);
            }

            if(null == suffix){
                return;
            }
            boolean has = suffix.contains(endfix);

            if(!has){
                return;
            }

            files.add(path);
        }
    }
}
