package io.github.viakiba.hinx.schedule;

import io.github.viakiba.hinx.compoent.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import java.lang.Exception;
import java.util.concurrent.ScheduledFuture;

@Slf4j
public abstract class ScheduleTask implements Runnable {

    public static int TASK_STATE_INITIALIZED = 0; // 消息初始化
    public static int TASK_STATE_WAITING = 1; // 消息投递完成
    public static int TASK_STATE_RUN = 2; // 消息执行中
    public static int TASK_STATE_CANCELED = 3; // 已经被取消
    public static int TASK_STATE_DONE = 4 ;// 执行完毕

    private static int state = TASK_STATE_INITIALIZED ;//任务状态
    private static long createTimestamp = TimeUtil.currentSystemTime(); //任务创建时间
    private static long triggerTimestamp = 0L; //任务执行时间
    private static ScheduledFuture future;

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