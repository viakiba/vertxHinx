package com.ohayoo.whitebird.bonus.reason


enum class BonusReason(var currencyReason: CurrencyReason, var itemReason: ItemReason) {
    NULL(CurrencyReason.NULL, ItemReason.NULL),  //未知原因
    GM(CurrencyReason.GM, ItemReason.GM);

}