package io.github.viakiba.hinx.net;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import org.testng.annotations.Test;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-27
 */
public class TestUdpClient {
    @Test
    public void test0() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
        vertx.setPeriodic(1000,h ->{
            Buffer buffer = Buffer.buffer();
            buffer.appendString("111111");
            // Send a Buffer
            socket.send(buffer, 8081, "10.79.19.90", asyncResult -> {
                System.out.println("Send succeeded? " + asyncResult.succeeded());
            });
        });
        socket.handler(x ->{
            Buffer data = x.data();
            String string = data.getString(0, data.length());
            System.out.println("收到"+string);
        });
        Thread.sleep(10000);
    }
}
