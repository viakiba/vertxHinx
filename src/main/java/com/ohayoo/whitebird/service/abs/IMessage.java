package com.ohayoo.whitebird.service.abs;

import com.google.protobuf.Message;

import java.util.Map;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-27
 */
public interface IMessage {

    Integer getMinMessageId();

    Integer getMaxMessageId();

}
