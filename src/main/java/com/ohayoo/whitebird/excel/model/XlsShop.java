package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.ohayoo.common.excel.convert.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.alibaba.excel.annotation.*;
import java.util.*;

/**
* 实体类
*/
@Data
@ToString
@NoArgsConstructor
@ExcelIgnoreUnannotated
public class XlsShop implements AfterReadDo{
	/**
	* 商品ID
1=卡包
2=金币宝箱
3=钻石宝箱
4=材料宝箱
5=混合宝箱
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 商品解锁条件id
functionUnlock表
	*/
	@ExcelProperty(value = "functionUnlock")
	private int functionUnlock;

	/**
	* 购买该宝箱需要消耗的资源类型，枚举，1=广告，2=物品,配置在前的优先级高
	*/
	@ExcelProperty(value = "costType",converter = StringListIntegerConverter.class)
	private List<Integer> costType= new ArrayList<>();;

	/**
	* 购买该宝箱需要消耗的资源ID，资源id从114表获取，广告填0
	*/
	@ExcelProperty(value = "costId",converter = StringListIntegerConverter.class)
	private List<Integer> costId= new ArrayList<>();;

	/**
	* 购买宝箱需要消耗的资源的数量
	*/
	@ExcelProperty(value = "costNum",converter = StringListIntegerConverter.class)
	private List<Integer> costNum= new ArrayList<>();;

	/**
	* 宝箱购买后至下一次购买的冷却时间，单位：S
与costType一一对应，表示不同种类的资源对应的冷却时间
-1使用对应资源购买时，没有冷却时间
	*/
	@ExcelProperty(value = "adCoolDown")
	private int adCoolDown;

	/**
	* 表示该宝箱在一定时间内消耗各种种类资源可购买最大次数，与costType一一对应；
	*/
	@ExcelProperty(value = "adBuyLimit")
	private int adBuyLimit;

	/**
	* 表示每种资源对应的购买限制对应的刷新时间，与从costType一一对应
	*/
	@ExcelProperty(value = "adRefreshTime")
	private int adRefreshTime;

	/**
	* 每个周期免费次数
	*/
	@ExcelProperty(value = "freeLimit")
	private int freeLimit;

	/**
	* 免费周期重置时间，单位小时
	*/
	@ExcelProperty(value = "freeRefreshTime")
	private int freeRefreshTime;

	/**
	* 二维数组，表示该宝箱对应的掉落ID,与章节进度一一对应
	*/
	@ExcelProperty(value = "#dropId",converter = StringListIntegerConverter2.class)
	private List<List<Integer>> dropId= new ArrayList<>();;

	/**
	* 二维数组，首位为指定掉落的次数，第二位为该次掉落的掉落id
	*/
	@ExcelProperty(value = "#specialDrop",converter = StringListIntegerConverter2.class)
	private List<List<Integer>> specialDrop= new ArrayList<>();;

	/**
	* 保底的最小次数（保底次数内必出，但不一定是该次数出）
	*/
	@ExcelProperty(value = "min")
	private int min;

	/**
	* 保底策略触发需要检测的物品的品质,卡牌品质
1=普通，2=稀有，3=史诗，4=传奇
	*/
	@ExcelProperty(value = "rarity")
	private int rarity;

	/**
	* 触发保底规则时，从该掉落池中获取可掉落的ID
	*/
	@ExcelProperty(value = "minPool",converter = StringListIntegerConverter.class)
	private List<Integer> minPool= new ArrayList<>();;


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

	public int getFunctionUnlock() {
		return functionUnlock;
	}

	public List<Integer> getCostType() {
		return costType;
	}

	public List<Integer> getCostId() {
		return costId;
	}

	public List<Integer> getCostNum() {
		return costNum;
	}

	public int getAdCoolDown() {
		return adCoolDown;
	}

	public int getAdBuyLimit() {
		return adBuyLimit;
	}

	public int getAdRefreshTime() {
		return adRefreshTime;
	}

	public int getFreeLimit() {
		return freeLimit;
	}

	public int getFreeRefreshTime() {
		return freeRefreshTime;
	}

	public List<List<Integer>> getDropId() {
		return dropId;
	}

	public List<List<Integer>> getSpecialDrop() {
		return specialDrop;
	}

	public int getMin() {
		return min;
	}

	public int getRarity() {
		return rarity;
	}

	public List<Integer> getMinPool() {
		return minPool;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setFunctionUnlock(int functionUnlock) {
		this.functionUnlock = functionUnlock;
	}

	public void setCostType(List<Integer> costType) {
		this.costType = costType;
	}

	public void setCostId(List<Integer> costId) {
		this.costId = costId;
	}

	public void setCostNum(List<Integer> costNum) {
		this.costNum = costNum;
	}

	public void setAdCoolDown(int adCoolDown) {
		this.adCoolDown = adCoolDown;
	}

	public void setAdBuyLimit(int adBuyLimit) {
		this.adBuyLimit = adBuyLimit;
	}

	public void setAdRefreshTime(int adRefreshTime) {
		this.adRefreshTime = adRefreshTime;
	}

	public void setFreeLimit(int freeLimit) {
		this.freeLimit = freeLimit;
	}

	public void setFreeRefreshTime(int freeRefreshTime) {
		this.freeRefreshTime = freeRefreshTime;
	}

	public void setDropId(List<List<Integer>> dropId) {
		this.dropId = dropId;
	}

	public void setSpecialDrop(List<List<Integer>> specialDrop) {
		this.specialDrop = specialDrop;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public void setMinPool(List<Integer> minPool) {
		this.minPool = minPool;
	}
}