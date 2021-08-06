package com.ohayoo.whitebird.net;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import org.testng.annotations.Test;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
public class TestWebSocketClient{
    @Test
    public void test0() {
        Vertx vertx = Vertx.vertx();
        for (int i = 0; i < 30; i++) {
            HttpClient client = vertx.createHttpClient(new HttpClientOptions()
                    .setDefaultHost("127.0.0.1")
                    .setDefaultPort(8080)
                    .setSsl(false));
            client.webSocket("/", ws -> {
                WebSocket result = ws.result();
                result.handler(buffer -> {
                    int msgId = buffer.getInt(0);
                    String body = buffer.getString(4,buffer.length());
                    System.out.println(msgId + body + Thread.currentThread().getName());
                });
                vertx.setPeriodic(1000,handler ->{
                    Buffer buffer = Buffer.buffer();
                    buffer.appendInt(10000);
                    buffer.appendString("{\"xx\":\"111\"}");
                    result.write(buffer,handler1->{
                        if(handler1.succeeded() ){
                            System.out.println(handler1.succeeded() + Thread.currentThread().getName());
                        }else{
                            System.out.println(handler1.cause().getMessage());
                        }
                    });
                });
            });
        }
    }
}
