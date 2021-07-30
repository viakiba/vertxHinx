package com.ohayoo.whitebird.message;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.enums.MessageType;
import com.ohayoo.whitebird.exception.CustomException;
import com.ohayoo.whitebird.player.model.IPlayer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class MsgSystemService implements SystemServiceImpl {

    public static MsgHandler msgHandler ;

    @Override
    public void start() {
        MessageType msgType = GlobalContext.serverConfig().getMsgType();
        if(msgType == MessageType.json) {
            msgHandler = new JsonMsgHandler();
        }
        if(msgType == MessageType.proto) {
            msgHandler = new ProtoMsgHandler();
        }
        msgHandler.init();
    }

    public void handler(IPlayer player, Integer msgId, byte[] body) {
        GlobalContext.getVertx().executeBlocking(handler->{
           try {
               msgHandler.handler(player,msgId,body);
           } catch (Exception e) {
               //TODO 异常捕获处理
               if(e instanceof CustomException){
                   log.error("未知异常",((CustomException) e).getStatusCode());

                   return;
               }else{
                   log.error("未知异常",e);

               }
           }
        },false);
    }
}
