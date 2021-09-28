package com.ohayoo.whitebird.bonus

import com.ohayoo.whitebird.compoent.LoggerUtil
import com.ohayoo.whitebird.data.model.GameData

object BonusService {
    fun canCostBonus(bonusInfo: BonusInfo, gameData: GameData): Boolean {
        if(checkBonus(bonusInfo)){
            return true
        }
        return bonusInfo.bonusHolder.canCostBonus(gameData, bonusInfo)
    }

    fun costBonus(bonusInfo: BonusInfo, gameData: GameData): List<BonusInfo> {
        if(checkBonus(bonusInfo)){
            return emptyList()
        }
        return bonusInfo.bonusHolder.costBonus(gameData, bonusInfo)
    }

    fun rewardBonus(bonusInfo: BonusInfo, gameData: GameData): List<BonusInfo> {
        if(checkBonus(bonusInfo)){
            return emptyList()
        }
        return bonusInfo.bonusHolder.rewardBonus(gameData, bonusInfo)
    }

    private fun checkBonus(bonusInfo: BonusInfo):Boolean {
        if (bonusInfo.bonusNum == 0) {
            return true
        }
        if (LoggerUtil.isDebugEnabled()) {
            LoggerUtil.debug("rewardBonus   " + bonusInfo.bonusId + "   " + bonusInfo.bonusType + "  " + bonusInfo.bonusNum)
            if (bonusInfo.bonusHolder == null) {
                LoggerUtil.error(bonusInfo.bonusId.toString() + "ID 的 holder 为 null")
                return true;
            }
        }
        return false;
    }
}