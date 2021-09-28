package com.ohayoo.whitebird.bonus.holder

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.data.GameDataHelper
import com.ohayoo.whitebird.enums.ItemType
import com.ohayoo.whitebird.excel.model.XlsItem
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import java.util.*

object ITowerStuffDataHolder : BonusHolder {

    override fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.TOWER_STUFF.value){
            return false
        }
        var skillData = gameData.towerDataMap[xlsItem.pieceToItemId]
        if(skillData == null || bonusInfo.bonusNum > skillData.fragmentNum){
            return false
        }
        return true
    }

    override fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.TOWER_STUFF.value){
            return emptyList()
        }
        var towerData = gameData.towerDataMap[xlsItem.pieceToItemId]
        if(towerData == null){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        if(towerData.fragmentNum == 0){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        towerData.fragmentNum = towerData.fragmentNum - bonusInfo.bonusNum
        if(towerData.fragmentNum < 0){
            towerData.fragmentNum = 0
        }
        return Collections.singletonList(bonusInfo)
    }

    override fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.TOWER_STUFF.value){
            return emptyList()
        }
        var towerData = gameData.towerDataMap[xlsItem.pieceToItemId]
        if(towerData == null){
            towerData = GameDataHelper.createTowerData(xlsItem)
            gameData.towerDataMap[xlsItem.pieceToItemId] = towerData
        } else{
            towerData.fragmentNum = towerData.fragmentNum + bonusInfo.bonusNum
        }
        return Collections.singletonList(bonusInfo)
    }

}