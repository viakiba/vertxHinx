package com.ohayoo.whitebird.service.abs

import com.google.protobuf.GeneratedMessage
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.compoent.TimeUtil
import com.ohayoo.whitebird.config.ExcelCommonConfig
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.data.model.gamedata.CardData
import com.ohayoo.whitebird.data.model.gamedata.ShopData
import com.ohayoo.whitebird.data.model.gamedata.SkillData
import com.ohayoo.whitebird.data.model.gamedata.TowerData
import com.ohayoo.whitebird.generate.message.ModelMessage
import com.ohayoo.whitebird.message.MsgHandler
import com.ohayoo.whitebird.player.PlayerSystemService

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
abstract class BaseService : IProtoMessage, IJsonMessage, IDataHolder{
    companion object{
        // 统一预处理  --------------------------------------------------------------
        /**
         * @description 判断指定属性是否为null 并且对其初始化
         * @author huangpeng.12@bytedance.com
         * @param gameData : GameData
         * @return:
         */
        @JvmStatic
        open suspend fun checkNullAndInitAll(gameData: GameData) {
            var values = MsgHandler.bizServiceMap.values
            for (v in values){
                v.checkNullAndInit(gameData)
            }
        }

        /**
         * @description 每次请求都会经过此操作进行处理
         * @author huangpeng.12@bytedance.com
         * @param gameData : GameData
         * @return:
         */
        @JvmStatic
        open suspend fun checkEveryRequestAll(gameData: GameData) {
            var values = MsgHandler.bizServiceMap.values
            for (v in values){
                v.checkEveryRequest(gameData)
            }
        }

        /**
         * @description 每日重置逻辑
         * @author huangpeng.12@bytedance.com
         * @param gameData :  GameData
         * @return:
         */
        @JvmStatic
        open suspend fun dailyRestAll(gameData: GameData) {
            if (TimeUtil.isSameDay(gameData.userData.dailyResetTime, TimeUtil.currentSystemTime())) {
                return
            }
            gameData.userData.dailyResetTime = System.currentTimeMillis()
            var values = MsgHandler.bizServiceMap.values
            for (v in values){
                v.dailyReset(gameData)
            }
        }

        /**
         * @description  登录时的统一预备处理
         * @author huangpeng.12@bytedance.com
         * @param gameData :  GameData
         * @return:
         */
        @JvmStatic
        open suspend fun prepareReq(gameData: GameData) {
            checkNullAndInitAll(gameData)
            dailyRestAll(gameData)
            checkEveryRequestAll(gameData)
        }
        // 以上为基础通用方法 ------------------------------------------ 结束
    }

    fun getMsgId(msg: GeneratedMessage): Int {
        return PlayerSystemService.getMsgId(msg)
    }

    fun printMessage(message: GeneratedMessage){
        PlayerSystemService.printMessage(message)
    }

    // 一下为下发的消息构造 较为公共的
    fun createPBSyncData(gameData: GameData): ModelMessage.PBSyncData{
        var newBuilder = ModelMessage.PBSyncData.newBuilder()
        newBuilder.userId = gameData.id
        newBuilder.user = createPBUserData(gameData)
        newBuilder.addAllCurrencyData(createPBCurrencyData(gameData))
        newBuilder.addAllCardData(createPBCardData(gameData))
        newBuilder.addAllCardPositionData(createPBCardPositionData(gameData))
        newBuilder.addAllTowerDataMap(createTowerData(gameData))
        newBuilder.addAllSkillData(createSkillData(gameData))
        newBuilder.chapterData = createPBChapterData(gameData)
        newBuilder.addAllShopData(createShopData(gameData))
        newBuilder.addAllMainTask(gameData.mainTaskDataList)
        return newBuilder.build()
    }

    private fun createTowerData(gameData: GameData): MutableIterable<ModelMessage.TowerData> {
        var towerData = gameData.towerDataMap
        var mutableIterable = mutableListOf<ModelMessage.TowerData>()
        for( e in towerData){
            var t = createTowerData(e.value)
            mutableIterable.add(t)
        }
        return mutableIterable
    }

    public fun createTowerData(towerData: TowerData): ModelMessage.TowerData {
        var newBuilder = ModelMessage.TowerData.newBuilder()
        newBuilder.id = towerData.id
        newBuilder.level = towerData.level
        newBuilder.fragmentNum = towerData.fragmentNum
        return newBuilder.build()
    }

    private fun createSkillData(gameData: GameData): MutableIterable<ModelMessage.SkillData> {
        var skillDataMap = gameData.skillDataMap
        var mutableIterable = mutableListOf<ModelMessage.SkillData>()
        for( e in skillDataMap){
            var createSkillData = createSkillData(e.value)
            mutableIterable.add(createSkillData)
        }
        return mutableIterable
    }

