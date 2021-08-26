package com.ohayoo.whitebird.player.model;

import com.google.protobuf.Message;
import com.ohayoo.whitebird.player.enums.AttributeEnum;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public interface IPlayer {

    void setAttribute(AttributeEnum key, Object attach);

    <T>T getAttribute(AttributeEnum key);

    void send(Object msg,int msgId);

}
