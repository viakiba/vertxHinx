package com.ohayoo.whitebird.schedule;

import com.ohayoo.whitebird.compoent.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Exception;
import java.util.concurrent.ScheduledFuture;

public abstract class ScheduleTask implements Runnable {
    private static Logger log = LoggerFactory.getLogger(ScheduleTask.class);

    public static final int TASK_STATE_INITIALIZED = 0; // 消息初始化
    public static final int TASK_STATE_WAITING = 1; // 消息投递完成
    public static final int TASK_STATE_RUN = 2; // 消息执行中
    public static final int TASK_STATE_CANCELED = 3; // 已经被取消
    public static final int TASK_STATE_DONE = 4 ;// 执行完毕

    private int state = TASK_STATE_INITIALIZED ;//任务状态
    private long createTimestamp = TimeUtil.currentSystemTime(); //任务创建时间
    private long triggerTimestamp = 0L; //任务执行时间
    private ScheduledFuture future;

    @Override
    public void run() {
        if(state == TASK_STATE_CANCELED){
            return;
        }
        state = TASK_STATE_RUN;
        triggerTimestamp = TimeUtil.currentSystemTime();
        //逻辑
        try {
            doTask();
        }catch (Exception e){
            log.error("定时任务执行出现异常 ",e);
        }
        state = TASK_STATE_DONE;
    }

    /**
     * 执行任务
     */
    public abstract void doTask();

    /**
     * 取消任务
     */
    public boolean doCancel(){
        if(state > TASK_STATE_WAITING){
            return false;
        }
        future.cancel(false);
        state = TASK_STATE_CANCELED;
        return true;
    }

    public long getCreateTimestamp(){
        return createTimestamp;
    }

    public long getTriggerTimestamp(){
        return triggerTimestamp;
    }

    /**
     * 获取任务状态
     */
    public int getState(){
        return state;
    }

    public void setState(int s){
        state = s;
    }

    public void setFuture( ScheduledFuture f){
        future = f;
    }



}