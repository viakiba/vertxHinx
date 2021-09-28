package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.ohayoo.common.excel.convert.*;
import com.alibaba.excel.annotation.*;
import java.util.*;

/**
* 实体类
*/
@ExcelIgnoreUnannotated
public class XlsMap implements AfterReadDo{
	/**
	* 关卡id
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 当前关卡对应的章节
	*/
	@ExcelProperty(value = "chapter")
	private int chapter;

	/**
	* 小关卡
	*/
	@ExcelProperty(value = "map")
	private int map;

	/**
	* 玩家的初始代币数
	*/
	@ExcelProperty(value = "CoinInitial",converter = StringListIntegerConverter.class)
	private List<Integer> coinInitial= new ArrayList<>();;

	/**
	* 玩家的代币增长速度（对应不同等级）
	*/
	@ExcelProperty(value = "CoinIncrease",converter = StringListIntegerConverter.class)
	private List<Integer> coinIncrease= new ArrayList<>();;

	/**
	* 玩家升级代币增长速率时消耗的代币数量
	*/
	@ExcelProperty(value = "CoinLevelUp",converter = StringListIntegerConverter.class)
	private List<Integer> coinLevelUp= new ArrayList<>();;

	/**
	* AI配置
	*/
	@ExcelProperty(value = "ai",converter = StringListIntegerConverter.class)
	private List<Integer> ai= new ArrayList<>();;

	/**
	* 资源能升级的最大等级
	*/
	@ExcelProperty(value = "MaxLevel")
	private int maxLevel;

	/**
	* 胜利方式：1=在关卡时间结束前摧毁所有敌人的基地。自己基地被摧毁立刻失败。
	*/
	@ExcelProperty(value = "WinType")
	private int winType;

	/**
	* 胜利方式：
	 * 1：参数含义：时间（单位：秒）
	*/
	@ExcelProperty(value = "WinTypeParameter",converter = StringListIntegerConverter.class)
	private List<Integer> winTypeParameter= new ArrayList<>();;

	/**
	* 模拟二位数组，初始角色的id、等级、坐标
	*/
	@ExcelProperty(value = "InitialCharacter",converter = StringListIntegerConverter.class)
	private List<Integer> initialCharacter= new ArrayList<>();;

	/**
	* 进入关卡需要消耗资源的ID
	*/
	@ExcelProperty(value = "resourceId")
	private int resourceId;

	/**
	* 当前关卡需要消耗的资源的数量
	*/
	@ExcelProperty(value = "energyNum")
	private int energyNum;

	/**
	* 当前关卡若失败/中途退出，玩家可被返还的资源数量
	*/
	@ExcelProperty(value = "energyReturn")
	private int energyReturn;

	@ExcelProperty(value = "drop",converter = StringListIntegerConverter.class)
	private List<Integer> drop= new ArrayList<>();;

	@ExcelProperty(value = "firstDrop",converter = StringListIntegerConverter.class)
	private List<Integer> firstDrop= new ArrayList<>();;

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

	public int getChapter() {
		return chapter;
	}

	public int getMap() {
		return map;
	}

	public List<Integer> getCoinInitial() {
		return coinInitial;
	}

	public List<Integer> getCoinIncrease() {
		return coinIncrease;
	}

	public List<Integer> getCoinLevelUp() {
		return coinLevelUp;
	}

	public List<Integer> getAi() {
		return ai;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getWinType() {
		return winType;
	}

	public List<Integer> getWinTypeParameter() {
		return winTypeParameter;
	}

	public List<Integer> getInitialCharacter() {
		return initialCharacter;
	}

	public int getResourceId() {
		return resourceId;
	}

	public int getEnergyNum() {
		return energyNum;
	}

	public int getEnergyReturn() {
		return energyReturn;
	}

	public List<Integer> getDrop() {
		return drop;
	}

	public List<Integer> getFirstDrop() {
		return firstDrop;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public void setCoinInitial(List<Integer> coinInitial) {
		this.coinInitial = coinInitial;
	}

	public void setCoinIncrease(List<Integer> coinIncrease) {
		this.coinIncrease = coinIncrease;
	}

	public void setCoinLevelUp(List<Integer> coinLevelUp) {
		this.coinLevelUp = coinLevelUp;
	}

	public void setAi(List<Integer> ai) {
		this.ai = ai;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public void setWinType(int winType) {
		this.winType = winType;
	}

	public void setWinTypeParameter(List<Integer> winTypeParameter) {
		this.winTypeParameter = winTypeParameter;
	}

	public void setInitialCharacter(List<Integer> initialCharacter) {
		this.initialCharacter = initialCharacter;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public void setEnergyNum(int energyNum) {
		this.energyNum = energyNum;
	}

	public void setEnergyReturn(int energyReturn) {
		this.energyReturn = energyReturn;
	}

	public void setDrop(List<Integer> drop) {
		this.drop = drop;
	}

	public void setFirstDrop(List<Integer> firstDrop) {
		this.firstDrop = firstDrop;
	}
}