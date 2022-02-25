package io.github.viakiba.hinx.network.verticle;

import com.alibaba.fastjson.JSONObject;
import com.backblaze.erasure.fec.Snmp;
import io.github.viakiba.hinx.enums.NetType;
import io.netty.buffer.ByteBuf;
import io.vertx.core.AbstractVerticle;
import kcp.ChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;
import lombok.extern.slf4j.Slf4j;

/**
 *  依赖实现:  https://github.com/l42111996/java-Kcp
 *  KCP 介绍: https://zhuanlan.zhihu.com/p/11244234
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class KcpServerVerticle extends AbstractVerticle implements BaseServerVerticle, KcpListener {

    /**
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        String configJson = configJson(NetType.kcp);
        JSONObject jsonObject = JSONObject.parseObject(configJson, JSONObject.class);
        ChannelConfig channelConfig = JSONObject.parseObject(configJson, ChannelConfig.class);
        KcpServerVerticle kcpServerVerticle = new KcpServerVerticle();
        KcpServer kcpServer = new KcpServer();
        kcpServer.init(kcpServerVerticle,channelConfig,jsonObject.getIntValue("port"));
        log.info("KcpServer Init Success");
    }

    @Override
    public void onConnected(Ukcp ukcp) {
        log.debug("有连接进来"+ukcp.user().getRemoteAddress());
    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        byte[] bytes = new  byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(),bytes);
        if(log.isDebugEnabled()) {
            log.debug("收到消息: " + new String(bytes));
        }
        ukcp.write(byteBuf);
    }

    @Override
    public void handleException(Throwable throwable, Ukcp ukcp) {
        log.info("连接异常断开 "+ukcp.getConv(),throwable);
    }

    @Override
    public void handleClose(Ukcp ukcp) {
        if(log.isDebugEnabled()) {
            log.debug(Snmp.snmp.toString());
            Snmp.snmp = new Snmp();
        }
    }
}
