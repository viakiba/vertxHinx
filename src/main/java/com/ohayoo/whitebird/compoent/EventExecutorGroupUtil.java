package com.ohayoo.whitebird.compoent;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-25
 */
public class EventExecutorGroupUtil {
    private EventExecutor[] executors;
    private EventExecutor defaultExecutor = new DefaultEventExecutor();

    public EventExecutorGroupUtil(int nthreads) {
        executors = new EventExecutor[nthreads];
        for (int i = 0; i < nthreads; i++) {
            executors[i] = new DefaultEventExecutor();
        }
    }

    public EventExecutor getEventExecutor(Long userId) {
        if(userId == null){
            return defaultExecutor;
        }
        int index = (int) (userId % executors.length);
        return executors[index];
    }
}
