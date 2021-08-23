package com.ohayoo.whitebird.schedule;

import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.compoent.TimeUtil;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleSystemService implements IScheduleService, SystemServiceImpl {

    public ScheduledExecutorService scheduledExecutorService ;

    @Override
    public ScheduleTask scheduleOnce(ScheduleTask scheduleTask, Date d) {
        scheduleTask.setState(ScheduleTask.TASK_STATE_WAITING);
        ScheduledFuture schedule = scheduledExecutorService.schedule(
            scheduleTask, d.getTime() - TimeUtil.currentSystemTime(),
            TimeUnit.MILLISECONDS
        );
        scheduleTask.setFuture(schedule);
        return scheduleTask;
    }

    @Override
    public ScheduleTask scheduleOnce(ScheduleTask scheduleTask, long delay) {
        scheduleTask.setState(ScheduleTask.TASK_STATE_WAITING);
        ScheduledFuture schedule = scheduledExecutorService.schedule(scheduleTask, delay, TimeUnit.MILLISECONDS);
        scheduleTask.setFuture(schedule);
        return scheduleTask;
    }

    @Override
    public ScheduleTask  scheduleWithFixedDelay(ScheduleTask scheduleTask, long delay, long period) {
        scheduleTask.setState(ScheduleTask.TASK_STATE_WAITING);
        ScheduledFuture schedule = scheduledExecutorService.scheduleAtFixedRate(
            scheduleTask, delay, period, TimeUnit.MILLISECONDS);
        scheduleTask.setFuture(schedule);
        return scheduleTask;
    }


    @Override
    public void start() throws IOException {
        scheduledExecutorService =  Executors.newScheduledThreadPool(1);
    }

    @Override
    public void stop() {
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
        }
    }
}