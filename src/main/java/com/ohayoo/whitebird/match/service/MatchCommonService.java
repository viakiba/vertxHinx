package com.ohayoo.whitebird.match.service;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.match.IMatchService;
import com.ohayoo.whitebird.match.model.MatchPlayer;
import com.ohayoo.whitebird.schedule.ScheduleSystemService;
import com.ohayoo.whitebird.schedule.ScheduleTask;
import org.apache.commons.collections4.list.TreeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-10
 */
public class MatchCommonService implements IMatchService {
    private static Logger log = LoggerFactory.getLogger(MatchCommonService.class);

    //按照分数分段的匹配池  分数段 -> (玩家ID, 玩家数据)
    private static ConcurrentHashMap<Integer,ConcurrentHashMap<Long, MatchPlayer>> playerPool = new ConcurrentHashMap<>();

    private static List<Integer> segmentSortList = new TreeList<>();

    private static int NEED_MATCH_PLAYER_COUNT = 1;

    private static boolean stop = false;

    public void addPlayer(MatchPlayer matchPlayer){
        //TODO 算出此人分段 放入对应 map

    }

    public void removePlayer(MatchPlayer matchPlayer,boolean overTime){
        //TODO 算出此人分段 从对应 map 移除

    }

    public void matchProcess() {
        if(!stop){
            //TODO 广播玩家回调
            playerPool.clear();
        }
        try{
            for (ConcurrentHashMap<Long,MatchPlayer> matchPlayers : playerPool.values()) {
                for ( MatchPlayer matchPlayer : matchPlayers.values()) {
                    if((System.currentTimeMillis()-matchPlayer.getStartMatchTime())> 15 * 1000){
                        //在匹配池中是时间太长，直接移除 15S
                        removePlayer(matchPlayer,true);
                        continue;
                    }
                }
            }
            for (ConcurrentHashMap<Long,MatchPlayer> sameRankPlayers: playerPool.values()) {
                boolean continueMatch = true;
                while(continueMatch){
                    //找出同一分数段里，等待时间最长的玩家，用他来匹配，因为他的区间最大
                    //如果他都不能匹配到，等待时间比他短的玩家更匹配不到
                    MatchPlayer oldest = null;
                    for (MatchPlayer playerMatchPoolInfo : sameRankPlayers.values()) {
                        if (oldest == null) {
                            oldest = playerMatchPoolInfo;
                            continue;
                        }
                        if (playerMatchPoolInfo.getStartMatchTime() < oldest.getStartMatchTime()) {
                            oldest = playerMatchPoolInfo;
                        }
                    }
                    if(oldest==null){
                        break;
                    }
                    List<MatchPlayer> matchPoolPlayer = new ArrayList<>();
                    //TODO 从此相同分段向两侧分段 扩大范围搜索  segmentSortList 是分段索引 算出玩家所属分段的索引 按照索引往两侧寻找即可

                    //CHECK 搜索结束 判断逻辑
                    if(matchPoolPlayer.size()==NEED_MATCH_PLAYER_COUNT){
                        log.debug(oldest.getPlayerId()+"|匹配到玩家数量够了|提交匹配成功处理");
                        //自己也匹配池移除
                        sameRankPlayers.remove(oldest);
                        //匹配成功处理
                        matchPoolPlayer.add(oldest);
                        //把配对的人提交匹配成功处理
                        matchSuccessProcess(matchPoolPlayer);
                    }else{
                        //本分数段等待时间最长的玩家都匹配不到，其他更不用尝试了
                        continueMatch = false;
                        log.debug(oldest.getPlayerId()+"|匹配到玩家数量不够，取消本次匹配");
                    }
                }
            }
        }catch (Exception e){
            log.error("匹配异常 ", e);
        }finally {
            startMatch();
        }
    }

    public void matchSuccessProcess(List<MatchPlayer> matchPoolPlayer) {
        for (MatchPlayer ma: matchPoolPlayer ) {
            removePlayer(ma,false);
            log.debug("匹配成功的玩家 " + ma.getPlayerId());
        }
    }

    @Override
    public void start(){
        initPool();
        startMatch();
    }

    public int calPlayerSegment(MatchPlayer matchPlayer){


        return 1;
    }

    private void initPool() {
        //TODO 初始化不同分段的 map


    }

    private void startMatch() {
        ScheduleSystemService scheduleSystemService = GlobalContext.getSystemService(ScheduleSystemService.class);
        scheduleSystemService.scheduleOnce(new ScheduleTask() {
            @Override
            public void doTask() {
                matchProcess();
            }
        },1000);
    }

    @Override
    public void stop() {
        stop = false;
    }

}
