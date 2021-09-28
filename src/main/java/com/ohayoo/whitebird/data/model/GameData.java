package com.ohayoo.whitebird.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ohayoo.whitebird.data.model.gamedata.*;

import java.util.List;
import java.util.Map;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class GameData {

    @JsonProperty("_id")
    private Long id;
    private String openId; //平台ID
    private UserData userData; //基础信息
    private BdData bdData; // 平台数据 不用同步给客户端 自身内部使用
    private Map<Integer,Long> currencyDataMap; //货币信息
    private Map<Integer, CardData> cardDataMap; //持有卡牌信息
    private List<Integer> cardPositionDataList; //卡牌上阵信息
    private Map<Integer,TowerData> towerDataMap; //基地数据
    private Map<Integer, SkillData> skillDataMap; //技能数据
    private ChapterData chapterData; //章节对局信息
    private Map<Integer,ShopData> shopData; //商城购物信息
    private List<Integer> mainTaskDataList; //主线任务 index 0 : 任务id  index 1 : 数量 index

    public List<Integer> getMainTaskDataList() {
        return mainTaskDataList;
    }

    public void setMainTaskDataList(List<Integer> mainTaskDataList) {
        this.mainTaskDataList = mainTaskDataList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public BdData getBdData() {
        return bdData;
    }

    public void setBdData(BdData bdData) {
        this.bdData = bdData;
    }

    public Map<Integer, Long> getCurrencyDataMap() {
        return currencyDataMap;
    }

    public void setCurrencyDataMap(Map<Integer, Long> currencyDataMap) {
        this.currencyDataMap = currencyDataMap;
    }

    public Map<Integer, CardData> getCardDataMap() {
        return cardDataMap;
    }

    public void setCardDataMap(Map<Integer, CardData> cardDataMap) {
        this.cardDataMap = cardDataMap;
    }

    public List<Integer> getCardPositionDataList() {
        return cardPositionDataList;
    }

    public void setCardPositionDataList(List<Integer> cardPositionDataList) {
        this.cardPositionDataList = cardPositionDataList;
    }

    public Map<Integer, TowerData> getTowerDataMap() {
        return towerDataMap;
    }

    public void setTowerDataMap(Map<Integer, TowerData> towerDataMap) {
        this.towerDataMap = towerDataMap;
    }

    public Map<Integer, SkillData> getSkillDataMap() {
        return skillDataMap;
    }

    public void setSkillDataMap(Map<Integer, SkillData> skillDataMap) {
        this.skillDataMap = skillDataMap;
    }

    public ChapterData getChapterData() {
        return chapterData;
    }

    public void setChapterData(ChapterData chapterData) {
        this.chapterData = chapterData;
    }

    public Map<Integer, ShopData> getShopData() {
        return shopData;
    }

    public void setShopData(Map<Integer, ShopData> shopData) {
        this.shopData = shopData;
    }
}
