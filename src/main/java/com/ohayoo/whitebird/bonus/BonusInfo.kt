package com.ohayoo.whitebird.bonus

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.bonus.holder.*
import com.ohayoo.whitebird.compoent.LoggerUtil
import com.ohayoo.whitebird.enums.ItemType
import com.ohayoo.whitebird.excel.model.XlsItem
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage

class BonusInfo {
    lateinit var bonusHolder: BonusHolder

    var bonusId: Int
    var bonusType: ItemType
    var bonusNum: Int

    /**
     * 掉落包构造函数
     * @param bonusId
     */
    constructor(bonusId: Int, num: Int) {
        this.bonusId = bonusId
        this.bonusType = patchBonusType(bonusId)
        bonusNum = num
        patch()
    }

    private fun patchBonusType(bonusId: Int): ItemType {
        if(bonusId.toString().startsWith("113")){
            return ItemType.DROP
        }
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusId)
        if(xlsItem == null){
            throw CustomException(CommonMessage.ErrorCode.ExcelDataError)
        }
        return ItemType.values()[xlsItem.type]
    }

    /**
     * 掉落包构造函数
     * @param bonusId
     */
    constructor(bonusId: Int, bonusType: ItemType) {
        this.bonusId = bonusId
        this.bonusType = bonusType
        bonusNum = 1
        patch()
    }

    constructor(bonusId: Int, bonusType: ItemType, num: Int) {
        this.bonusId = bonusId
        this.bonusType = bonusType
        bonusNum = num
        patch()
    }

    private fun patch() {
        if (bonusType == ItemType.DROP) {
            bonusType = ItemType.DROP
            bonusHolder = IDropDataHolder;
            return
        }
        if (bonusType == ItemType.CURRENCY) {
            bonusType = ItemType.CURRENCY
            bonusHolder = ICurrencyDataHolder;
            return
        }
        if (bonusType == ItemType.CARD) {
            bonusType = ItemType.CARD
            bonusHolder = ICardDataHolder;
            return
        }
        if (bonusType == ItemType.CARD_PRICE) {
            bonusType = ItemType.CARD_PRICE
            bonusHolder = ICardPriceDataHolder;
            return
        }
        if (bonusType == ItemType.TOWER_STUFF) {
            bonusType = ItemType.TOWER_STUFF
            bonusHolder = ITowerStuffDataHolder;
            return
        }
        if (bonusType == ItemType.SKILL) {
            bonusType = ItemType.SKILL
            bonusHolder = ISkillDataHolder;
            return
        }
        if (bonusType == ItemType.SKILL_PRICE) {
            bonusType = ItemType.SKILL_PRICE
            bonusHolder = ISkillPriceDataHolder;
            return
        }
        LoggerUtil.error("未知物品类型 $bonusId")
    }

}