    public fun createSkillData(skillData: SkillData): ModelMessage.SkillData {
        var newBuilder = ModelMessage.SkillData.newBuilder()
        newBuilder.id = skillData.id
        newBuilder.level = skillData.level
        newBuilder.fragmentNum = skillData.fragmentNum
        return newBuilder.build()
    }

    public fun createPBCardPositionData(gameData: GameData): MutableIterable<ModelMessage.CardPositionData> {
        var mutableListOf = mutableListOf<ModelMessage.CardPositionData>()

        for ( (index,value ) in gameData.cardPositionDataList.withIndex()) {
            var newBuilder = ModelMessage.CardPositionData.newBuilder()
            newBuilder.index = index
            newBuilder.cardId = value
            mutableListOf.add(newBuilder.build())
        }
        return mutableListOf
    }

    fun createPBCardData(gameData: GameData): List<ModelMessage.CardData> {
        var mutableListOf = mutableListOf<ModelMessage.CardData>()
        gameData.cardDataMap.entries.stream().forEach { x ->
            var build = createPBCardData(x.value)
            mutableListOf.add(build)
        }
        return mutableListOf
    }

    fun createPBCardData(x: CardData): ModelMessage.CardData {
        var newBuilder = ModelMessage.CardData.newBuilder()
        newBuilder.id = x.id
        newBuilder.level = x.level
        newBuilder.fragmentNum = x.fragmentNum
        return newBuilder.build()
    }

    fun createPBCurrencyData(gameData: GameData): MutableIterable<ModelMessage.CurrencyData> {
        var mutableListOf = mutableListOf<ModelMessage.CurrencyData>()
        gameData.currencyDataMap.entries.stream().forEach { x ->
            var newBuilder = ModelMessage.CurrencyData.newBuilder()
            newBuilder.type = x.key
            newBuilder.count = x.value
            mutableListOf.add(newBuilder.build())
        }
        return mutableListOf
    }

    private fun createPBChapterData(gameData: GameData): ModelMessage.ChapterData {
        var newBuilder = ModelMessage.ChapterData.newBuilder()
        var chapterData = gameData.chapterData
        newBuilder.chapterNum = chapterData.chapterNum
        newBuilder.smallLevelNum = chapterData.smallLevelNum
        return newBuilder.build()
    }

    fun createPBUserData(gameData: GameData): ModelMessage.UserData {
        var newBuilder = ModelMessage.UserData.newBuilder()
        var userData = gameData.userData
        if(userData.name.isNullOrBlank()) {
            newBuilder.name = ExcelCommonConfig.getDefaultName()
        }else{
            newBuilder.name = userData.name
        }
        if(userData.icon.isNullOrBlank()) {
            newBuilder.icon = ExcelCommonConfig.getDefaultIcon()
        }else{
            newBuilder.icon = userData.icon
        }
        newBuilder.createTime = userData.createTime
        newBuilder.dailyResetTime = userData.dailyResetTime
        newBuilder.renameLastTime = userData.renameLastTime
        newBuilder.signNum = userData.signNum
        newBuilder.signTime = userData.signTime
        newBuilder.skillId = userData.skillId
        newBuilder.towerId = userData.towerId
        newBuilder.addAllNewPlayerFlag( userData.newPlayerFlag )
        return newBuilder.build()
    }

    private fun createShopData(gameData: GameData): MutableIterable<ModelMessage.ShopData> {
        var mutableListOf = mutableListOf<ModelMessage.ShopData>()
        var shopData = gameData.shopData
        for ( s in shopData ){
            var value = s.value
            mutableListOf.add(createShopData(value))
        }
        return mutableListOf
    }

    fun createShopData(shopData: ShopData): ModelMessage.ShopData {
        var newBuilder = ModelMessage.ShopData.newBuilder()
        newBuilder.id = shopData.id
        newBuilder.adCdLastSecond = shopData.adCdLastSecond
        newBuilder.adBuyNum = shopData.adBuyNum
        newBuilder.freeBuyNum = shopData.freeBuyNum
        newBuilder.freeLastRefreshSecond = shopData.freeLastRefreshSecond
        newBuilder.specialDropNum = shopData.specialDropNum
        newBuilder.minimumGuaranteeNum = shopData.minimumGuaranteeNum
        return newBuilder.build()
    }

    fun createBonusInfo(bonusInfo:List<BonusInfo>): MutableList<ModelMessage.ResourceData> {
        var mutableListOf = mutableListOf<ModelMessage.ResourceData>()
        bonusInfo.stream().forEach { x ->
            mutableListOf.add(createBonusInfo(x))
        }
        return mutableListOf
    }

    fun createBonusInfo(bonusInfo:BonusInfo): ModelMessage.ResourceData {
        var newBuilder = ModelMessage.ResourceData.newBuilder()
        newBuilder.resourceID = bonusInfo.bonusId
        newBuilder.type = bonusInfo.bonusType.value
        newBuilder.count = bonusInfo.bonusNum
        return newBuilder.build()
    }


}