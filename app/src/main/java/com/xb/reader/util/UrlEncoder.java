package com.xb.reader.util;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created by asus on 2017/8/9.
 */

public class UrlEncoder {

    /**
     * 中文转URL编码
     * @param str
     * @return
     */
    public static String encode(String str){
        String url = null;
        try {
            url = URLEncoder.encode(str,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * URL中文编码转中文
     * @param str
     * @return
     */
    public static String decode(String str){
        String url = null;
        try {
            url = URLDecoder.decode(str,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    //判断地址中的编码
    public static String getEncoding(String bookurl){
        Pattern pattern = Pattern.compile("((/.*/)|(/))([^/]+$)");
        if (pattern.matcher(bookurl).matches()){
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(bookurl));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return Encoding(fileInputStream);
        }else {
            ByteArrayInputStream stream = new ByteArrayInputStream(bookurl.getBytes());
            return Encoding(stream);
        }
    }

    public static String Encoding(InputStream inputStream){
        UniversalDetector detector = new UniversalDetector(null);
        byte[] bytes = new byte[1024];
        try{
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int length;
            while ((length = bufferedInputStream.read(bytes)) > 0){
                detector.handleData(bytes,0,length);
            }
            detector.dataEnd();
            bufferedInputStream.close();
        }catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }
        return detector.getDetectedCharset();
    }

}
