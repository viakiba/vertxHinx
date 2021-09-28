package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.ohayoo.common.excel.convert.*;
import com.alibaba.excel.annotation.*;
import java.util.*;

/**
* 实体类
*/
@ExcelIgnoreUnannotated
public class XlsDrop implements AfterReadDo{
	/**
	* 掉落id
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 掉落名称
	*/
	@ExcelProperty(value = "name")
	private String name;

	/**
	* 掉多少张完整的卡
	*/
	@ExcelProperty(value = "completeCardNumber")
	private int completeCardNumber;

	/**
	* 不同稀有度的掉落权重
	*/
	@ExcelProperty(value = "completeCardWeight",converter = StringListIntegerConverter.class)
	private List<Integer> completeCardWeight =new ArrayList<>();

	/**
	* 该次掉落中必定包含的完整卡的id
	*/
	@ExcelProperty(value = "completeCardGuaranteed",converter = StringListIntegerConverter.class)
	private List<Integer> completeCardGuaranteed =new ArrayList<>();

	/**
	* 本次掉落时，整卡部分是否去重掉落
	*/
	@ExcelProperty(value = "completeCardisUnique")
	private int completeCardisUnique;

	/**
	* 完整卡的卡池枚举。
约定：1：当前版本开放的所有卡牌(同稀有度下每张卡等权重)
2：玩家当前拥有的所有卡牌(同稀有度下每张卡等权重，如果玩家没有该稀有度的卡则在玩家拥有的稀有度中，选择不大于该稀有度的最大稀有度中随机一张)
3：玩家当前解锁的所有卡牌
	*/
	@ExcelProperty(value = "completeCardPool")
	private int completeCardPool;

	/**
	* 卡牌碎片：掉落概率、掉落下限、掉落上限、分几堆、是否去重掉落
	*/
	@ExcelProperty(value = "cardPiece",converter = StringListIntegerConverter2.class)
	private List<List<Integer>> cardPiece = new ArrayList<>();

	/**
	* 卡牌碎片的卡池枚举
	*/
	@ExcelProperty(value = "cardPiecePool")
	private int cardPiecePool;

	/**
	* 物品id、掉落概率、掉落下限，掉落上限
	*/
	@ExcelProperty(value = "itemDrop",converter = StringListIntegerConverter2.class)
	private List<List<Integer>> itemDrop= new ArrayList<>();


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

	public String getName() {
		return name;
	}

	public int getCompleteCardNumber() {
		return completeCardNumber;
	}

	public List<Integer> getCompleteCardWeight() {
		return completeCardWeight;
	}

	public List<Integer> getCompleteCardGuaranteed() {
		return completeCardGuaranteed;
	}

	public int getCompleteCardisUnique() {
		return completeCardisUnique;
	}

	public int getCompleteCardPool() {
		return completeCardPool;
	}

	public List<List<Integer>> getCardPiece() {
		return cardPiece;
	}

	public int getCardPiecePool() {
		return cardPiecePool;
	}

	public List<List<Integer>> getItemDrop() {
		return itemDrop;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCompleteCardNumber(int completeCardNumber) {
		this.completeCardNumber = completeCardNumber;
	}

	public void setCompleteCardWeight(List<Integer> completeCardWeight) {
		this.completeCardWeight = completeCardWeight;
	}

	public void setCompleteCardGuaranteed(List<Integer> completeCardGuaranteed) {
		this.completeCardGuaranteed = completeCardGuaranteed;
	}

	public void setCompleteCardisUnique(int completeCardisUnique) {
		this.completeCardisUnique = completeCardisUnique;
	}

	public void setCompleteCardPool(int completeCardPool) {
		this.completeCardPool = completeCardPool;
	}

	public void setCardPiece(List<List<Integer>> cardPiece) {
		this.cardPiece = cardPiece;
	}

	public void setCardPiecePool(int cardPiecePool) {
		this.cardPiecePool = cardPiecePool;
	}

	public void setItemDrop(List<List<Integer>> itemDrop) {
		this.itemDrop = itemDrop;
	}

}