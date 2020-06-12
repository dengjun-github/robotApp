package com.dj.util;

import com.dj.Exception.ClientErrorException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.dj.util.GlobalConstant.ImageSize.KAIJIANG;
import static com.dj.util.GlobalConstant.ImageSize.LISHI;

public class TemplateConverter {
    public static Configuration configuration;
    public static String TEMPLATE_PATH = TemplateConverter.class.getResource("/templates").toString().replace("%20"," ").substring(6);
    public static String TEMP_PATH = "D://bot";
    static{
        try {
            File bot = new File(TEMP_PATH);
            if(!bot.exists()){
                bot.mkdir();
            }
            configuration = new Configuration(Configuration.VERSION_2_3_28);
            configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static File execute(String filename, Map<String,Object> parameter, GlobalConstant.ImageSize size){
        int height;
        int width;
        switch (size){
            case LISHI:
                width = LISHI.getWidth();
                List<Map<String, Object>> lishis = (List<Map<String, Object>>) parameter.get("lishis");
                height = LISHI.getHeight(lishis.size());
                break;
            case KAIJIANG:
                width = KAIJIANG.getWidth();
                height = KAIJIANG.getHeightInit();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + size);
        }


        Writer out = null;
        File tempFile = null;
        try{
            Template template = configuration.getTemplate(filename);
            tempFile = new File(TEMP_PATH+"/"+ getUUID() + ".xhtml");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)));
            template.process(parameter, out);
            Java2DRenderer renderer = new Java2DRenderer(tempFile, width, height);
            final BufferedImage img = renderer.getImage();
            final FSImageWriter imageWriter = new FSImageWriter();
            imageWriter.setWriteCompressionQuality(0.9f);
            String uuid = getUUID();
            imageWriter.write(img, TEMP_PATH+"/"+ uuid + ".png");
            File responseFile = new File(TEMP_PATH + "/" + uuid + ".png");
            return responseFile;
        }catch (Exception e){
            throw new ClientErrorException("["+filename+"]图片生成失败");
        }finally {
            if(null != out){
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tempFile.delete();
            }
        }
    }


}
