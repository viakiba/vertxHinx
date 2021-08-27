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
public class TcpServerVerticle extends AbstractVerticle implements BaseServerVerticle {

    private NetServer server;

    @Override
    public void start() throws Exception {
        NetServerOptions options = new NetServerOptions(new JsonObject(configJson(NetType.tcp)));
        NetServer server = vertx.createNetServer(options);
        server.connectHandler(this::handler);
        server.listen();
        log.info("TcpServer Init Success");
    }

    private void handler(NetSocket netSocket) {
        NetSocketInternal soi = (NetSocketInternal)netSocket ;
        ChannelPipeline pipeline = soi.channelHandlerContext().pipeline();

        LengthFieldBasedFrameDecoder decoder ;
        if(GlobalContext.serverConfig().isByteOrder()) {
            decoder = new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE,
                    0, 4, -4, 0, true) {
                /**
                 * 小端  最大长度不限
                 * [2-3]4位为长度头
                 * [4]由于总长度包含头长度，所以需要进行修正 lengthAdjustment + 数据长度取值 = 数据长度字段之后剩下包的字节数
                 * 不跳过任何字节（比如头长度），客户端发什么就接什么
                 *
                 * @param ctx
                 * @param buf
                 * @return
                 * @throws Exception
                 */
                @Override
                protected Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
                    ByteBuf buffs = (ByteBuf) super.decode(ctx, buf);
                    return buffs;
                }
            };
        }else{
            decoder = new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE,
                    0, 4, -4, 0, true) {
                /**
                 * 小端  最大长度不限
                 * [2-3]4位为长度头
                 * [4]由于总长度包含头长度，所以需要进行修正 lengthAdjustment + 数据长度取值 = 数据长度字段之后剩下包的字节数
                 * 不跳过任何字节（比如头长度），客户端发什么就接什么
                 *
                 * @param ctx
                 * @param buf
                 * @return
                 * @throws Exception
                 */
                @Override
                protected Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
                    ByteBuf buffs = (ByteBuf) super.decode(ctx, buf);
                    return buffs;
                }
            };
        }

        pipeline.addBefore("handler", "DECODER",
                decoder);
        pipeline.addBefore("handler", "IDLE", new ChannelInboundHandlerAdapter() {
            /**
             * @see IdleStateHandler
             * @param channelHandlerContext
             * @param o
             * @throws Exception
             */
            @Override
            public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

            }
        });
        TcpPlayer tcpPlayer = new TcpPlayer();
        netSocket.handler(buffer -> {
            PlayerSystemService systemService = GlobalContext.getSystemService(PlayerSystemService.class);
            tcpPlayer.setAttribute(AttributeEnum.link.name(),netSocket);
            systemService.tcpHandle(buffer, tcpPlayer);
        });

        netSocket.closeHandler(handler -> {

        });

        netSocket.exceptionHandler(e -> {
            log.info("连接异常断开 ",e);
        });

    }
}
