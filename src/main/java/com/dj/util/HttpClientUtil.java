package com.dj.util;

import com.alibaba.fastjson.JSONObject;
import com.dj.pojo.response.Root;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class); // 日志记录

    private static RequestConfig requestConfig = null;

    private static final String FIRST_URL = "http://192.168.1.106:8080/";

    static
    {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
    }

    /**
     * post请求传输json参数
     * @param uri  url地址
     * @param
     * @return
     */
    public static HttpResult httpPost(String uri, JSONObject jsonParam,String token) {
        String url = FIRST_URL + uri;
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResult httpResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        if (null != token)
            httpPost.addHeader("token",token);
        try
        {
            if (null != jsonParam)
            {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                String str = "";
                try
                {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    httpResult = JsonUtil.string2Obj(str,HttpResult.class);
                }
                catch (Exception e)
                {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        }
        catch (IOException e)
        {
            logger.error("post请求提交失败:" + url, e);
        }
        finally
        {
            httpPost.releaseConnection();
        }
        return httpResult;
    }

    /**
     * post请求传输String参数 例如：name=Jack&sex=1&type=2
     * Content-type:application/x-www-form-urlencoded
     * @param uri            url地址
     * @param strParam       参数
     * @return
     */
    public static HttpResult httpPost(String uri, String strParam) {
        String url = FIRST_URL + uri;
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResult httpResult = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try
        {
            if (null != strParam)
            {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                String str = "";
                try
                {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    httpResult = JsonUtil.string2Obj(str,HttpResult.class);
                }
                catch (Exception e)
                {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        }
        catch (IOException e)
        {
            logger.error("post请求提交失败:" + url, e);
        }
        finally
        {
            httpPost.releaseConnection();
        }
        return httpResult;
    }

    /**
     * 发送get请求
     * @param uri 路径
     * @return 返回pojo
     */
    public static HttpResult httpGet(String uri,String token) {
        String url = FIRST_URL + uri;
        // get请求返回结果
        HttpResult httpResult = null;
        CloseableHttpClient client = HttpClients.createDefault();
        // 发送get请求
        HttpGet request = new HttpGet(url);
        if (null != token)
            request.addHeader("token",token);
        request.setConfig(requestConfig);
        try
        {
            CloseableHttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                httpResult = JsonUtil.string2Obj(strResult,HttpResult.class);
            }
            else
            {
                logger.error("get请求提交失败:" + url);
            }
        }
        catch (IOException e)
        {
            logger.error("get请求提交失败:" + url, e);
        }
        finally
        {
            request.releaseConnection();
        }
        return httpResult;
    }

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1OTAwNTYyOTQsInN1YiI6ImRqMTIzIiwiY3JlYXRlZCI6MTU5MDA1MjY5NDAzMX0.1NfnIdvAj_rH1tZzOPcsDFC_QTViO8uhzuUR7Q2HmyxPG9Mg2cRAKAsEhDpUxMR8yC3NrFsr-MDG8YmloViKOg";
        HttpResult httpResult = httpGet("config", token);
        JSONObject data = (JSONObject)httpResult.getData();
        Root root = JSONObject.toJavaObject(data,Root.class);
        System.out.println(root);
    }
}
