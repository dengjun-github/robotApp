package com.dj.net;

import com.alibaba.fastjson.JSONObject;
import com.dj.util.HttpResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;


public class WebClientVerticle{


    private static final WebClientOptions webClientOptions = new WebClientOptions();
    public final static Vertx vertx = Vertx.vertx();
    private final static WebClient webClient = WebClient.create(vertx,webClientOptions);
    static{
        //设置连接超时时间,单位毫秒
        webClientOptions.setConnectTimeout(5000);

    }



    /**
     * get请求
     * @param port 端口号
     * @param host 主机地址
     * @param requestURI 路由
     * @param param 参数,可为null
     * @param token token
     * @return
     */
    public static void httpGet(int port, String host, String requestURI, JSONObject param, String token , Handler<AsyncResult<HttpResponse<HttpResult>>> handler) {

        HttpRequest<Buffer> request = webClient.get(port, host, requestURI);
        if (null != param)
            param.forEach((k,v) -> {
                request.addQueryParam(k,v.toString());
            });
        if (null != token)
            request.putHeader("token",token);
        request.as(BodyCodec.json(HttpResult.class)).send(handler);
    }

    /**
     * post请求
     * @param port
     * @param host
     * @param requestURI
     * @param param
     * @param token
     * @return
     */
    public static void httpPost(int port, String host, String requestURI, JSONObject param, String token,Handler<AsyncResult<HttpResponse<HttpResult>>> handler) {
        HttpRequest<Buffer> request = webClient.post(port, host, requestURI);
        if (null != token)
            request.putHeader("token",token);
        request.as(BodyCodec.json(HttpResult.class))
                .sendJsonObject(JsonObject.mapFrom(param), handler);
    }





    public static void main(String[] args) {

        JSONObject json = new JSONObject();
        json.put("account","dj123");
        json.put("password","456789");

        httpPost(8080,"192.168.1.106","/authentication", json,null, res -> {
            if (res.succeeded()) {
                System.out.println(res.result().body());
            }
                });
//        JSONObject loginData = (JSONObject)login.getData();

        HttpRequest<Buffer> request = webClient.post(8080, "192.168.1.106", "/authentication");
//        if (null != queryParam)
//            queryParam.forEach((k,v) -> {
//                request.addQueryParam(k,v.toString());
//            });
//        if (null != token)
//        request.putHeader("token",loginData.get("token").toString());
        request.as(BodyCodec.json(HttpResult.class))
                .sendJsonObject(JsonObject.mapFrom(json), res -> {
            if (res.succeeded()) {
                HttpResponse<HttpResult> result = res.result();
                System.out.println(result.body().toString());

            } else {
//                return new HttpResult()
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
