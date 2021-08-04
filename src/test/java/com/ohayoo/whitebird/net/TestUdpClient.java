package com.ohayoo.whitebird.net;

import cn.hutool.core.lang.Tuple;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import com.ohayoo.whitebird.player.PlayerSystemService;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.nio.charset.StandardCharsets;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
public class TestUdpClient {
    public static Vertx vertx = Vertx.vertx();

    static {
        ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
        GlobalContext.addSystemService(serverSystemConfig);
        serverSystemConfig.start();

    }
    public static void main(String[] args) {
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
        vertx.setPeriodic(1000,h ->{
            Buffer buffer = Buffer.buffer();
            buffer.appendString("111111");
            // Send a Buffer
            socket.send(buffer, 8081, "10.79.19.67", asyncResult -> {
                System.out.println("Send succeeded? " + asyncResult.succeeded());
            });
        });

    }
}
