package com.ohayoo.whitebird.net;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import org.testng.annotations.Test;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
public class TestUdpClient {
    @Test
    public void test0() {
        Vertx vertx = Vertx.vertx();
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
