package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.ohayoo.common.excel.convert.*;
import com.alibaba.excel.annotation.*;
import java.util.*;

/**
* 实体类
*/
@ExcelIgnoreUnannotated
public class XlsLeaderSkill implements AfterReadDo{
	/**
	* 技能id
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 技能稀有度
1=普通
2=稀有
3=史诗
4=传奇
	*/
	@ExcelProperty(value = "rarity")
	private int rarity;

	/**
	* 使用后的冷却时间,ms
	*/
	@ExcelProperty(value = "useCoolDown")
	private int useCoolDown;

	/**
	* 初始冷却时间,ms
	*/
	@ExcelProperty(value = "initialCoolDown")
	private int initialCoolDown;

	/**
	* 主塔升级所需要货币资源的id
	*/
	@ExcelProperty(value = "moneyId")
	private int moneyId;

	/**
	* 技能由当前等级升至下一等级所需要消耗的货币数量
	*/
	@ExcelProperty(value = "coinCost",converter = StringListIntegerConverter.class)
	private List<Integer> coinCost;

	/**
	* 技能升级所需要其他资源的id
	*/
	@ExcelProperty(value = "resourceId")
	private int resourceId;

	/**
	* 技能由当前等级升至下一等级所需要消耗的资源碎片数量
	*/
	@ExcelProperty(value = "resourceCost",converter = StringListIntegerConverter.class)
	private List<Integer> resourceCost;

	/**
	* 技能能升到的最大等级
	*/
	@ExcelProperty(value = "maxLev")
	private int maxLev;


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

	public void setGid(int gid) {
		this.gid = gid;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public int getUseCoolDown() {
		return useCoolDown;
	}

	public void setUseCoolDown(int useCoolDown) {
		this.useCoolDown = useCoolDown;
	}

	public int getInitialCoolDown() {
		return initialCoolDown;
	}

	public void setInitialCoolDown(int initialCoolDown) {
		this.initialCoolDown = initialCoolDown;
	}

	public int getMoneyId() {
		return moneyId;
	}

	public void setMoneyId(int moneyId) {
		this.moneyId = moneyId;
	}

	public List<Integer> getCoinCost() {
		return coinCost;
	}

	public void setCoinCost(List<Integer> coinCost) {
		this.coinCost = coinCost;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public List<Integer> getResourceCost() {
		return resourceCost;
	}

	public void setResourceCost(List<Integer> resourceCost) {
		this.resourceCost = resourceCost;
	}

	public int getMaxLev() {
		return maxLev;
	}

	public void setMaxLev(int maxLev) {
		this.maxLev = maxLev;
	}
}