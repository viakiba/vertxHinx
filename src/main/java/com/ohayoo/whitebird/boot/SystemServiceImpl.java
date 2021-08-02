package com.ohayoo.whitebird.boot;

import java.io.IOException;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public interface SystemServiceImpl {

    void start() throws IOException;

    default void stop(){}

}
