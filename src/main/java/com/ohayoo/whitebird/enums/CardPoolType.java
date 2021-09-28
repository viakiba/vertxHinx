package com.ohayoo.whitebird.enums;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-09-13
 */
public enum CardPoolType {
    ALL_CARD_VERSION(1),
    PLAYER_CARD(2),
    ONLY_PLAYER_CARD(3),
    ;

    private int value;

    CardPoolType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
