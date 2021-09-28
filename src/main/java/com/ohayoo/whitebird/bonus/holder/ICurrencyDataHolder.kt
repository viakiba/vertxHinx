package com.ohayoo.whitebird.bonus.holder

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.enums.ItemType
import com.ohayoo.whitebird.excel.model.XlsItem
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import java.util.*

object ICurrencyDataHolder : BonusHolder {

    override fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)

        if(xlsItem.type != ItemType.CURRENCY.value){
            return false
        }
        var num = gameData.currencyDataMap[xlsItem.idKey]
        if(num == null || num < bonusInfo.bonusNum){
            return false
        }
        return true
    }

    override fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        var l = gameData.currencyDataMap[xlsItem.idKey]
        if(l == null){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        if(l <= 0){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        gameData.currencyDataMap[xlsItem.idKey] = l - bonusInfo.bonusNum
        if(gameData.currencyDataMap[xlsItem.idKey]!! < 0){
            gameData.currencyDataMap[xlsItem.idKey] = 0
        }
        return Collections.singletonList(bonusInfo)
    }

    override fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.CURRENCY.value){
            return emptyList()
        }
        var currencyDataMap = gameData.currencyDataMap
        var curNum = currencyDataMap[bonusInfo.bonusId]
        if(curNum == null){
            currencyDataMap[bonusInfo.bonusId] = bonusInfo.bonusNum.toLong()
        }else {
            currencyDataMap[bonusInfo.bonusId] = curNum + bonusInfo.bonusNum
        }
        return Collections.singletonList(bonusInfo)
    }
}