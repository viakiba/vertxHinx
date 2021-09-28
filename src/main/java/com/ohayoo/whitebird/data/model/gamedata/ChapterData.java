package com.ohayoo.whitebird.data.model.gamedata;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-09-13
 */
public class ChapterData {

    private int chapterNum;
    private int smallLevelNum;

    public int getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
    }

    public int getSmallLevelNum() {
        return smallLevelNum;
    }

    public void setSmallLevelNum(int smallLevelNum) {
        this.smallLevelNum = smallLevelNum;
    }

    @JsonIgnore
    public int getExcelChapterLevelNum(){
        return 108*10000+chapterNum*100*smallLevelNum;
    }
}
