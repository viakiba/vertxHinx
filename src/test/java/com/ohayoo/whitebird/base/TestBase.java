package com.ohayoo.whitebird.base;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.config.ServerSystemConfig;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-28
 */
public abstract class TestBase {
    public static void init() {
        ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
        GlobalContext.addSystemService(serverSystemConfig);
        serverSystemConfig.start();
        GlobalContext.initVertx();
    }
}
