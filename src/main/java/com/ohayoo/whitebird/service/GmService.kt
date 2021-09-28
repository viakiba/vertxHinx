package com.ohayoo.whitebird.service;

import com.google.protobuf.Message
import com.ohayoo.whitebird.annotate.BizServiceAnnotate
import com.ohayoo.whitebird.bonus.BonusInfo
import com.ohayoo.whitebird.bonus.BonusService
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.compoent.JsonUtil
import com.ohayoo.whitebird.data.client.MongoDBService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.service.abs.BaseService
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@BizServiceAnnotate
class GmService(override val minMessageId: Int = 10000, override val maxMessageId: Int = 10100) : BaseService() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override val protoMessageRecognize: Map<Int, Message.Builder>
        get() {
            val msgMap = mutableMapOf<Int, Message.Builder>()
            msgMap[10001] = CommonMessage.PBGmCmdUp.newBuilder()
            return msgMap
        }

    override suspend fun handlerProto(messageId: Int, message: Message, player: IPlayer) {
        if(messageId == 10001) {
            gmAction( message as CommonMessage.PBGmCmdUp,player)
            return
        }
        throw CustomException(CommonMessage.ErrorCode.MsgNotFoundException)
    }

    fun gmAction(msg: CommonMessage.PBGmCmdUp,player: IPlayer){
        printMessage(msg)
        var gameData = player.getData<GameData>()
        if (!GlobalContext.serverConfig().isDebug) {
            throw CustomException(CommonMessage.ErrorCode.ServerInnerException)
        }
        val commonGmCmd = msg.param.trim().split(",")
        var mutableListOf = mutableListOf<String>()
        for((index,item) in commonGmCmd.withIndex()){
            if(index == 0){
                continue
            }
            mutableListOf.add(item)
        }
        var iterator = this.javaClass.methods.iterator()
        while (iterator.hasNext()){
            var method = iterator.next()
            if(method.name.equals(commonGmCmd[0],ignoreCase = true)){
                method.invoke(this,gameData,player,mutableListOf)
                if(!method.name.startsWith( "deleteGameData" )){
                    GlobalScope.launch {
                        awaitResult<String> { h -> GlobalContext.getDataSystemService().saveGameData(gameData, h) }
                    }
                }
                responseAllData(gameData, player)
            }
        }
    }

    fun deleteGameData(gameData: GameData,iPlayer: IPlayer, param: List<String>) {
        GlobalScope.launch {
            awaitResult<String> { h -> GlobalContext.getDataSystemService().getIdbService<MongoDBService>().deleteObj("openId",gameData.openId,"gameData",h) }
        }
    }

    fun changeChapter(gameData: GameData,iPlayer: IPlayer, param: List<String>) {
       gameData.chapterData.chapterNum = param[0].toInt()
       gameData.chapterData.smallLevelNum = param[1].toInt()
    }

    fun bonus(gameData: GameData,iPlayer: IPlayer, param: List<String>) {
        var bonusId = param[0].toInt()
        var bonusNum = param[1].toInt()
        var rewardBonus = BonusService.rewardBonus(BonusInfo(bonusId, bonusNum), gameData)
        log.info(JsonUtil.obj2Str(rewardBonus))
    }

    private fun responseAllData(gameData: GameData, player: IPlayer) {
        var newBuilder = CommonMessage.PBGmCmdDown.newBuilder()
        var build = newBuilder.build()
        player.send(build, getMsgId(build))
    }

}
