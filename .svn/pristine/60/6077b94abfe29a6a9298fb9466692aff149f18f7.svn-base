package com.iweipeng.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweipeng.mapper.DataTableMapper;
import com.iweipeng.service.DataTableService;
import com.iweipeng.utility.AttributeUtils;
import com.weiju.common.model.WJStockDB;

@Service
public class DataTableServiceImpl implements DataTableService {

	@Autowired
	private DataTableMapper	dataTableMapper;

	private Logger			logger	= LoggerFactory.getLogger(this.getClass());

	@Override
	public List<WJStockDB> getDataByTableBeforeDate(String tableName, Date dbDate) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("tableName", tableName);
		params.put("dbDate", "'" + DateFormatUtils.format(dbDate, "yyyy-MM-dd hh:mm:sss") + "'");
		return dataTableMapper.findSourceTableData(params);
	}

	@Override
	public void createTableByParams(String tableName, Map<String, Object> columnMap) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("createTableSQL", "create table " + tableName + " ()");
		dataTableMapper.createTable(params);
	}

	@Override
	public void loadData(String tableName, String filePath, List<String> columns) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		params.put("filePath", AttributeUtils.getStringName(filePath));
		for (String col : columns) {
			logger.debug("column: " + col);
		}
		params.put("columns", columns);
		dataTableMapper.importDataToTable(params);
	}

}
