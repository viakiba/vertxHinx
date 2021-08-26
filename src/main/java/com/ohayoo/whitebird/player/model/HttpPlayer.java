package com.ohayoo.whitebird.player.model;

import com.ohayoo.whitebird.player.enums.AttributeEnum;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
@Slf4j
public class HttpPlayer implements IPlayer{

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
        HttpServerRequest request = getAttribute(AttributeEnum.link);
        request.response().end(msgId+String.valueOf(msg));
    }
}
