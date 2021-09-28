package com.ohayoo.whitebird.excel.model;

import com.ohayoo.common.excel.AfterReadDo;
import com.ohayoo.common.excel.annotate.UnionAnnotate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.alibaba.excel.annotation.*;

/**
* 实体类
*/
@Data
@ToString
@NoArgsConstructor
@ExcelIgnoreUnannotated
@UnionAnnotate(type = "type")
public class XlsItem implements AfterReadDo{
	/**
	* 物品id
	*/
	@ExcelProperty(value = "gid")
	private int gid;

	/**
	* 技能名称
	*/
	@ExcelProperty(value = "name")
	private String name;

	/**
	* 物品种类
1=货币
2=整卡
3=碎片
4=基地升级材料
5=技能解锁
6=技能碎片
	*/
	@ExcelProperty(value = "type")
	private int type;

	/**
	* 稀有度
	*/
	@ExcelProperty(value = "rarity")
	private int rarity;

	/**
	* icon资源
	*/
	@ExcelProperty(value = "icon")
	private String icon;

	/**
	* 碎片对应的id
	*/
	@ExcelProperty(value = "pieceToItemId")
	private int pieceToItemId;

	/**
	* 物品对应的碎片id
	*/
	@ExcelProperty(value = "itemToPieceId")
	private int itemToPieceId;

	/**
	* 卡牌对应的碎片数量
	*/
	@ExcelProperty(value = "itemToPieceNum")
	private int itemToPieceNum;

	/**
	* 卡牌对应的碎片数量
	*/
	@ExcelProperty(value = "unlock")
	private int unlock;


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

	public int getType() {
		return type;
	}

	public int getRarity() {
		return rarity;
	}

	public String getIcon() {
		return icon;
	}

	public int getPieceToItemId() {
		return pieceToItemId;
	}

	public int getItemToPieceId() {
		return itemToPieceId;
	}

	public int getItemToPieceNum() {
		return itemToPieceNum;
	}

	public int getUnlock() {
		return unlock;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setPieceToItemId(int pieceToItemId) {
		this.pieceToItemId = pieceToItemId;
	}

	public void setItemToPieceId(int itemToPieceId) {
		this.itemToPieceId = itemToPieceId;
	}

	public void setItemToPieceNum(int itemToPieceNum) {
		this.itemToPieceNum = itemToPieceNum;
	}

	public void setUnlock(int unlock) {
		this.unlock = unlock;
	}
}