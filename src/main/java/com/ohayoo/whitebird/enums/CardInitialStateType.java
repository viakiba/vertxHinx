package com.ohayoo.whitebird.enums;

public enum CardInitialStateType {

    NO_GIVE(0),
    GIVE(1),
    GIVE_UP(2),
    ;

    private int value;

    CardInitialStateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
