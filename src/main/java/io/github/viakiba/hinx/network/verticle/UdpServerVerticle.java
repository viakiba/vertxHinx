package io.github.viakiba.hinx.network.verticle;

import io.github.viakiba.hinx.compoent.LocalIpUtil;
import io.github.viakiba.hinx.enums.NetType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class UdpServerVerticle extends AbstractVerticle implements BaseServerVerticle {

    private DatagramSocket socket;

    /**
     * UDP 使用起来不如 TCP 安全，这意味着根本无法保证发送数据报数据包会收到它的端点。
     * 唯一的保证是它要么接收完整，要么根本不接收。
     * 此外，您通常无法发送大于网络接口 MTU 大小的数据，这是因为每个数据包都将作为一个数据包发送。
     * 但是，请注意，即使数据包大小小于 MTU，它也可能会失败。
     * 它会失败的大小取决于操作系统等。所以经验法则是尝试发送小数据包。
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        JsonObject jsonObject = new JsonObject(configJson(NetType.udp));
        DatagramSocketOptions options = new DatagramSocketOptions(jsonObject);
        DatagramSocket socket = vertx.createDatagramSocket(options);
        socket.listen(jsonObject.getInteger("port",8080),jsonObject.getString("host", LocalIpUtil.get10BeginIp()), asyncResult -> this.handler(asyncResult ,socket));
        log.info("UdpServer Init Success");
    }

    private void handler(AsyncResult<DatagramSocket> datagramSocketAsyncResult,DatagramSocket socket) {
        if(!datagramSocketAsyncResult.succeeded()){
            log.error("Listen failed" + datagramSocketAsyncResult.cause());
        }
        socket.handler(packet -> {
            SocketAddress sender = packet.sender();
            socket.send("xxx"+ (new Random()).nextInt(19000),sender.port(),sender.host());
            Buffer data = packet.data();
            String string = data.getString(0, data.length());
            System.out.println(string);
        });
    }
}
