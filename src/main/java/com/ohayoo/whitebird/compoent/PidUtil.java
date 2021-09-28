package com.ohayoo.whitebird.compoent;

import java.lang.management.ManagementFactory;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-09-13
 */
public class PidUtil {

    public static int getPid() {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            return Integer.parseInt(jvmName.split("@")[0]);
        }
        catch (Exception ex) {
            LoggerUtil.error(ex.getMessage());
            return RandomUtil.regionRandom(0,32);
        }
    }

    public static void main(String[] args) {
        LoggerUtil.info(getPid()+"");
    }

}
