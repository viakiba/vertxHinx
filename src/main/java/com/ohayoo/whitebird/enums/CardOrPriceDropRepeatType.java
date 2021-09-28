package com.ohayoo.whitebird.enums;

public enum CardOrPriceDropRepeatType {

    REMOVE_REPEAT(1),
    REPEAT(2),
    ;

    private int value;

    CardOrPriceDropRepeatType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
