package com.ohayoo.whitebird.boot;

import java.io.IOException;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public interface SystemServiceImpl {

    void startService() throws IOException;

    default void stopService(){}

}
