package com.ohayoo.whitebird.net;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.incubator.codec.quic.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 *  依赖实现:  https://github.com/netty/netty-incubator-codec-quic
 *  QUIC 介绍: https://zh.wikipedia.org/wiki/QUIC
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class TestQuicServer {
    public static void main(String[] args) throws Exception {
        //配置参数
        String configJson = "{\n" +
                "  \"port\": 8081,\n" +
                "  \"host\": \"10.79.19.90\",\n" +
                "  \"eventLoopThreadNum\": 3,\n" +
                "  \"idleTimeoutMs\": 3000,\n" +
                "  \"maxData\": 100000,\n" +
                "  \"maxStreamDataBidirectional\": 100000,\n" +
                "  \"maxStreamsBidirectional\": 100\n" +
                "}";
        JSONObject config = JSONObject.parseObject(configJson, JSONObject.class);
        SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
        QuicSslContext context = QuicSslContextBuilder.forServer(
                        selfSignedCertificate.privateKey(), null, selfSignedCertificate.certificate())
                .applicationProtocols("http/0.9").build();
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ChannelHandler codec = new QuicServerCodecBuilder().sslContext(context)
                .maxIdleTimeout(config.getIntValue("idleTimeoutMs"), TimeUnit.MILLISECONDS)
                // Configure some limits for the maximal number of streams (and the data) that we want to handle.
                .initialMaxData(config.getIntValue("maxData"))
                .initialMaxStreamDataBidirectionalLocal(config.getIntValue("maxStreamDataBidirectional"))
                .initialMaxStreamDataBidirectionalRemote(config.getIntValue("maxStreamDataBidirectional"))
                .initialMaxStreamsBidirectional(config.getIntValue("maxStreamsBidirectional"))
                .initialMaxStreamsUnidirectional(config.getIntValue("maxStreamsBidirectional"))
                // Setup a token handler. In a production system you would want to implement and provide your custom
                // one.
                .tokenHandler(InsecureQuicTokenHandler.INSTANCE)
                // ChannelHandler that is added into QuicChannel pipeline.
                .handler(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) {
                        QuicChannel channel = (QuicChannel) ctx.channel();
                        // Create streams etc..
                        log.info("已经连接");
                    }
                    public void channelInactive(ChannelHandlerContext ctx) {
                        ((QuicChannel) ctx.channel()).collectStats().addListener(f -> {
                            if (f.isSuccess()) {
                                log.info("连接关闭: {}", f.getNow());
                            }
                        });
                    }
                    @Override
                    public boolean isSharable() {
                        return true;
                    }
                })
                .streamHandler(new ChannelInitializer<QuicStreamChannel>() {
                    @Override
                    protected void initChannel(QuicStreamChannel ch) {
                        // Add a LineBasedFrameDecoder here as we just want to do some simple HTTP 0.9 handling.
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                        ByteBuf byteBuf = (ByteBuf) msg;
                                        try {
                                            String reqMsg = "GET /";
                                            String respMsg = "Hello World!\r\n";
                                            if (byteBuf.toString(CharsetUtil.US_ASCII).trim().equals(reqMsg)) {
                                                ByteBuf buffer = ctx.alloc().directBuffer();
                                                buffer.writeCharSequence(respMsg, CharsetUtil.US_ASCII);
                                                // Write the buffer and shutdown the output by writing a FIN.
                                                ctx.writeAndFlush(buffer).addListener(QuicStreamChannel.SHUTDOWN_OUTPUT);
                                            }
                                        } finally {
                                            byteBuf.release();
                                        }
                                    }
                                });
                    }
                }).build();

        try {
            Bootstrap bs = new Bootstrap();
            Channel channel = bs.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(codec)
                    .bind(new InetSocketAddress(config.getIntValue("port"))).sync().channel();
            channel.closeFuture().syncUninterruptibly();
        } finally {
            group.shutdownGracefully();
        }
    }


}
