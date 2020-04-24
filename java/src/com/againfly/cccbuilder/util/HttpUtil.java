package com.againfly.cccbuilder.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtil {
    public static String get(String url){
        HttpURLConnection http = null;
        try {
            URL urlObj = new URL(url);
            if(url.startsWith("https://")){
                http = (HttpsURLConnection) urlObj.openConnection();
            }else{
                http = (HttpURLConnection) urlObj.openConnection();
            }
            http.setRequestMethod("GET");
            http.setConnectTimeout(30 * 1000);
            http.setReadTimeout(30 * 1000);
            http.setRequestProperty("Charset", "utf-8");
            http.setDefaultUseCaches(false);
            http.setDoOutput(true);
            http.connect();
            InputStream in = http.getInputStream();
            String line = null;
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            while(null != (line = br.readLine())){
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return null;
    }
}
