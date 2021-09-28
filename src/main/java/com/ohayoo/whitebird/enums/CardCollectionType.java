package com.ohayoo.whitebird.enums;

public enum CardCollectionType {

    can_collection(1),
    no_collection(2),
    ;
    private int value;

    CardCollectionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
