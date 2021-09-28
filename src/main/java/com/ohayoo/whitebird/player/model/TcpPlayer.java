package com.ohayoo.whitebird.player.model;

import com.google.protobuf.Message;
import com.ohayoo.whitebird.compoent.JsonUtil;
import com.ohayoo.whitebird.player.PlayerSystemService;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
@Slf4j
public class TcpPlayer implements IPlayer {
    private Map<AttributeEnum, Object> attribute = new ConcurrentHashMap<>(8);

    @Override
    public void setAttribute(AttributeEnum key, Object attach) {
        attribute.put(key,attach);
    }

    @Override
    public <T> T getAttribute(AttributeEnum key) {
        return (T)attribute.get(key);
    }

    @Override
    public void send(Object msg,int msgId) {
        NetSocket socket = getAttribute(AttributeEnum.link);
        Buffer buffer = Buffer.buffer();
        if(msg instanceof Message){
            PlayerSystemService.writeMessageInfo(buffer,((Message) msg).toByteArray(),msgId);
        }else{
            PlayerSystemService.writeMessageInfo(buffer, JsonUtil.obj2Str(msg).getBytes(StandardCharsets.UTF_8),msgId);
        }
        socket.write(buffer);
    }
}
