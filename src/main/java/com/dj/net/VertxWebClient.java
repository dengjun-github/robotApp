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
import org.jetbrains.annotations.NotNull;


public class VertxWebClient {
;
    private static HttpRequest<Buffer> get ;
    private static HttpRequest<Buffer> post ;
    private static HttpRequest<Buffer> put ;
    private static HttpRequest<Buffer> delete;


    private static final WebClientOptions webClientOptions = new WebClientOptions();
    public final static Vertx vertx = Vertx.vertx();
    private final static WebClient webClient = WebClient.create(vertx,webClientOptions);
    static{
        //设置连接超时时间,单位毫秒
        webClientOptions.setConnectTimeout(5000);

    }
    public enum RestFul{
        GET,POST,DELET,PUT;
    }



    /**
     * 请求
     * @param port 端口号
     * @param host 主机地址
     * @param requestURI 路由
     * @param param 参数,可为null
     * @param token token
     * @return
     */
    public static void httpRequest(int port, String host, String requestURI, JSONObject param, String token , @NotNull RestFul method, Handler<AsyncResult<HttpResponse<HttpResult>>> handler) {
        switch (method) {
            case GET:
                get = webClient.get(port, host, requestURI);
                if (null != token)
                    get.putHeader("token",token);
                if (null != param)
                    param.forEach((k,v) -> {
                        get.addQueryParam(k,v.toString());
                    });
                get.as(BodyCodec.json(HttpResult.class)).send(handler);
                break;
            case POST:
                post = webClient.post(port, host, requestURI);
                if (null != token)
                    post.putHeader("token",token);
                post.as(BodyCodec.json(HttpResult.class))
                        .sendJsonObject(JsonObject.mapFrom(param), handler);
                break;
            case PUT:
                put = webClient.put(port, host, requestURI);
                if (null != token)
                    post.putHeader("token",token);
                post.as(BodyCodec.json(HttpResult.class))
                        .sendJsonObject(JsonObject.mapFrom(param), handler);
                break;
            case DELET:
                delete = webClient.delete(port, host, requestURI);
                if (null != token)
                    post.putHeader("token",token);
                post.as(BodyCodec.json(HttpResult.class))
                        .sendJsonObject(JsonObject.mapFrom(param), handler);
                break;

        }

    }

}
