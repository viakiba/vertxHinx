package com.ohayoo.whitebird.schedule;

import java.util.*;

interface IScheduleService {
    /**
     *
     * @param msg
     * @param d
     * 指定日期
     */
    public ScheduleTask scheduleOnce(ScheduleTask msg, Date d);

    /**
     *
     * @param msg
     * @param delay
     * 延迟时间(单位:豪秒)
     */
    public ScheduleTask scheduleOnce(ScheduleTask msg, long delay);

    /**
     *
     * @param msg
     * @param delay
     * 延迟时间
     * @param period
     * 周期时间(单位:豪秒)
     */
    public ScheduleTask scheduleWithFixedDelay(ScheduleTask  msg, long delay, long period);
}