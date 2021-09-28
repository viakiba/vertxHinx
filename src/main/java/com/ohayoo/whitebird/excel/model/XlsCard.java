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
public class XlsCard implements AfterReadDo{
	/**
	* 卡牌id
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 卡牌名称
	*/
	@ExcelProperty(value = "name")
	private String name;

	/**
	* 是否可收集
1=可以
2=不可以
	*/
	@ExcelProperty(value = "collection")
	private int collection;

	/**
	* 卡牌稀有度
1=普通
2=稀有
3=史诗
4=传奇
	*/
	@ExcelProperty(value = "rarity")
	private int rarity;

	/**
	* 召唤的角色id
	*/
	@ExcelProperty(value = "character_Id")
	private int character_Id;

	/**
	* 召唤的角色数量
	*/
	@ExcelProperty(value = "character_Number")
	private int character_Number;

	/**
	* 使用卡牌的消耗
	*/
	@ExcelProperty(value = "coin_Cost")
	private int coin_Cost;

	/**
	* 使用后的冷却时间,ms
	*/
	@ExcelProperty(value = "use_CoolDown")
	private int use_CoolDown;

	/**
	* 初始冷却时间,ms
	*/
	@ExcelProperty(value = "initial_CoolDown")
	private int initial_CoolDown;

	/**
	* 卡牌图像路径
	*/
	@ExcelProperty(value = "pic_Path")
	private String pic_Path;

	/**
	* 战中长按卡牌出现的描述
	*/
	@ExcelProperty(value = "desc")
	private String desc;

	/**
	* 枚举，initialState=0时，表示初始未获得的卡牌，
initialState=1时，初始化给的卡牌（包含2）
initialState=2时，初始化时上阵的卡牌；
	*/
	@ExcelProperty(value = "initialState")
	private int initialState;

	/**
	* 表示该卡牌的初始等级，最小卡牌等级为1
	*/
	@ExcelProperty(value = "initialLev")
	private int initialLev;

	@ExcelProperty(value = "cardFragmentId")
	private int cardFragmentId;

	@ExcelProperty(value = "cardCost",converter = StringListIntegerConverter.class)
	private List<Integer> cardCost = new ArrayList<>();

	@ExcelProperty(value = "coinCost",converter = StringListIntegerConverter.class)
	private List<Integer> coinCost = new ArrayList<>();

	/**
	 * 表示该卡牌能升至的最大等级
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

	public int getCollection() {
		return collection;
	}

	public void setCollection(int collection) {
		this.collection = collection;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public int getCharacter_Id() {
		return character_Id;
	}

	public void setCharacter_Id(int character_Id) {
		this.character_Id = character_Id;
	}

	public int getCharacter_Number() {
		return character_Number;
	}

	public void setCharacter_Number(int character_Number) {
		this.character_Number = character_Number;
	}

	public int getCoin_Cost() {
		return coin_Cost;
	}

	public void setCoin_Cost(int coin_Cost) {
		this.coin_Cost = coin_Cost;
	}

	public int getUse_CoolDown() {
		return use_CoolDown;
	}

	public void setUse_CoolDown(int use_CoolDown) {
		this.use_CoolDown = use_CoolDown;
	}

	public int getInitial_CoolDown() {
		return initial_CoolDown;
	}

	public void setInitial_CoolDown(int initial_CoolDown) {
		this.initial_CoolDown = initial_CoolDown;
	}

	public String getPic_Path() {
		return pic_Path;
	}

	public void setPic_Path(String pic_Path) {
		this.pic_Path = pic_Path;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getInitialState() {
		return initialState;
	}

	public void setInitialState(int initialState) {
		this.initialState = initialState;
	}

	public int getInitialLev() {
		return initialLev;
	}

	public void setInitialLev(int initialLev) {
		this.initialLev = initialLev;
	}

	public int getMaxLev() {
		return maxLev;
	}

	public void setMaxLev(int maxLev) {
		this.maxLev = maxLev;
	}

	public int getCardFragmentId() {
		return cardFragmentId;
	}

	public void setCardFragmentId(int cardFragmentId) {
		this.cardFragmentId = cardFragmentId;
	}

	public List<Integer> getCardCost() {
		return cardCost;
	}

	public void setCardCost(List<Integer> cardCost) {
		this.cardCost = cardCost;
	}

	public List<Integer> getCoinCost() {
		return coinCost;
	}

	public void setCoinCost(List<Integer> coinCost) {
		this.coinCost = coinCost;
	}
}