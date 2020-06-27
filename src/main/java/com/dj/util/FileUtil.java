package com.dj.util;

import com.dj.Exception.ClientErrorException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    /**
     * @从制定URL下载文件并保存到指定目录
     * @param filePath 文件将要保存的目录
     * @param method 请求方法，包括POST和GET
     * @param url 请求的路径
     * @return
     */

    public static File saveUrlAs(String url, String filePath,String fileName, String method){

        //System.out.println("fileName---->"+filePath);
        //判断文件的保存路径后面是否以/结尾
        if (!(filePath+fileName).endsWith("/")) {
            filePath += "/";
        }
        //创建要写入的文件
        File file = new File(filePath+fileName+".png");

        //创建不同的文件夹目录
        File folder=new File(filePath);
        //判断文件夹是否存在
        if (!folder.exists())
        {
            //如果文件夹不存在，则创建新的的文件夹
            folder.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(encode(url));
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            bis = new BufferedInputStream(inputStream);

            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）

            fileOut = new FileOutputStream(file);
            bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new ClientErrorException("下载图片失败 : " + e.getMessage() );
        }finally {
            try {
                bos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.disconnect();
        }

        return file;

    }

    public static String encode(String url){
        try {

            Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
            int count = 0;
            while (matcher.find()) {
                //System.out.println(matcher.group());
                String tmp=matcher.group();
                url=url.replaceAll(tmp,java.net.URLEncoder.encode(tmp,"UTF-8"));
            }
            // System.out.println(count);
            //url = java.net.URLEncoder.encode(url,"gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return url;
    }
//    public static void main(String[] args) {
//        saveUrlAs(encode("http://192.168.1.107:8080/images/停止攻击.jpg"), IMAGE_PATH , Keys.SETTINGS_DEFAULT_DOWN_30,"GET");
//    }

}
