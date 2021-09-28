package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.ohayoo.common.excel.convert.*;
import com.alibaba.excel.annotation.*;
import java.util.*;

/**
* 实体类
*/
@ExcelIgnoreUnannotated
public class XlsTower implements AfterReadDo{
	/**
	* 基地id
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 角色名称
	*/
	@ExcelProperty(value = "name")
	private String name;

	/**
	* 血量
	*/
	@ExcelProperty(value = "hp",converter = StringListIntegerConverter.class)
	private List<Integer> hp;

	/**
	* 攻击力
	*/
	@ExcelProperty(value = "atk",converter = StringListIntegerConverter.class)
	private List<Integer> atk;

	/**
	* 暴击概率：1=0.1%
	*/
	@ExcelProperty(value = "crit_Chance")
	private int crit_Chance;

	/**
	* 暴击倍率：1=0.1%
	*/
	@ExcelProperty(value = "crit_Ratio")
	private int crit_Ratio;

	/**
	* 基地的普攻技能id
	*/
	@ExcelProperty(value = "attackSkillId")
	private int attackSkillId;

	/**
	* 基地拥有的技能id
	*/
	@ExcelProperty(value = "activeSkillId",converter = StringListIntegerConverter.class)
	private List<Integer> activeSkillId;

	/**
	* 基地模型路径
	*/
	@ExcelProperty(value = "model_path")
	private String model_path;

	/**
	* 攻击速度,x毫秒攻击一次
	*/
	@ExcelProperty(value = "atkSpeed")
	private int atkSpeed;

	/**
	* 主塔升级所需要货币资源的id
	*/
	@ExcelProperty(value = "moneyId")
	private int moneyId;

	/**
	* 主塔由当前等级升至下一等级所需要消耗的货币数量
	*/
	@ExcelProperty(value = "moneyCost",converter = StringListIntegerConverter.class)
	private List<Integer> moneyCost;

	/**
	* 主塔由当前等级升至下一等级所需要消耗的碎片ID
	*/
	@ExcelProperty(value = "resourceId")
	private int resourceId;

	/**
	* 主塔由当前等级升至下一等级所需要消耗的资源碎片数量
	*/
	@ExcelProperty(value = "resourceCost",converter = StringListIntegerConverter.class)
	private List<Integer> resourceCost;

	/**
	* 主塔能升到的最大等级
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getHp() {
		return hp;
	}

	public void setHp(List<Integer> hp) {
		this.hp = hp;
	}

	public List<Integer> getAtk() {
		return atk;
	}

	public void setAtk(List<Integer> atk) {
		this.atk = atk;
	}

	public int getCrit_Chance() {
		return crit_Chance;
	}

	public void setCrit_Chance(int crit_Chance) {
		this.crit_Chance = crit_Chance;
	}

	public int getCrit_Ratio() {
		return crit_Ratio;
	}

	public void setCrit_Ratio(int crit_Ratio) {
		this.crit_Ratio = crit_Ratio;
	}

	public int getAttackSkillId() {
		return attackSkillId;
	}

	public void setAttackSkillId(int attackSkillId) {
		this.attackSkillId = attackSkillId;
	}

	public List<Integer> getActiveSkillId() {
		return activeSkillId;
	}

	public void setActiveSkillId(List<Integer> activeSkillId) {
		this.activeSkillId = activeSkillId;
	}

	public String getModel_path() {
		return model_path;
	}

	public void setModel_path(String model_path) {
		this.model_path = model_path;
	}

	public int getAtkSpeed() {
		return atkSpeed;
	}

	public void setAtkSpeed(int atkSpeed) {
		this.atkSpeed = atkSpeed;
	}

	public int getMoneyId() {
		return moneyId;
	}

	public void setMoneyId(int moneyId) {
		this.moneyId = moneyId;
	}

	public List<Integer> getMoneyCost() {
		return moneyCost;
	}

	public void setMoneyCost(List<Integer> moneyCost) {
		this.moneyCost = moneyCost;
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