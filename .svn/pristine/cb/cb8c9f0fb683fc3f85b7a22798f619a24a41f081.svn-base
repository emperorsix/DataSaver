package com.iweipeng;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iweipeng.interpret.AbstractExpression;
import com.iweipeng.interpret.AbstractExpression.ExpressionRule;
import com.iweipeng.interpret.Context;
import com.iweipeng.interpret.ObjectListAttributeExpression;
import com.iweipeng.interpret.SimpleAttributeExpression;
import com.iweipeng.interpret.SingleValueListAttributeExpression;
import com.iweipeng.runner.TaskRunner;
import com.iweipeng.service.DataTableService;
import com.iweipeng.service.RedisMappingDetailService;
import com.iweipeng.service.RedisMappingService;
import com.iweipeng.utility.DateUtils;
import com.weiju.common.model.RedisMapping;
import com.weiju.common.model.RedisMappingDetail;
import com.weiju.common.model.WJStockDB;

@Component
public class RedisDataConverter {

	private static Logger				logger				= Logger.getLogger(RedisDataConverter.class);

	private static final int			THREAT_POOL_SIZE	= 5;

	@Autowired
	private RedisMappingService			redisMappingService;

	@Autowired
	private RedisMappingDetailService	redisMappingDetailService;

	@Autowired
	private DataTableService			dataTableService;

	/**
	 * 转换指定日期内的数据，当日期为空时，默认转换当前时间之前24小时内的数据
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void parser() {

		// STEP1. 查询关系规则映射数据
		List<RedisMapping> redisMappingList = redisMappingService.getRedisMappingList();

		Map<String, List<WJStockDB>> tableDataMap = new HashMap<String, List<WJStockDB>>();

		// 数据结构：tableName=[{String columnName,List<String> columnValue)},
		// {String columnName,List<String> columnValue)}]
		Map<String, List<Map<String, List<String>>>> dataTableMap = new HashMap<String, List<Map<String, List<String>>>>();
		logger.info("开始解析......");
		for (RedisMapping rm : redisMappingList) {

			Date lastUpdateDate = rm.getLastUpdate();
			if (lastUpdateDate == null) {
				logger.warn("映射表redis_mapping中有无效的lastUpdate日期-" + lastUpdateDate);
				continue;
			}
			// STEP2. 查询REDIS数据源表data数据，用于数据解析。
			List<WJStockDB> stockDataList = getRedisTableData(tableDataMap, lastUpdateDate, rm.getKeyType());
			Integer mapId = rm.getMapId();
			if (mapId == null) {
				throw new IllegalArgumentException("无效的mapId:" + mapId);
			}

			List<Map<String, List<String>>> tableListTmp = new ArrayList<Map<String, List<String>>>();
			for (WJStockDB stockData : stockDataList) {

				String data = stockData.getData();
				if (StringUtils.isNotEmpty(data)) {
					// 表字段和REDIS数据映射集合
					List<RedisMappingDetail> mappingRules = redisMappingDetailService.getRedisMappingDetailListByMapId(mapId);
					final String updateMode = rm.getUpdateMode();
					boolean isOld = false;
					if (updateMode != null && !"D".equalsIgnoreCase(updateMode)) {
						for (RedisMappingDetail mr : mappingRules) {

							final String fieldIdr = rm.getFieldsUpdTime();

							if (StringUtils.isEmpty(fieldIdr))
								break;

							String[] fields = fieldIdr.split(",");
							for (String field : fields) {
								if (StringUtils.isNotEmpty(field)) {
									if (field.trim().equals(mr.getJsonPath().trim())) {
										Context context = new Context();
										context.setRule(mr.getJsonPath());
										context.setColumn(mr.getDbCol());
										context.setKeyTable(rm.getKeyType());
										context.setTargetTable(rm.getDbTable());
										context.setJsonData(data);
										// STEP3.转换数据
										convert(context);

										List<String> results = context.getResult();
										if (results != null && results.size() > 0) {
											// compare by updateMode
											try {
												Date tmpDate = DateUtils.parse(results.get(0));
												if (tmpDate.before(lastUpdateDate)) {
													// 检查数据版本是否已经太旧，如果太旧，则忽略这条数据
													// isOld = true;
													break;
												}
											} catch (ParseException e) {
												logger.debug("日期解析失败。");
												break;
											}
										}
									}
								}
							}
							if (isOld) {
								break;
							}
						}
					}

					if (isOld) {
						break;
					}
					Map<String, List<String>> dataColumnMap = new HashMap<String, List<String>>();
					for (RedisMappingDetail mr : mappingRules) {
						//
						Context context = new Context();
						context.setRule(mr.getJsonPath());
						context.setColumn(mr.getDbCol());
						context.setKeyTable(rm.getKeyType());
						context.setTargetTable(rm.getDbTable());
						context.setJsonData(data);
						// STEP3.转换数据
						convert(context);

						logger.debug(rm.getDbTable() + " - " + context.getColumn() + " - " + context.getResult());
						dataColumnMap.put(context.getColumn(), context.getResult());
					}
					tableListTmp.add(dataColumnMap);
				} else {
					continue;
				}
			}
			if (stockDataList != null && stockDataList.size() > 0) {
				// 把最新的一条记录的时间作为lastUpdate时间记录到数据表中。
				String lastUpdateStr = DateUtils.formatDate(stockDataList.get(0).getDbDate());
				dataTableMap.put(rm.getDbTable() + "@" + mapId + "@" + lastUpdateStr + "@" + DateUtils.formatDate(lastUpdateDate), tableListTmp);
			}
		}
		if (dataTableMap.size() > 0) {
			// STEP4. 开启线程池执行数据导入功能。
			ExecutorService pool = Executors.newFixedThreadPool(THREAT_POOL_SIZE);
			Set<Map.Entry<String, List<Map<String, List<String>>>>> dataSet = dataTableMap.entrySet();
			Iterator<Entry<String, List<Map<String, List<String>>>>> dataTableSet = dataSet.iterator();
			while (dataTableSet.hasNext()) {
				Entry<String, List<Map<String, List<String>>>> dataTableItem = dataTableSet.next();
				pool.execute(new TaskRunner(dataTableItem, dataTableService, redisMappingService));
			}
			pool.shutdown();
			logger.info("所有导入数据逻辑已经加入线程池中开始工作.");
		} else {
			logger.info("没有redis数据需要导入.");
		}
		logger.info("定时任务等待执行中......");
	}

	private void convert(Context context) {
		ExpressionRule expressionRule = context.getExpressionRule();
		AbstractExpression expression = null;
		if (expressionRule.name() == ExpressionRule.OBJECT_LIST.name()) {
			expression = new ObjectListAttributeExpression();
		} else if (expressionRule.name() == ExpressionRule.SINGLE_VALUE_LIST.name()) {
			expression = new SingleValueListAttributeExpression();
		} else {
			expression = new SimpleAttributeExpression();
		}
		expression.interpret(context);
	}

	private List<WJStockDB> getRedisTableData(Map<String, List<WJStockDB>> tableDataMap, Date dbDate, String keyTable) {
		List<WJStockDB> stockDBList = null;
		if (tableDataMap.get(keyTable) == null) {
			stockDBList = dataTableService.getDataByTableBeforeDate(keyTable, dbDate);
			tableDataMap.put(keyTable, stockDBList);
		}
		stockDBList = tableDataMap.get(keyTable);
		return stockDBList;
	}
}
