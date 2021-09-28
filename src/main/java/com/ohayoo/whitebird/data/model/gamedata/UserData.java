package com.ohayoo.whitebird.data.model.gamedata;

import java.util.Set;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-09-06
 */

// 基础信息
public class UserData {
    //昵称
    private String name;
    //头像
    private String icon;
    //建号时间
    private long createTime;
    //最近一次登录时间
    private long lastLoginTime;
    //下发的token
    private String token;
    //每日重置时间
    private long dailyResetTime;
    //最近一次签到时间
    private long renameLastTime = 0;
    //对局奖励签名
    protected String battleSign;
    //签到信息
    private int signNum = 0; //次数
    private long signTime = 0; //最近签到时间
    //万能卡牌碎片
    private int allPurposeCardPrice = 0;
    //最近一次体力刷新时间
    private long brawnRefreshTime;
    //基地ID
    private int towerId;
    //主技能ID
    private int skillId;

    private long commonFlag;

    private Set<Integer> newPlayerFlag;

    public Set<Integer> getNewPlayerFlag() {
        return newPlayerFlag;
    }

    public void setNewPlayerFlag(Set<Integer> newPlayerFlag) {
        this.newPlayerFlag = newPlayerFlag;
    }

    public long getCommonFlag() {
        return commonFlag;
    }

    public void setCommonFlag(long commonFlag) {
        this.commonFlag = commonFlag;
    }

    public int getTowerId() {
        return towerId;
    }

    public void setTowerId(int towerId) {
        this.towerId = towerId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getAllPurposeCardPrice() {
        return allPurposeCardPrice;
    }

    public void setAllPurposeCardPrice(int allPurposeCardPrice) {
        this.allPurposeCardPrice = allPurposeCardPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getDailyResetTime() {
        return dailyResetTime;
    }

    public void setDailyResetTime(long dailyResetTime) {
        this.dailyResetTime = dailyResetTime;
    }

    public long getRenameLastTime() {
        return renameLastTime;
    }

    public void setRenameLastTime(long renameLastTime) {
        this.renameLastTime = renameLastTime;
    }

    public String getBattleSign() {
        return battleSign;
    }

    public void setBattleSign(String battleSign) {
        this.battleSign = battleSign;
    }

    public int getSignNum() {
        return signNum;
    }

    public void setSignNum(int signNum) {
        this.signNum = signNum;
    }

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getBrawnRefreshTime() {
        return brawnRefreshTime;
    }

    public void setBrawnRefreshTime(long brawnRefreshTime) {
        this.brawnRefreshTime = brawnRefreshTime;
    }
}
