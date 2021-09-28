package com.ohayoo.whitebird.bonus.holder

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.bonus.BonusService
import com.ohayoo.whitebird.data.model.gamedata.CardData
import com.ohayoo.whitebird.enums.ItemType
import com.ohayoo.whitebird.excel.model.XlsItem

object ICardDataHolder : BonusHolder {

    override fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        var cardData = gameData.cardDataMap[bonusInfo.bonusId]
        if(xlsItem.type != ItemType.CARD.value){
            return false
        }
        if(cardData == null || bonusInfo.bonusNum > 1){
            return false
        }
        return true
    }

    override fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        gameData.cardDataMap.remove(bonusInfo.bonusId)
        var mutableListOf = mutableListOf<BonusInfo>()
        mutableListOf.add(bonusInfo)
        return mutableListOf
    }

    override fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.CARD.value){
            return emptyList()
        }
        var bonusNum = bonusInfo.bonusNum
        var mutableListOf = mutableListOf<BonusInfo>()

        var cardData = gameData.cardDataMap[bonusInfo.bonusId]
        if(cardData == null){
            cardData = CardData()
            gameData.cardDataMap[bonusInfo.bonusId] = cardData
            cardData.id = bonusInfo.bonusId
            cardData.level = 1
            cardData.fragmentNum = 0
            bonusInfo.bonusNum = 1
            mutableListOf.add(bonusInfo)
            bonusNum -= 1
        }
        if(cardData.level == 0){
            cardData.level = 1
            bonusNum -= 1
            mutableListOf.add(bonusInfo)
        }
        if(bonusNum == 0){
            return mutableListOf
        }
        var bonusInfo1 =
            BonusInfo(xlsItem.itemToPieceId, ItemType.CARD_PRICE, xlsItem.itemToPieceNum * bonusNum)
        var rewardBonus = BonusService.rewardBonus(bonusInfo1, gameData)
        mutableListOf.addAll(rewardBonus)
        return mutableListOf
    }
}