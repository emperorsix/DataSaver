package com.iweipeng.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.weiju.common.model.WJStockDB;

public interface DataTableService {

	/**
	 * 根据数据表名查询提定日期之后的数据
	 * 
	 * @param tableName
	 * @param dbDate 查询这个目期之后的数据
	 * @return
	 */
	List<WJStockDB> getDataByTableBeforeDate(String tableName, Date dbDate);

	void createTableByParams(String tableName, Map<String, Object> columnMap);

	/**
	 * 使用MYSQL Load File方式导入数据到数据表中
	 * 
	 * @param tableName
	 *            数据表名
	 * @param filePath
	 *            文件名称必须为一个文字字符串
	 * @param columns
	 *            数据表映射列名
	 */
	void loadData(String tableName, String filePath, List<String> columns);

}
