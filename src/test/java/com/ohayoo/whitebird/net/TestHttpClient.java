package com.ohayoo.whitebird.net;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.client.HttpRequest;
import io.vertx.rxjava3.ext.web.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
public class TestHttpClient {

    public  void test() {
        Vertx vertx = Vertx.vertx();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        WebClientOptions options = new WebClientOptions()
                .setUserAgent("My-App/1.2.3")
                ;
        WebClient client = WebClient.create(vertx);
        options.setKeepAlive(false);
        Map map = new HashMap();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            vertx.setPeriodic(1,handler->{
                HttpRequest<Buffer> bufferHttpRequest = client
                        .get(10001,"10.79.19.67","/handler");
                bufferHttpRequest.headers().add("header_message_id","10000");
                bufferHttpRequest
                        .sendJson(map)
                        .subscribe(res ->{
                            atomicInteger.incrementAndGet();
                        },error->{
                            System.out.println("error ");
                        })
                ;
            });
        }
        vertx.setPeriodic(1000,handler -> {
            long l1 = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() +"速率 "+atomicInteger.get() / ((l1 - l)/1000));
        });
    }
}
