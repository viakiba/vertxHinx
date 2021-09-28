package com.ohayoo.whitebird.enums;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-09-24
 */
public enum CommonFlagType {
    TWO_DAY_BONUS(1), // 2 的 n次方
    ;

    private long value;

    CommonFlagType(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }


}
