package com.ohayoo.whitebird.data

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.bonus.BonusService
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.compoent.TimeUtil
import com.ohayoo.whitebird.config.ExcelCommonConfig
import com.ohayoo.whitebird.config.ServerConfig
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.data.model.gamedata.*
import com.ohayoo.whitebird.enums.CardCollectionType
import com.ohayoo.whitebird.enums.CardInitialStateType
import com.ohayoo.whitebird.enums.CurrencyType
import com.ohayoo.whitebird.excel.model.XlsCard
import com.ohayoo.whitebird.excel.model.XlsItem
import com.ohayoo.whitebird.excel.model.XlsShop
import com.ohayoo.whitebird.excel.model.XlsTower
import com.ohayoo.whitebird.generate.message.ModelMessage
import com.ohayoo.gamecloud.model.BaseInfo
import org.apache.commons.lang3.StringUtils

object GameDataHelper {
    @JvmStatic
    fun checkBdData(gameData: GameData) : Boolean{
        val bdData = gameData.bdData ?: return false
        if (StringUtils.isEmpty(bdData.logId)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.osName)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.loginType)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.accountType)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.version)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.packageName)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.channel)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.deviceId)) {
            return false
        }
        if (StringUtils.isEmpty(bdData.openId)) {
            false
        }
        return true
    }
    @JvmStatic
    fun initGameData(gameData: GameData){
        gameData.id = GlobalContext.getSnowflake().nextId();
        gameData.openId = "ccc"
        initUserData(gameData)
        initCurrencyData(gameData)
        initCardData(gameData)
        initTowerData(gameData);
        initSkillData(gameData);
        initChapterData(gameData)
        initShopData(gameData)
        initMainTaskData(gameData)
    }

    private fun initMainTaskData(gameData: GameData) {
        gameData.mainTaskDataList = mutableListOf(3)
        gameData.mainTaskDataList[0] = 1
        gameData.mainTaskDataList[1] = 0
    }

    @JvmStatic
    private fun initTowerData(gameData: GameData) {
        gameData.towerDataMap = mutableMapOf()
        var xlsTowerMap : Map<Int,XlsTower>  = ExcelDataService.getByClass(XlsTower::class.java)
        for(xls in xlsTowerMap){
            var createTowerData = createTowerData(xls.value)
            gameData.towerDataMap[createTowerData.id] = createTowerData
        }
    }

    private fun createTowerData(xlsTower: XlsTower): TowerData {
        var towerData1 = TowerData()
        towerData1.id = xlsTower.idKey
        towerData1.level = 1
        towerData1.fragmentNum = 0
        return towerData1
    }

    private fun initSkillData(gameData: GameData) {
        gameData.skillDataMap = mutableMapOf()
    }

    @JvmStatic
    private fun initShopData(gameData: GameData) {
        gameData.shopData = mutableMapOf()
        var xlsShopMap : Map<Int,XlsShop> = ExcelDataService.getByClass(XlsShop::class.java)
        for( xls in xlsShopMap){
            var value = xls.value
            gameData.shopData[value.idKey] = getShopData(value)
        }
    }

    @JvmStatic
    fun createTowerData(
        xlsItem: XlsItem
    ): TowerData {
        var towerData1 = TowerData()
        towerData1.id = xlsItem.pieceToItemId
        towerData1.level = 1
        towerData1.fragmentNum = 0
        return towerData1
    }

    @JvmStatic
    fun getShopData(xlsShop: XlsShop): ShopData {
        var curTime = TimeUtil.currentSystemTime()
        return ShopData(
            xlsShop.idKey, curTime, 0, 0,
        curTime+xlsShop.freeRefreshTime*60*60*1000,
            0, 0
        )
    }

    @JvmStatic
    private fun initChapterData(gameData: GameData) {
        gameData.chapterData = ChapterData()
        var chapterData = gameData.chapterData
        chapterData.chapterNum = ExcelCommonConfig.getDefaultChapter()
        chapterData.smallLevelNum = ExcelCommonConfig.getDefaultChapterSmallLevel()
    }

    @JvmStatic
    fun initCardData(gameData: GameData) {
        var cardData = mutableMapOf<Int, CardData>()
        gameData.cardDataMap = cardData
        gameData.cardPositionDataList = mutableListOf()

        var xlsCards : Map<Int,XlsCard> = ExcelDataService.getByClass(XlsCard::class.java)
        for ( card in xlsCards){
            var xlsCard = card.value
            if(xlsCard.collection == CardCollectionType.no_collection.value){
                continue
            }
            if(xlsCard.initialState == CardInitialStateType.NO_GIVE.value){
                continue
            }
            //给卡
            giveCard(gameData,xlsCard)
            //上阵
            if(xlsCard.initialState == CardInitialStateType.GIVE_UP.value) {
                gameData.cardPositionDataList.add(xlsCard.gid)
            }
        }
    }

    @JvmStatic
    fun giveCard(gameData: GameData,xlsCard: XlsCard){
        var cardData = CardData()
        cardData.id = xlsCard.idKey
        cardData.fragmentNum = 0
        cardData.level = xlsCard.initialLev
        gameData.cardDataMap[cardData.id] = cardData
    }

    @JvmStatic
    fun initCurrencyData(gameData: GameData) {
        var currencyData = mutableMapOf<Int,Long>()
        gameData.currencyDataMap = currencyData

        for ( c in CurrencyType.values() ){
            gameData.currencyDataMap[c.value] = 0
        }
    }
    @JvmStatic
    fun initUserData(gameData: GameData) {
        var userData = UserData()
        userData.name = ""
        userData.icon = ""
        userData.createTime = TimeUtil.currentSystemTime()
        userData.lastLoginTime = TimeUtil.currentSystemTime()
        userData.dailyResetTime = TimeUtil.currentSystemTime()
        userData.renameLastTime = TimeUtil.currentSystemTime() - ExcelCommonConfig.getRenameCdMS()
        userData.brawnRefreshTime = TimeUtil.currentSystemTime()
        userData.towerId = ExcelCommonConfig.getDefaultTowerId()
        userData.skillId = ExcelCommonConfig.getDefaultSkillId()
        userData.newPlayerFlag = mutableSetOf()
        gameData.userData = userData
    }
    @JvmStatic
    fun getBaseInfo(gameData: GameData): BaseInfo {
            val logModelObj: BdData = gameData.bdData
            val baseInfo = BaseInfo()
            baseInfo.appId = ServerConfig.gameCloudAppID
            baseInfo.appPackage = logModelObj.packageName
            baseInfo.timestamp = TimeUtil.currentSystemTime()
            baseInfo.userId = logModelObj.logId
            baseInfo.accountType = logModelObj.accountType
            baseInfo.accountId = logModelObj.openId
            baseInfo.deviceId = logModelObj.deviceId
            baseInfo.os = logModelObj.osName
            baseInfo.channel = logModelObj.channel
            return baseInfo
        }

    @JvmStatic
    fun checkBrawn(gameData: GameData) {
        //体力check
        var userData = gameData.userData
        var brawn = gameData.currencyDataMap[CurrencyType.TI_LI.value]!!.toInt()
        val curTimeStamp = TimeUtil.currentSystemTimeSeconds()
        if (brawn >= ExcelCommonConfig.getMaxBrawn()) {
            userData.brawnRefreshTime = curTimeStamp
            return
        }
        val energyCostTimeStamp: Long = userData.brawnRefreshTime
        val timeInterval = curTimeStamp - energyCostTimeStamp
        val recoverCount: Int = timeInterval.toInt() / ExcelCommonConfig.getRefreshBrawnTimeMs()
        if (recoverCount <= 0) {
            return
        }
        var recoverValue: Int = recoverCount * ExcelCommonConfig.getRefreshBrawnTimeMs()
        if (recoverValue <= 0) {
            return
        }
        if (brawn + recoverValue > ExcelCommonConfig.getMaxBrawn()) {
            recoverValue = ExcelCommonConfig.getMaxBrawn() - brawn
        }
        userData.brawnRefreshTime = curTimeStamp
        gameData.currencyDataMap[CurrencyType.TI_LI.value] = recoverValue.toLong()
        return
    }

    fun giveDefaultItem(gameData: GameData) {
        var defaultItemId = ExcelCommonConfig.getDefaultItemId()
        var defaultItemNum = ExcelCommonConfig.getDefaultItemNum()
        for ( (index,itemId) in defaultItemId.withIndex()){
            var bonusInfo = BonusInfo(itemId, defaultItemNum[index])
            BonusService.rewardBonus(bonusInfo,gameData)
        }
    }
}