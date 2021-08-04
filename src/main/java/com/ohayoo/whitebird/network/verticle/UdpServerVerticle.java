package com.ohayoo.whitebird.network.verticle;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.enums.NetType;
import com.ohayoo.whitebird.player.PlayerSystemService;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import com.ohayoo.whitebird.player.model.TcpPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.impl.NetSocketInternal;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteOrder;

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
        socket.listen(jsonObject.getInteger("port",8080),jsonObject.getString("host","127.0.0.1"),asyncResult -> this.handler(asyncResult ,socket));
        log.info("UdpServer Init Success");
    }

    private void handler(AsyncResult<DatagramSocket> datagramSocketAsyncResult,DatagramSocket socket) {
        if(!datagramSocketAsyncResult.succeeded()){
            log.error("Listen failed" + datagramSocketAsyncResult.cause());
        }
        socket.handler(packet -> {
            Buffer data = packet.data();
            String string = data.getString(0, data.length());
            System.out.println(string);
        });
    }
}
