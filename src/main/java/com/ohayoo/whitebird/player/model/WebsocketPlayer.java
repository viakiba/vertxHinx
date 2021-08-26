package com.ohayoo.whitebird.player.model;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
@Slf4j
public class WebsocketPlayer implements IPlayer {
    private Map<AttributeEnum , Object> attribute = new ConcurrentHashMap<>(8);

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
        ServerWebSocket o = (ServerWebSocket)attribute.get(AttributeEnum.link);
        Buffer buffer = Buffer.buffer();
        if(msg instanceof Message){
            buffer.appendInt(msgId).appendBytes(((Message) msg).toByteArray());
        }else{
            buffer.appendInt(msgId).appendString(JSONObject.toJSONString(msg));
        }
        o.write(buffer);
    }

}
