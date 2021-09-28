package com.ohayoo.whitebird.data.model.gamedata;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-09-06
 */
public class CardData {
    private Integer id;
    private Integer level = 0;
    private Integer fragmentNum = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFragmentNum() {
        return fragmentNum;
    }

    public void setFragmentNum(Integer fragmentNum) {
        this.fragmentNum = fragmentNum;
    }
}
