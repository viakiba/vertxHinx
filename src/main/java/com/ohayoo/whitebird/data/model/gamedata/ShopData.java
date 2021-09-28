package com.ohayoo.whitebird.data.model.gamedata;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-09-15
 */
public class ShopData {

    private int id;

    private long adCdLastSecond;

    private int adBuyNum;

    private int freeBuyNum;

    private long freeLastRefreshSecond;

    private int specialDropNum;

    private int minimumGuaranteeNum;

    public ShopData() {

    }

    public ShopData(int id, long adCdLastSecond, int adBuyNum,
                    int freeBuyNum, long freeLastRefreshSecond,
                    int specialDropNum, int minimumGuaranteeNum) {
        this.id = id;
        this.adCdLastSecond = adCdLastSecond;
        this.adBuyNum = adBuyNum;
        this.freeBuyNum = freeBuyNum;
        this.freeLastRefreshSecond = freeLastRefreshSecond;
        this.specialDropNum = specialDropNum;
        this.minimumGuaranteeNum = minimumGuaranteeNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAdCdLastSecond() {
        return adCdLastSecond;
    }

    public void setAdCdLastSecond(long adCdLastSecond) {
        this.adCdLastSecond = adCdLastSecond;
    }

    public int getAdBuyNum() {
        return adBuyNum;
    }

    public void setAdBuyNum(int adBuyNum) {
        this.adBuyNum = adBuyNum;
    }

    public int getFreeBuyNum() {
        return freeBuyNum;
    }

    public void setFreeBuyNum(int freeBuyNum) {
        this.freeBuyNum = freeBuyNum;
    }

    public long getFreeLastRefreshSecond() {
        return freeLastRefreshSecond;
    }

    public void setFreeLastRefreshSecond(long freeLastRefreshSecond) {
        this.freeLastRefreshSecond = freeLastRefreshSecond;
    }

    public int getSpecialDropNum() {
        return specialDropNum;
    }

    public void setSpecialDropNum(int specialDropNum) {
        this.specialDropNum = specialDropNum;
    }

    public int getMinimumGuaranteeNum() {
        return minimumGuaranteeNum;
    }

    public void setMinimumGuaranteeNum(int minimumGuaranteeNum) {
        this.minimumGuaranteeNum = minimumGuaranteeNum;
    }
}
