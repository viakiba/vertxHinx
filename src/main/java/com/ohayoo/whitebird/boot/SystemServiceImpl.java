package com.ohayoo.whitebird.boot;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public interface SystemServiceImpl {

    void start();

    default void stop(){}

}
