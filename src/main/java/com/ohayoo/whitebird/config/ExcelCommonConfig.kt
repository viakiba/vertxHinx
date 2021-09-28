package com.ohayoo.whitebird.config

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.excel.model.XlsCommon

object ExcelCommonConfig {
    private val defaultItemIdList = mutableListOf<Int>()
    private val defaultItemNumList =  mutableListOf<Int>()
    private var maxChapterNum =  0
    private var maxBrawn =  0
    private var refreshBrawnTimeMS =  0
    private var defaultName =  ""
    private var defaultIcon =  ""
    private var defaultChapter =  0
    private var defaultChapterSmallLevel =  0
    private var renameCdMS =  0
    private var defaultTowerId =  0
    private var defaultSkillId =  0
    private var defaultTwoDayBonusId =  0

    fun initConstant() {
    }

    fun getMaxChapterNum(): Int {
       return maxChapterNum
    }

    fun getMaxBrawn(): Int {
       return maxBrawn
    }

    fun getRefreshBrawnTimeMs(): Int {
       return refreshBrawnTimeMS
    }

    fun getDefaultName(): String {
       return defaultName
    }

    fun getDefaultIcon(): String {
       return defaultIcon
    }

    fun getDefaultChapter(): Int {
       return defaultChapter
    }

    fun getDefaultChapterSmallLevel(): Int {
       return defaultChapterSmallLevel
    }

    fun getRenameCdMS(): Int {
       return renameCdMS
    }

    fun getDefaultItemId(): MutableList<Int> {
       return defaultItemIdList;
    }

    fun getDefaultItemNum(): MutableList<Int> {
       return defaultItemNumList
    }

    fun getDefaultTowerId(): Int {
        return defaultTowerId
    }

    fun getDefaultSkillId(): Int {
        return defaultSkillId
    }

    fun getDefaultTwoDayBonusId(): Int {
        return defaultTwoDayBonusId
    }

}