package com.iweipeng.runner;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iweipeng.service.DataTableService;
import com.iweipeng.service.RedisMappingService;
import com.iweipeng.utility.DateUtils;

/**
 * 
 * 转换数据线程执行器
 * 
 * @author qingfeng
 *
 */
public class TaskRunner implements Runnable {

	private static Logger									logger			= LoggerFactory.getLogger(TaskRunner.class);

	private static final String								REDIS_TEM_DIR	= "/redisTmpData/";

	/**
	 * {tableName=columns}
	 */
	private Entry<String, List<Map<String, List<String>>>>	tableColumnMap;

	private DataTableService								dataTableService;

	private RedisMappingService								redisMappingService;

	public TaskRunner(Entry<String, List<Map<String, List<String>>>> item, DataTableService dataTableService,
			RedisMappingService redisMappingService) {
		this.tableColumnMap = item;
		this.dataTableService = dataTableService;
		this.redisMappingService = redisMappingService;
	}

	@Override
	public void run() {
		// tableName_{mapId}
		String tableNameIdr = tableColumnMap.getKey();
		if (StringUtils.isEmpty(tableNameIdr)) {
			logger.warn("解析出错，没有找到目标数据表。");
			return;
		}

		// Date now = Calendar.getInstance().getTime();

		List<Map<String, List<String>>> columnDataTmpMap = tableColumnMap.getValue();
		List<String[]> allRowList = new ArrayList<String[]>();
		Iterator<Map<String, List<String>>> tempIter = columnDataTmpMap.iterator();
		List<String> columnNameList = new ArrayList<String>();
		boolean haveNames = false;
		while (tempIter.hasNext()) {

			Map<String, List<String>> columnDataMap = tempIter.next();
			// 列数
			int columnLength = columnDataMap.size();
			if (columnLength == 0) {
				continue;
			}
			// LOAD DATA INFILE 'data.txt' INTO TABLE data_table
			// (col1,col2,...);
			Set<Entry<String, List<String>>> columnMap = columnDataMap.entrySet();
			List<String[]> rowList = new ArrayList<String[]>();
			int maxSize = 0;
			for (Entry<String, List<String>> columns : columnMap) {
				List<String> values = columns.getValue();
				if (maxSize < values.size()) {
					maxSize = values.size();
				}
			}

			for (int i = 0; i < maxSize; i++) {
				rowList.add(new String[columnLength]);
			}

			int columnIndex = 0;

			for (Entry<String, List<String>> columns : columnMap) {
				//
				List<String> columnValues = columns.getValue();
				if (!haveNames) {
					columnNameList.add(columns.getKey());
				}
				String lastValue = null;
				int rowIndex = 0;
				for (int i = 0; i < maxSize; i++) {
					logger.debug("maxSize: " + maxSize + " rowIndex: " + rowIndex + " columnIndex: " + columnIndex);
					String[] row = rowList.get(rowIndex++);
					if (columnValues.size() <= i) {
						row[columnIndex] = lastValue;
					} else {
						row[columnIndex] = columnValues.get(i);
						lastValue = columnValues.get(i);
					}
				}
				columnIndex++;
			}
			haveNames = true;
			allRowList.addAll(rowList);
		}

		StringBuffer rowDataStr = new StringBuffer();

		for (String[] row : allRowList) {
			int i = 0;
			for (String fieldVal : row) {
				rowDataStr.append(fieldVal);
				if (i != row.length - 1) {
					rowDataStr.append(",");
				}
				i++;
			}
			rowDataStr.append("\n");
		}
		String[] tableNameIdrArr = tableNameIdr.split("@");
		if (tableNameIdrArr.length > 2) {
			final String tableName = tableNameIdrArr[0];
			logger.debug("Table:" + tableName + " mapId:" + tableNameIdrArr[1] + " updateDate:" + tableNameIdrArr[2]);
			logger.debug(rowDataStr.toString());
			String userHomeDir = System.getProperty("user.home");
			File targetFile = new File(userHomeDir + REDIS_TEM_DIR + tableName);
			if (rowDataStr.length() > 0) {
				try {
					// Paths.get(userHomeDir);
					// Files.newBufferedWriter(path, cs, options)
					FileUtils.writeStringToFile(targetFile, rowDataStr.toString(), "UTF-8");
				} catch (IOException e) {
					logger.error(tableNameIdrArr[0] + "表数据文件写入磁盘失败了。");
				}

				String path = targetFile.getAbsolutePath();
				logger.info(tableName + "表需要的数据保存到临时目录下:" + path);
//				logger.info(rowDataStr.toString());
				// 执行MYSQL load data command.
				dataTableService.loadData(tableName, path, columnNameList);

				// 修改数据表上次更新时间。mapId - updateDate
				String mapIdStr = tableNameIdrArr[1];
				if (StringUtils.isNotEmpty(mapIdStr)) {
					String redisDataUpdateDateStr = tableNameIdrArr[2];
					try {
						Date redisDateUpdateDate = DateUtils.parse(redisDataUpdateDateStr);
						redisMappingService.updateUpdateDateByMapId(Integer.valueOf(mapIdStr), redisDateUpdateDate);
					} catch (ParseException e) {
						logger.error("解析updateDate失败.");
					}
				}
				logger.info(tableName + "表数据落地完成。");
			}
		}
	}
}
