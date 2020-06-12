package com.dj.net;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dj.entity.pojo.response.OpenData;
import com.dj.util.HttpResult;
import com.dj.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {
    private static Logger logger = LoggerFactory.getLogger(HttpClient.class); // 日志记录

    private static RequestConfig requestConfig = null;

    public static final String FIRST_URL = "http://192.168.1.107:8080";

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
    public static HttpResult httpPost(String uri, JSONObject jsonParam, String token) {
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
     * @param url 路径
     * @return 返回pojo
     */
    public static HttpResult httpGet(String url, String token, Map<String,String> param) {
        // get请求返回结果
        HttpResult httpResult = null;
        // get请求
        HttpGet httpGet = null;

        CloseableHttpClient client = HttpClients.createDefault();





        try
        {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != param)
                param.forEach(uriBuilder::addParameter);

            // 根据带参数的URI对象构建GET请求对象
            httpGet = new HttpGet(uriBuilder.build());

            //添加token
            if (null != token)
                httpGet.addHeader("token",token);

            //执行请求
            CloseableHttpResponse response = client.execute(httpGet);

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
        catch (URISyntaxException e) {
            logger.error("getURI解析异常:" + url, e);
        }
        catch (IOException e)
        {
            logger.error("get请求提交失败:" + url, e);
        }
        finally
        {
            httpGet.releaseConnection();
        }
        return httpResult;
    }

    public static void main(String[] args) {
        Map<String,String> param = new HashMap<>();
        param.put("limit","20");

        HttpResult httpResult = HttpClient.httpGet("http://192.168.1.107:8888/api/azxy5", null,param);

        JSONArray data = (JSONArray)httpResult.getData();
//        Root root = JSONObject.toJavaObject(data,Root.class);
        List<OpenData> openData = data.toJavaList(OpenData.class);
        System.out.println(openData);
    }
}
