package com.ohayoo.whitebird.enums;

public enum CardRarityType {
    PU_TONG(1),
    XI_YOU(2),
    SHI_SHI(3),
    CHUAN_QI(4),
    ;

    private int value;

    CardRarityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
