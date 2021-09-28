package com.ohayoo.whitebird.message

import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.boot.SystemServiceImpl
import com.ohayoo.whitebird.compoent.TokenUtil
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.enums.MessageType
import com.ohayoo.whitebird.exception.CustomException
import com.ohayoo.whitebird.generate.message.CommonMessage
import com.ohayoo.whitebird.player.PlayerSystemService
import com.ohayoo.whitebird.player.enums.AttributeEnum
import com.ohayoo.whitebird.player.model.IPlayer
import com.ohayoo.whitebird.service.abs.BaseService
import io.vertx.core.http.HttpServerRequest
import io.vertx.kotlin.coroutines.awaitResult
import org.slf4j.LoggerFactory

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
class MsgSystemService : SystemServiceImpl {
    private val log = LoggerFactory.getLogger(this::class.java)

    @JvmField
    var msgHandler: MsgHandler? = null
    override fun startService() {
        val msgType = GlobalContext.serverConfig().msgType
        if (msgType == MessageType.json) {
            msgHandler = JsonMsgHandler()
        }
        if (msgType == MessageType.proto) {
            msgHandler = ProtoMsgHandler()
        }
        msgHandler!!.init()
    }

    suspend fun handler(player: IPlayer, msgId: Int, body: ByteArray,http:Boolean) {
        //这里进行取库Data
        try {
            if(canCheckToken(msgId.toString())){
                //check token
                if(http){
                    val req: HttpServerRequest = player.getAttribute<HttpServerRequest>(AttributeEnum.link)
                    val token = req.getHeader(CommonMessage.HeaderType.token.name)
                    var openId = TokenUtil.checkToken(token)
                    //取数据
                    val gameData = awaitResult<GameData> {
                            h -> GlobalContext.getDataSystemService().selectByOpenId(openId,h)
                    }
                    if (gameData == null) {
                        throw CustomException(CommonMessage.ErrorCode.TokenErrorException)
                    }
                    if (!token.endsWith(gameData.userData.token,ignoreCase = true)) {
//   TODO                     throw CustomException(CommonMessage.ErrorCode.TokenErrorException)
                    }
                    //set数据
                    player.setAttribute(AttributeEnum.data,gameData)
                    //预处理
                    BaseService.prepareReq(gameData)
                }
            }
            msgHandler!!.handler(player, msgId, body)
        }catch (e : CustomException){
            log.error("业务异常",e)
            if(http) {
                player.sendError(e.statusCode,0)
            }else{
                var newBuilder = CommonMessage.PBErrorDown.newBuilder()
                newBuilder.commandId = msgId
                newBuilder.errorCode = e.statusCode
                var build = newBuilder.build()
                player.send(build,PlayerSystemService.getMsgId(build))
            }
        }catch (e : Exception){
            log.error("未知异常",e)
            if(http) {
                player.sendError(CommonMessage.ErrorCode.ServerInnerException_VALUE,0)
            }else{
                var newBuilder = CommonMessage.PBErrorDown.newBuilder()
                newBuilder.commandId = msgId
                newBuilder.errorCode = CommonMessage.ErrorCode.ServerInnerException_VALUE
                var build = newBuilder.build()
                player.send(build,PlayerSystemService.getMsgId(build))
            }
        }
    }

    private fun canCheckToken(msgId: String):Boolean{
        var noDataExecuteMsgId = GlobalContext.serverConfig().noDataExecuteMsgId
        for(id  in noDataExecuteMsgId){
            if(id.equals( msgId )){
                return false
            }
        }
        return true
    }
}

