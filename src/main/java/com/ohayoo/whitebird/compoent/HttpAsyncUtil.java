package com.ohayoo.whitebird.compoent;

import com.ohayoo.whitebird.boot.GlobalContext;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 基于 vertx 的 http 异步请求客户端
 * 同步请求
 * @see cn.hutool.http.HttpUtil
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-10
 */
public class HttpAsyncUtil {
    private static WebClient client ;

    public static void init(){
        client = WebClient.create(GlobalContext.getVertx());
    }

    /**
     * get请求
     * @param uri myserver.mycompany.com
     * @param param
     * @param headerParam
     * @param handler
     */
    public static void get(String uri, HashMap<String,String> param,
                           HashMap<String,String> headerParam, Handler<HttpResponse<Buffer>> handler){
        HttpRequest<Buffer> bufferHttpRequest = client
                .get(uri);
        initGetAndHeaderParam(param, headerParam, bufferHttpRequest);
        bufferHttpRequest.send()
                .onSuccess(response -> handler.handle(response))
                .onFailure(err -> handler.handle(null));
    }


    //post请求
    public static void post(String uri, HashMap<String,String> param, Object body,
                            HashMap<String,String> headerParam, Handler<HttpResponse<Buffer>> handler){
        HttpRequest<Buffer> bufferHttpRequest = client
                .post(uri);
        initGetAndHeaderParam(param, headerParam, bufferHttpRequest);
        bufferHttpRequest.sendJson(body)
                .onSuccess(response -> handler.handle(response))
                .onFailure(err -> handler.handle(null));
    }

    //表单请求
    public static void form(String uri, HashMap<String,String> param, HashMap<String,String> headerParam,
                            HashMap<String,String> formParam, Handler<HttpResponse<Buffer>> handler){
        HttpRequest<Buffer> bufferHttpRequest = client.post(uri);
        initGetAndHeaderParam(param, headerParam, bufferHttpRequest);
        MultiMap form = MultiMap.caseInsensitiveMultiMap();
        Set<Map.Entry<String, String>> entries = formParam.entrySet();
        for(Map.Entry<String, String> entry : entries){
            form.set(entry.getKey(), entry.getValue());
        }
        bufferHttpRequest.sendForm(form)
                .onSuccess(response -> handler.handle(response))
                .onFailure(err -> handler.handle(null));
    }

    private static void initGetAndHeaderParam(HashMap<String, String> param, HashMap<String, String> headerParam, HttpRequest<Buffer> bufferHttpRequest) {
        for (Map.Entry<String, String> entry : param.entrySet()) {
            bufferHttpRequest.addQueryParam(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : headerParam.entrySet()) {
            bufferHttpRequest.putHeader(entry.getKey(), entry.getValue());
        }
    }


}
