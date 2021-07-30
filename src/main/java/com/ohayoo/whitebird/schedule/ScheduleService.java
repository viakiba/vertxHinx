package com.ohayoo.whitebird.schedule;

import com.ohayoo.whitebird.compoent.TimeUtil;

import java.util.concurrent.Executors;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class ScheduleService implements IScheduleService {

    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public ScheduleTask scheduleOnce(ScheduleTask scheduleTask, Date d) {
        scheduleTask.setState(ScheduleTask.TASK_STATE_WAITING);
        var schedule = scheduledExecutorService.schedule(
            scheduleTask, d.getTime() - TimeUtil.currentSystemTime(),
            TimeUnit.MILLISECONDS
        );
        scheduleTask.setFuture(schedule);
        return scheduleTask;
    }

    @Override
    public ScheduleTask scheduleOnce(ScheduleTask scheduleTask, long delay) {
        scheduleTask.setState(ScheduleTask.TASK_STATE_WAITING);
        var schedule = scheduledExecutorService.schedule(scheduleTask, delay, TimeUnit.MILLISECONDS);
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


}