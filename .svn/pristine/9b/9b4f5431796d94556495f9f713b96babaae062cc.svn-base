package com.iweipeng.interpret;

import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 解析列表中仅仅拥有单值数据的列表结构 例如：[string, string, string], the real example: m_chart[index]
 * 
 * @author kevin.pei
 */
public class SingleValueListAttributeExpression extends AbstractExpression {

	@Override
	public void interpret(Context context) {

		String jsonData = context.getJsonData();
		String rule = context.getRule();
		if (StringUtils.isEmpty(rule) || StringUtils.isEmpty(jsonData)) {
			// throw new IllegalArgumentException("Invalid parameters.");
			logger.warn("错误的解析规则：" + rule + " from table:" + context.getKeyTable() + " to table:" + context.getTargetTable());
			return;
		}
		// Parse original JSON data.
		JSONObject dataMap = JSON.parseObject(jsonData);
		// 提取规则 according to "["
		String[] segments = rule.split("[");
		JSONArray dataArr = JSON.parseArray(dataMap.get(segments[0]).toString());
		if (StringUtils.isNotEmpty(segments[1])) {
			// 除去最后一位的字符"]"
			String indexStr = segments[1].substring(0, segments[1].length() - 1);
			boolean isDigit = NumberUtils.isDigits(indexStr);
			if (isDigit) {
				String value = (String) dataArr.get(Integer.valueOf(indexStr));
				context.setResult(Collections.singletonList(value));
			}
		}
	}
}
