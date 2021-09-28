package com.ohayoo.whitebird.bonus.holder

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.data.model.gamedata.SkillData
import com.ohayoo.whitebird.enums.ItemType
import com.ohayoo.whitebird.excel.model.XlsItem
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import java.util.*

object ISkillPriceDataHolder : BonusHolder {

    override fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.SKILL_PRICE.value){
            return false
        }
        var skillData = gameData.skillDataMap[xlsItem.pieceToItemId]
        if(skillData == null || skillData.fragmentNum < bonusInfo.bonusNum){
            return false
        }
        return true
    }

    override fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        var skillData = gameData.skillDataMap[xlsItem.pieceToItemId] ?: throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        if(skillData.fragmentNum <= 0){
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        skillData.fragmentNum = skillData.fragmentNum - bonusInfo.bonusNum
        if(skillData.fragmentNum<0){
            skillData.fragmentNum = 0
        }
        return Collections.singletonList(bonusInfo)
    }

    override fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        var skillData = gameData.skillDataMap[xlsItem.pieceToItemId]
        var mutableListOf = mutableListOf<BonusInfo>()
        if(xlsItem.type != ItemType.SKILL_PRICE.value){
            return emptyList()
        }
        if(skillData == null){
            var skillData = SkillData()
            gameData.skillDataMap[xlsItem.pieceToItemId] = skillData
            skillData.id = bonusInfo.bonusId
            skillData.level = 0
            skillData.fragmentNum = bonusInfo.bonusNum
            gameData.skillDataMap[xlsItem.pieceToItemId] = skillData
        }else{
            skillData.fragmentNum = skillData.fragmentNum + bonusInfo.bonusNum
        }
        mutableListOf.add(bonusInfo)
        return mutableListOf
    }
}