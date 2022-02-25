package io.github.viakiba.hinx.service.abs;

import com.google.protobuf.Message;

import java.util.Map;

/**
 * @createTime 2021-07-27
 */
public interface IMessage {

    Integer getMinMessageId();

    Integer getMaxMessageId();

}
