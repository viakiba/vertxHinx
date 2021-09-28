package com.ohayoo.whitebird.bonus.holder

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.data.model.gamedata.CardData
import com.ohayoo.whitebird.enums.ItemType
import com.ohayoo.whitebird.excel.model.XlsItem
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import java.util.*

object ICardPriceDataHolder : BonusHolder {

    override fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.CARD_PRICE.value){
            return false
        }
        if(xlsItem.pieceToItemId == 0 && gameData.userData.allPurposeCardPrice < bonusInfo.bonusNum){
            return false;
        }
        var cardData = gameData.cardDataMap[xlsItem.pieceToItemId]
        if(cardData == null || cardData.fragmentNum < bonusInfo.bonusNum){
            return false
        }
        return true
    }

    override fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.pieceToItemId == 0 && gameData.userData.allPurposeCardPrice < bonusInfo.bonusNum){
            gameData.userData.allPurposeCardPrice = gameData.userData.allPurposeCardPrice - bonusInfo.bonusNum
        }
        var cardData = gameData.cardDataMap[xlsItem.pieceToItemId]
        if(cardData == null){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        if(cardData.fragmentNum <= 0){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        cardData.fragmentNum = cardData.fragmentNum - bonusInfo.bonusNum
        if(cardData.fragmentNum<0){
            cardData.fragmentNum = 0
        }
        return Collections.singletonList(bonusInfo)
    }

    override fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.CARD_PRICE.value){
            return emptyList()
        }
        if(xlsItem.pieceToItemId == 0 ){
            gameData.userData.allPurposeCardPrice = gameData.userData.allPurposeCardPrice + bonusInfo.bonusNum
            return Collections.singletonList(bonusInfo)
        }
        var cardData = gameData.cardDataMap[xlsItem.pieceToItemId]
        var mutableListOf = mutableListOf<BonusInfo>()
        mutableListOf.add(bonusInfo)
        if(cardData == null){
            var cardData = CardData()
            gameData.cardDataMap[xlsItem.pieceToItemId] = cardData
            cardData.id = bonusInfo.bonusId
            cardData.level = 0
            cardData.fragmentNum = 0
            gameData.cardDataMap[bonusInfo.bonusId] = cardData
            bonusInfo.bonusNum = 1
            return mutableListOf
        }
        cardData.fragmentNum = cardData.fragmentNum + bonusInfo.bonusNum
        return mutableListOf
    }

}