package io.github.viakiba.hinx.net;

import cn.hutool.core.lang.Tuple;
import io.github.viakiba.hinx.player.PlayerSystemService;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-27
 */
public class TestTcpClient {

    @Test
    public void test0() {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        netClient.connect(8080,"127.0.0.1",x ->{
            NetSocket result = x.result();
            result.handler(buffer -> {
               Tuple tuple = PlayerSystemService.readMessageInfo(buffer);
               System.out.println(new String(tuple.get(1),StandardCharsets.UTF_8));
            });
            vertx.setPeriodic(1000,handler -> {
                Buffer buffer = Buffer.buffer();
                byte[] data = "{\"xx\":\"111\"}".getBytes(StandardCharsets.UTF_8);
                PlayerSystemService.writeMessageInfo(buffer,data,10000);
                result.write(buffer);
            });
        });
    }
}
