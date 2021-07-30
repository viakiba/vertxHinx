package com.ohayoo.whitebird.net;

import cn.hutool.core.lang.Tuple;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import com.ohayoo.whitebird.player.PlayerSystemService;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.nio.charset.StandardCharsets;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
public class TestTcpClient {
    public static Vertx vertx = Vertx.vertx();

    static {
        ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
        GlobalContext.addSystemService(serverSystemConfig);
        serverSystemConfig.start();

    }
    public static void main(String[] args) {
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
