package com.ohayoo.whitebird.service.abs

import com.ohayoo.whitebird.data.model.GameData

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-05-12
 */
interface IDataHolder {
    suspend fun checkNullAndInit(gameData: GameData) {}

    suspend fun dailyReset(gameData: GameData) {}

    suspend fun checkEveryRequest(gameData: GameData) {}

}