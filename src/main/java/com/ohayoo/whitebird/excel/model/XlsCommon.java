package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.alibaba.excel.annotation.*;

/**
* 实体类
*/
@ExcelIgnoreUnannotated
public class XlsCommon implements AfterReadDo{
	/**
	* gid<cs>
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 值<cs>
	*/
	@ExcelProperty(value = "value")
	private String value;


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

	public String getValue() {
		return value;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setValue(String value) {
		this.value = value;
	}
}