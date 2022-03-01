package io.github.viakiba.hinx.excel.model;

import org.viakiba.exceltool.AfterReadDo;
import org.viakiba.exceltool.convert.*;
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
public class DataExcel implements AfterReadDo{
	/**
	* id
	*/
	@ExcelProperty(value = "id")
	private int id;

	/**
	* 对应名称
	*/
	@ExcelProperty(value = "name")
	private String name;


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
	public Object getIdKey() {
		return id;
	}
}