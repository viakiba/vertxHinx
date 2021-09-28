package com.ohayoo.whitebird.bonus.holder

import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.bonus.BonusInfo

interface BonusHolder {
    /**
     * 判断可否扣除奖励
     * @return
     */
    fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean

    /**
     * 扣除奖励
     * @return
     */
    fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo>

    /**
     * 发放奖励
     * @return
     */
    fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo>
}