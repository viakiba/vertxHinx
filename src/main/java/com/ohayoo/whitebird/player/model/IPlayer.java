package com.ohayoo.whitebird.player.model;

import com.ohayoo.whitebird.player.enums.AttributeEnum;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public interface IPlayer {

    void setAttribute(AttributeEnum key, Object attach);

    <T>T getAttribute(AttributeEnum key);

    void send(Object msg,int msgId);

    default void sendError(int msgCode,int msgId){
        throw new RuntimeException("未实现");
    };

    default <T> T  getData(){
        return getAttribute(AttributeEnum.data);
    }
}
