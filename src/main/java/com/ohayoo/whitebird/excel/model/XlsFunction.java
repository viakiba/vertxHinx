package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.ohayoo.common.excel.convert.*;
import com.alibaba.excel.annotation.*;
import java.util.*;

/**
* 实体类
*/
@ExcelIgnoreUnannotated
public class XlsFunction implements AfterReadDo{
	/**
	* gid<cs>
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 0// 直接开启
1// 通关章节
	*/
	@ExcelProperty(value = "conditionType",converter = StringListIntegerConverter.class)
	private List<Integer> conditionType= new ArrayList<>();;

	/**
	* 参数1
	*/
	@ExcelProperty(value = "conditionValue1",converter = StringListIntegerConverter.class)
	private List<Integer> conditionValue1= new ArrayList<>();;

	/**
	* 参数2
	*/
	@ExcelProperty(value = "conditionValue2",converter = StringListIntegerConverter.class)
	private List<Integer> conditionValue2= new ArrayList<>();;


	/**
	* 每读完成一个sheet之后会调用此方法 此时遍历所有对象做check
	*/
	@Override
	public void afterAllSheetReadDo() {

	}

	/**
	* 会以此方法返回的id作为key（不可能是字符串吧）
	*/
	@Override
	public Integer getIdKey() {
		return gid;
	}

	public int getGid() {
		return gid;
	}

	public List<Integer> getConditionType() {
		return conditionType;
	}

	public List<Integer> getConditionValue1() {
		return conditionValue1;
	}

	public List<Integer> getConditionValue2() {
		return conditionValue2;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setConditionType(List<Integer> conditionType) {
		this.conditionType = conditionType;
	}

	public void setConditionValue1(List<Integer> conditionValue1) {
		this.conditionValue1 = conditionValue1;
	}

	public void setConditionValue2(List<Integer> conditionValue2) {
		this.conditionValue2 = conditionValue2;
	}
}