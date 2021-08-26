package com.ohayoo.whitebird.player;

import cn.hutool.core.lang.Tuple;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.compoent.KTCoroutineHelp;
import com.ohayoo.whitebird.enums.NetType;
import com.ohayoo.whitebird.message.MsgSystemService;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import com.ohayoo.whitebird.player.model.HttpPlayer;
import com.ohayoo.whitebird.player.model.IPlayer;
import com.ohayoo.whitebird.player.model.TcpPlayer;
import com.ohayoo.whitebird.player.model.WebsocketPlayer;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class PlayerSystemService implements SystemServiceImpl {

    public Map<Long, IPlayer> playerMap;

    @Override
    public void start() {
        NetType[] netType = GlobalContext.serverConfig().getNetType();
        for (int i = 0; i < netType.length; i++) {
            if(netType[i] != NetType.http && playerMap!=null){
                playerMap = new ConcurrentHashMap();
            }
        }
    }

    @Override
    public void stop() {
        if(playerMap!=null){

        }
    }

    public void addPlayer(IPlayer player){
        playerMap.put( player.getAttribute(AttributeEnum.key),player);
    }

    public static Tuple readMessageInfo(Buffer buffer) {
        int allLength;
        int msgId ;
        if(GlobalContext.serverConfig().isByteOrder()){
            //小端模式
            allLength = buffer.getIntLE(0);
            msgId = buffer.getIntLE(4);
        }else{
            //大端模式
            allLength = buffer.getInt(0);
            msgId = buffer.getInt(4);
        }
        byte[] bytes = buffer.getBytes(4 + 4, allLength);
        Tuple tuple = new Tuple(msgId,bytes);
        return tuple;
    }

    public static void writeMessageInfo(Buffer buffer,byte[] data,int msgId) {
        //长度数据 4字节 + 消息ID 4字节 ++ 数据内容长度
        int length = 4 + 4 + data.length;
        if(GlobalContext.serverConfig().isByteOrder()){
            //小端模式
            buffer.appendIntLE(length);
            buffer.appendIntLE(msgId);
        }else{
            //大端模式
            buffer.appendInt(length);
            buffer.appendInt(msgId);
        }
        buffer.appendBytes(data);
    }

    private void handle(IPlayer player, int msgId, byte[] data) {
        MsgSystemService msgSystemService = GlobalContext.getSystemService(MsgSystemService.class);
        msgSystemService.handler(player, msgId, data);
    }

    public void tcpHandle(Buffer data, TcpPlayer tcpPlayer) {
        Tuple tuple = readMessageInfo(data);
        KTCoroutineHelp ktCoroutineHelp = tcpPlayer.getAttribute(AttributeEnum.coroutine);
        if(ktCoroutineHelp != null){
            MsgSystemService msgSystemService = GlobalContext.getSystemService(MsgSystemService.class);
            ktCoroutineHelp.addTask(tuple.get(0),tuple.get(1),msgSystemService.msgHandler);
        }else{
            KTCoroutineHelp.Companion.runDefault(tcpPlayer,tuple.get(0),tuple.get(1));
        }
    }

    public void websocketHandle(Buffer data, WebsocketPlayer websocketPlayer) {
        int msgId = data.getInt(0);
        byte[] body = data.getBytes(4, data.length());
        KTCoroutineHelp ktCoroutineHelp = websocketPlayer.getAttribute(AttributeEnum.coroutine);
        if(ktCoroutineHelp != null){
            MsgSystemService msgSystemService = GlobalContext.getSystemService(MsgSystemService.class);
            ktCoroutineHelp.addTask(msgId,body,msgSystemService.msgHandler);
        }else{
            KTCoroutineHelp.Companion.runDefault(websocketPlayer,msgId,body);
        }
    }

    public void httpHandle(HttpServerRequest req, Buffer buffer, HttpPlayer httpPlayer) {
        int msgId = Integer.parseInt(req.getHeader("header_message_id"));
        byte[] bytes = buffer.getBytes();
        // http服务 使用 worker线程
        GlobalContext.getVertx().executeBlocking(h -> {
            handle(httpPlayer,msgId,bytes);
        });
    }
}
