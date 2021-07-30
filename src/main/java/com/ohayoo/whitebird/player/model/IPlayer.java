package com.ohayoo.whitebird.player.model;

import com.google.protobuf.Message;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public interface IPlayer {

    void setAttribute(String key, Object attach);

    <T>T getAttribute(String key);

    void send(Object msg,int msgId);

}
