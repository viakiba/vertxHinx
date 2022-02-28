package io.github.viakiba.hinx.message;

import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.boot.SystemServiceImpl;
import io.github.viakiba.hinx.compoent.EventExecutorGroupUtil;
import io.github.viakiba.hinx.enums.MessageType;
import io.github.viakiba.hinx.exception.CustomException;
import io.github.viakiba.hinx.player.enums.AttributeEnum;
import io.github.viakiba.hinx.player.model.IPlayer;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-23
 */
@Slf4j
public class MsgSystemService implements SystemServiceImpl {

    public static MsgHandler msgHandler ;
    public static EventExecutorGroupUtil eventExecutorGroupUtil ;

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
        eventExecutorGroupUtil = new EventExecutorGroupUtil(10);
    }

    public void handler(IPlayer player, Integer msgId, byte[] body) {
        EventExecutor eventExecutor = eventExecutorGroupUtil.getEventExecutor(player.getAttribute(AttributeEnum.id.name()));
        eventExecutor.execute(()->{
            try {
                msgHandler.handler(player,msgId,body);
            } catch (Exception e) {
                if(e instanceof CustomException){
                    log.error("未知异常",((CustomException) e).getStatusCode());
                    return;
                }else{
                    log.error("未知异常",e);
                }
            }
        });
    }
}
