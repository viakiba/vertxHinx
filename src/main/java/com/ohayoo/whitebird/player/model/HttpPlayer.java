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

    private Map<String , Object> attribute = new ConcurrentHashMap<>(8);

    @Override
    public void setAttribute(String key, Object attach) {
        if(StringUtils.isEmpty(key)){
            log.error("key is empty!");
        }
        attribute.put(key,attach);
    }

    @Override
    public <T> T getAttribute(String key) {
        if(StringUtils.isEmpty(key)){
            log.error("key is empty!");
        }
        return (T)attribute.get(key);
    }

    @Override
    public void send(Object msg,int msgId) {
        HttpServerRequest request = getAttribute(AttributeEnum.link.name());
        request.response().end(msgId+String.valueOf(msg));
    }
}
