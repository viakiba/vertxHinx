package com.ohayoo.whitebird.enums;

public enum CurrencyType {

    COIN(114010001),
    BAO_SHI(114010002),
    TI_LI(114010003),
    ;

    private int value;

    CurrencyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
