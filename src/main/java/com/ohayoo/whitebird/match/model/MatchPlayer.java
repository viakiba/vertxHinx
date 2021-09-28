package com.ohayoo.whitebird.match.model;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-10
 */
public class MatchPlayer {

    private long playerId;
    private int score;
    private long startMatchTime;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getStartMatchTime() {
        return startMatchTime;
    }

    public void setStartMatchTime(long startMatchTime) {
        this.startMatchTime = startMatchTime;
    }
}
