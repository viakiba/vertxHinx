package com.ohayoo.whitebird.enums;

//货币类型
public enum ItemType {
    DROP(0),
    CURRENCY(1), //货币
    CARD(2), // 整卡
    CARD_PRICE(3),// 卡碎片
    TOWER_STUFF(4),// 基地升级材料
    SKILL(5),// 技能解锁
    SKILL_PRICE(6),// 技能碎片
    ;

    private int value;

    ItemType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}