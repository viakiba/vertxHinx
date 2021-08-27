package com.ohayoo.whitebird.net;

import cn.hutool.core.lang.Tuple;
import com.ohayoo.whitebird.base.TestBase;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.player.PlayerSystemService;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
public class TestTcpClient extends TestBase {

    @Test
    public void test0() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        netClient.connect(8080,"10.79.19.90",x ->{
            NetSocket result = x.result();
            result.handler(buffer -> {
               Tuple tuple = PlayerSystemService.readMessageInfo(buffer);
               System.out.println(new String(tuple.get(1),StandardCharsets.UTF_8));
            });
            vertx.setPeriodic(100,handler -> {
                Buffer buffer = Buffer.buffer();
                byte[] data = ("{\"xx\":\""+atomicInteger.incrementAndGet()+"\"}").getBytes(StandardCharsets.UTF_8);
                PlayerSystemService.writeMessageInfo(buffer,data,10000);
                result.write(buffer);
            });
        }
        );
        Thread.sleep(1000000L);
    }
}
