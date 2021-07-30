package com.ohayoo.whitebird.service;

import com.google.protobuf.Message;
import com.ohayoo.whitebird.annotate.BizServiceAnnotate;
import com.ohayoo.whitebird.compoent.JsonUtil;
import com.ohayoo.whitebird.generate.message.CommonMessage;
import com.ohayoo.whitebird.player.model.IPlayer;
import com.ohayoo.whitebird.service.abs.BaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@BizServiceAnnotate
public class CommonService extends BaseService  {

    @Override
    public Integer getMinMessageId() {
        return 10000;
    }

    @Override
    public Integer getMaxMessageId() {
        return 10100;
    }

    @Override
    public Map<Integer, Class> getJsonMessageRecognize() {
        HashMap<Integer, Class> msgMap = new HashMap<>();
        msgMap.put(10000, Map.class);
        return msgMap;
    }

    @Override
    public void handler(Integer messageId, Object message, IPlayer player) throws Exception {
        if(messageId == 10000){
            login(player);
            return ;
        }
        throw new Exception("CommonService#handler 此方法为实现"+messageId);
    }

    @Override
    public Map<Integer, Message.Builder> getProtoMessageRecognize() throws Exception {
        HashMap<Integer, Message.Builder> msgMap = new HashMap<>();
        msgMap.put(10000, CommonMessage.PBGmCmdUp.newBuilder());
        return msgMap;
    }

    @Override
    public void handler(Integer messageId, Message message, IPlayer player) throws Exception {
        if(messageId == 10000){
            login(player);
            return ;
        }
        throw new Exception("CommonService#handler 此方法为实现"+messageId);
    }

    private void login( IPlayer player) {
        player.send("xxxxxx",1);
    }

}
