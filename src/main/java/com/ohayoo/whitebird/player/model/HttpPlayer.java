package com.ohayoo.whitebird.player.model;

import com.google.protobuf.GeneratedMessage;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.compoent.JsonUtil;
import com.ohayoo.whitebird.compoent.TimeUtil;
import com.ohayoo.whitebird.enums.MessageType;
import com.ohayoo.whitebird.generate.message.CommonMessage;
import com.ohayoo.whitebird.player.PlayerSystemService;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
public class HttpPlayer implements IPlayer{
    private static Logger log = LoggerFactory.getLogger(HttpPlayer.class);
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
        long endTime = TimeUtil.currentSystemTime();
        HttpServerRequest request = getAttribute(AttributeEnum.link);
        HttpServerResponse response = request.response();
        response.putHeader(CommonMessage.HeaderType.response_message_id.name(),String.valueOf(msgId));
        response.putHeader(CommonMessage.HeaderType.status_code.name(),String.valueOf(CommonMessage.ErrorCode.SUCCESS_VALUE));
        response.putHeader(CommonMessage.HeaderType.response_server_time.name(),String.valueOf(endTime));
        long beginTime = Long.parseLong(response.headers().get(CommonMessage.HeaderType.collect_time.name()));
        long doTime = endTime - beginTime;
        response.putHeader(CommonMessage.HeaderType.collect_time.name(),String.valueOf(doTime));
        if(doTime > 200){
            log.warn("业务处理过久" + doTime);
        }
        log.info("业务处理 === " + doTime);
        if(GlobalContext.serverConfig().getMsgType() == MessageType.proto){
            PlayerSystemService.printMessage(msg);
            response.end(Buffer.buffer ( ((GeneratedMessage)msg).toByteArray()) ,h -> {});
        }else{
            response.end(JsonUtil.obj2Str(msg),h ->{});
        }
    }

    @Override
    public void sendError(int msgCode, int msgId) {
        long endTime = TimeUtil.currentSystemTime();
        HttpServerRequest request = getAttribute(AttributeEnum.link);
        HttpServerResponse response = request.response();

        response.putHeader(CommonMessage.HeaderType.response_message_id.name(),String.valueOf(msgId));
        response.putHeader(CommonMessage.HeaderType.status_code.name(),String.valueOf(msgCode));
        response.putHeader(CommonMessage.HeaderType.response_server_time.name(),String.valueOf(endTime));
        String s = response.headers().get(CommonMessage.HeaderType.collect_time.name());
        if(s!=null) {
            long beginTime = Long.parseLong(s);
            long doTime = endTime - beginTime;
            response.putHeader(CommonMessage.HeaderType.collect_time.name(), String.valueOf(doTime));
            if(doTime > 200){
                log.warn("业务处理过久" + doTime);
            }
        }
        response.end("",h ->{});
    }
}
