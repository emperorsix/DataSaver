package com.iweipeng.mapper;

import java.util.List;
import java.util.Map;

import com.iweipeng.annotation.DataSource;
import com.weiju.common.model.WJStockDB;

public interface DataTableMapper extends BaseMapper<WJStockDB, String> {

	@DataSource("dataSourceWeiJu1")
	public List<WJStockDB> findSourceTableData(Map<String, Object> params);

	@DataSource("dataSourceWeiJu1")
	public void createTable(Map<String, Object> params);

	@DataSource("dataSourceWpstock")
	public void importDataToTable(Map<String, Object> params);
}
