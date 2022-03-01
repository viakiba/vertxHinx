package io.github.viakiba.hinx.net;

import io.github.viakiba.hinx.generate.message.CommonMessage;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.client.HttpRequest;
import io.vertx.rxjava3.ext.web.client.WebClient;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-27
 */
public class TestHttpClient {

    @Test
    public  void test0() {
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
                        .get(10001,"127.0.0.1","/handler");
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

    @Test
    public  void test1() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        WebClientOptions options = new WebClientOptions()
                .setUserAgent("My-App/1.2.3")
                ;
        WebClient client = WebClient.create(vertx);
        options.setKeepAlive(false);
        CommonMessage.PBGmCmdUp.Builder builder = CommonMessage.PBGmCmdUp.newBuilder();
        builder.setCmd(1111);
        CommonMessage.PBGmCmdUp build = builder.build();
        long l = System.currentTimeMillis();
        HttpRequest<Buffer> bufferHttpRequest = client
                .get(10001,"127.0.0.1","/handler");
        bufferHttpRequest.headers().add("header_message_id","10001");
        bufferHttpRequest
                .sendBuffer(Buffer.buffer(build.toByteArray()))
                .subscribe(res ->{
                    atomicInteger.incrementAndGet();
                },error->{
                    System.out.println("error ");
                });
        Thread.sleep(1000000);
    }
}
