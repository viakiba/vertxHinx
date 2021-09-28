package com.ohayoo.whitebird.service.compoent

import com.ohayoo.common.excel.ExcelDataService
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.excel.model.XlsFunction

object FunctionService {

    fun checkOpen(id : Int,gameData: GameData) : Boolean{
        var xlsFunction = ExcelDataService.getByClassAndId<XlsFunction>(XlsFunction::class.java, id)

        var conditionType = xlsFunction.conditionType
        if(conditionType[0] == 0 ){
            return true
        }
        for ( (index,type) in conditionType.withIndex()){
            var value1 = xlsFunction.conditionValue1[index]
            var value2 = xlsFunction.conditionValue2[index]
            if(!checkType(type,value1,value2,gameData)){
                return false
            }
        }
        return true
    }

    private fun checkType(type: Int, value1: Int, value2: Int, gameData: GameData): Boolean {
        //TODO Type策划未明确呢

        return true
    }
}