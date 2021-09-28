package com.ohayoo.whitebird.bonus.holder

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.bonus.BonusService
import com.ohayoo.whitebird.compoent.LoggerUtil
import com.ohayoo.whitebird.compoent.RandomUtil
import com.ohayoo.whitebird.enums.CardOrPriceDropRepeatType
import com.ohayoo.whitebird.enums.CardPoolType
import com.ohayoo.whitebird.enums.ItemType
import com.ohayoo.whitebird.excel.model.XlsCard
import com.ohayoo.whitebird.excel.model.XlsDrop
import com.ohayoo.whitebird.excel.model.XlsItem
import lombok.extern.slf4j.Slf4j
import kotlin.collections.ArrayList

@Slf4j
object IDropDataHolder : BonusHolder {

    override fun canCostBonus(gameData: GameData, bonusInfo: BonusInfo): Boolean {
        LoggerUtil.error("策划扣除时不能配置掉落ID")
        return false
    }

    override fun costBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        LoggerUtil.error("策划扣除时不能配置掉落ID")
        return ArrayList()
    }

    /**
     * @description https://bytedance.feishu.cn/docs/doccnN7ZbSsySNuW0UYu8xI6OAt
     * @author huangpeng.12@bytedance.com
     * @param gameData: GameData
     * @param bonusInfo: BonusInfo
     * @return: List<BonusInfo>
     */
    override fun rewardBonus(gameData: GameData, bonusInfo: BonusInfo): List<BonusInfo> {
        var result = mutableListOf<BonusInfo>()
        var xlsDrop = ExcelDataService.getByClassAndId<XlsDrop>(XlsDrop::class.java, bonusInfo.bonusId)
            ?: return emptyList()
        //卡池计算
        var cardPool:Map<Int,List<Int>> = getCardPool( xlsDrop.completeCardPool,gameData)
        //先掉完整卡
        var completeCardGuaranteed = xlsDrop.completeCardGuaranteed
        for(cardId in completeCardGuaranteed){
            var bonusInfoTemp = BonusInfo(cardId, ItemType.CARD)
            var rewardBonus = BonusService.rewardBonus(bonusInfoTemp, gameData)
            result.addAll(rewardBonus)
        }
        //再掉概率卡
        var completeCardNumber = xlsDrop.completeCardNumber
        var repeatCard = mutableListOf<Int>()
        for ( index in 1..completeCardNumber){
            var completeCardWeight = xlsDrop.completeCardWeight
            if(completeCardWeight.size<index){
                break
            }
            var cardRarity = RandomUtil.norepeatRrandomByList(completeCardWeight)
            var cardPoolType2 = getCardPoolType2(cardRarity, cardPool)
            if(cardPoolType2.isEmpty()){
                continue
            }
            cardPoolType2.removeAll(repeatCard)
            if(cardPoolType2.isEmpty()){
                continue
            }
            var norepeatRrandomByList = RandomUtil.norepeatRrandomByList(cardPoolType2, 1)
            var cardId = norepeatRrandomByList[0]
            var bonusInfo1 = BonusInfo(cardId, ItemType.CARD, 1)
            result.add(bonusInfo1)
            if(xlsDrop.completeCardisUnique == CardOrPriceDropRepeatType.REMOVE_REPEAT.value){
                repeatCard.add(cardId)
            }
        }
        //碎片
        var cardPieceAll = xlsDrop.cardPiece
        var cardPiecePoolConfig = xlsDrop.cardPiecePool
        var cardPricePool = getCardPool(cardPiecePoolConfig, gameData)
        for ( cardPiece in cardPieceAll){
            if(cardPiece.size < 6){
                continue
            }
            var cardRarity = cardPiece[0]
            var randomCardPriceList = mutableListOf<Int>()
            if(cardPiecePoolConfig == CardPoolType.ALL_CARD_VERSION.value){
                randomCardPriceList.addAll(getCardPoolType2(cardRarity,cardPricePool))
            }else{
                var list1 = cardPricePool[cardRarity]
                if(list1 != null) {
                    randomCardPriceList.addAll(list1)
                }
            }
            if(randomCardPriceList.size == 0){
                continue
            }
            var cardPieceChance = cardPiece[1]
            if(!RandomUtil.getSuccessRandom(cardPieceChance,1000)){
                continue
            }
            var cardPieceMinNum = cardPiece[2]
            var cardPieceMaxNum = cardPiece[3]
            var allNum = RandomUtil.regionRandom(cardPieceMinNum, cardPieceMaxNum)
            var dropNumTimes = cardPiece[4]
            if(allNum == 0){
                continue
            }
            if(dropNumTimes == 0){
                continue
            }
            var dropNum = allNum / dropNumTimes
            if(dropNum == 0){
                continue
            }
            if(cardPiece[5] == CardOrPriceDropRepeatType.REMOVE_REPEAT.value) {
                var cardIds = RandomUtil.norepeatRrandomByList(randomCardPriceList, dropNumTimes)
                for(cardId in cardIds){
                    dropCardPrice(cardId, dropNum, gameData, result)
                }
            }else{
                for (i in 0..dropNumTimes) {
                    var cardId = randomCardPriceList[RandomUtil.regionRandom(0,randomCardPriceList.size)];
                    dropCardPrice(cardId, dropNum, gameData, result)
                }
            }
        }

        //指定物品  各自单独掉 根据自己配置的概率（比1000）与 数量（最大最小中等概率随机）
        var itemDrop = xlsDrop.itemDrop
        for(itemConfig in itemDrop){
            if(itemConfig.size < 4){
                continue
            }
            var itemDropChance = itemConfig[1]
            if(!RandomUtil.getSuccessRandom(itemDropChance,1000)){
                continue
            }
            var itemId = itemConfig[0]
            var itemDropMinNum = itemConfig[2]
            var itemDropMaxNum = itemConfig[3]
            var num = RandomUtil.regionRandom(itemDropMinNum, itemDropMaxNum)
            if(itemDropMaxNum == itemDropMinNum){
                num = itemDropMaxNum
            }
            var bonusInfoTemp = BonusInfo(itemId, num)
            var rewardBonus = BonusService.rewardBonus(bonusInfoTemp, gameData)
            result.addAll(rewardBonus)
        }
        return result
    }

    /**
     * @description 获取卡池类型2的卡池
     * @author huangpeng.12@bytedance.com
     * @param cardRarity: Int
     * @param cardPricePool: Map<Int, List<Int>>
     * @return: MutableList<Int>
     */
    private fun getCardPoolType2(cardRarity: Int, cardPricePool: Map<Int, List<Int>>): MutableList<Int> {
        var mutableListOf = mutableListOf<Int>()
        for(cardRarity in cardRarity downTo 1){
            var list = cardPricePool[cardRarity]
            if(list !=null){
               mutableListOf.addAll(list)
               break
            }
        }
        return mutableListOf
    }

    /**
     * @description 根据配置获取符合条件的掉落卡池
     * @author huangpeng.12@bytedance.com
     * @param  completeCardPool: Int
     * @param  gameData: GameData
     * @return: Map<Int,List<Int>>
     */
    private fun getCardPool(completeCardPool: Int, gameData: GameData): Map<Int,List<Int>> {
        var result = mutableMapOf<Int,ArrayList<Int>>()
        if(completeCardPool == 1){
            var byClass : Map<Int,XlsCard>? = ExcelDataService.getByClassAndTypeName(XlsCard::class.java,1)
            if(byClass != null) {
                for (xlsCard in byClass) {
                    var value = xlsCard.value
                    var list = result[value.rarity]
                    if (list == null) {
                        list = ArrayList()
                        result[value.rarity] = list
                    }
                    list.add(xlsCard.key)
                }
            }
        }
        if(completeCardPool == 2){
            var keys = gameData.cardDataMap.keys
            for (id in keys) {
                var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, id)
                if(xlsItem == null || xlsItem.type != ItemType.CARD.value ){
                    continue
                }
                var list = result[xlsItem.rarity]
                if(list == null){
                    list = ArrayList()
                    result[xlsItem.rarity] = list
                }
                list.add(id)
            }
        }
        if(completeCardPool == 3){
            var xlsItemMap : Map<Int,XlsItem>? = ExcelDataService.getByClassAndTypeName(XlsItem::class.java, ItemType.CARD.value)
            if(xlsItemMap != null){
            for (xlsItem in xlsItemMap){
                var xlsItemCard = xlsItem.value
                if(gameData.chapterData.excelChapterLevelNum < xlsItemCard.unlock){
                    continue
                }
                var list = result[xlsItemCard.rarity]
                if(list == null){
                    list = ArrayList()
                    result[xlsItemCard.rarity] = list
                }
                list.add(xlsItemCard.gid)
            }
            }
        }
        return result
    }

    /**
     * @description 给碎片
     * @author huangpeng.12@bytedance.com
     * @param cardId: Int
     * @param dropNum: Int
     * @param gameData: GameData
     * @param result: MutableList<BonusInfo>
     * @return:
     */
    private fun dropCardPrice(cardId: Int, dropNum: Int, gameData: GameData, result: MutableList<BonusInfo>) {
        var xlsItem = ExcelDataService.getByClassAndId<XlsItem>(XlsItem::class.java, cardId)
        if (xlsItem != null) {
            var bonusInfoTemp = BonusInfo(xlsItem.itemToPieceId, ItemType.CARD_PRICE, dropNum)
            var rewardBonus = BonusService.rewardBonus(bonusInfoTemp, gameData)
            result.addAll(rewardBonus)
        }
    }
}