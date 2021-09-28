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

object ISkillDataHolder : BonusHolder {

    override fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.SKILL.value){
            return false
        }
        var skillData = gameData.skillDataMap[xlsItem.idKey]
        if(skillData == null || bonusInfo.bonusNum > 1){
            return false
        }
        return true
    }

    override fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        var cardData = gameData.cardDataMap[xlsItem.idKey] ?: throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        gameData.skillDataMap.remove(cardData.id)
        return Collections.singletonList(bonusInfo)
    }

    override fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, bonusInfo.bonusId)
        if(xlsItem.type != ItemType.SKILL.value){
            return emptyList()
        }
        var skillData = gameData.skillDataMap[bonusInfo.bonusId]
        bonusInfo.bonusNum = 1
        if(skillData == null){
            skillData = SkillData()
            gameData.skillDataMap[bonusInfo.bonusId] = skillData
            skillData.id = xlsItem.idKey
            skillData.level = 1
            skillData.fragmentNum = 0
            return Collections.singletonList(bonusInfo)
        } else if(skillData.level == 0){
            skillData.level = 1
            return Collections.singletonList(bonusInfo)
        }else{
            return emptyList()
        }
    }
}