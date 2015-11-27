package com.iweipeng.interpret;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 解析对象列表结构
 * 
 * 例如：[{key=value, key1=value2}, {key=value, key1=value2}];
 * #shares/name
 * @author kevin.pei
 *
 */
public class ObjectListAttributeExpression extends AbstractExpression {

	@Override
	public void interpret(Context context) {
		String jsonData = context.getJsonData();
		String rule = context.getRule();
		if (StringUtils.isEmpty(rule) || StringUtils.isEmpty(jsonData)) {
			// throw new IllegalArgumentException("Invalid parameters.");
			logger.warn("错误的解析规则：" + rule + " from table:" + context.getKeyTable() + " to table:" + context.getTargetTable());
			return;
		}
		String[] segments = rule.substring(1).split("/");
		if (segments.length > 1) {
			// #/objectAttrA/objectAttrB/attrC
			if (StringUtils.isEmpty(segments[0].trim())) {
				this.parseArrayObject(segments, jsonData, context);
			} else {
				// #shares/objectAttrA/objectAttrB/attrC
				JSONObject rootData = JSON.parseObject(jsonData);
				if (rootData.get(segments[0]) != null) {
					String jsonStr = rootData.get(segments[0]).toString();
					this.parseArrayObject(segments, jsonStr, context);
				}
			}
		} else {
			logger.warn("错误的解析规则:" + rule);
		}
	}

	protected void parseArrayObject(String[] segments, String jsonArrStr, Context context) {
		List<String> results = new ArrayList<String>();
		JSONArray arrayData = JSON.parseArray(jsonArrStr);
		for (Object item : arrayData) {
			String value = this.digin(item, segments, 1);
			results.add(value);
		}
		context.setResult(results);
	}

	protected String digin(Object item, String[] segments, int start) {
		JSONObject itemData = JSON.parseObject(item.toString());
		//
		if (start < segments.length - 1) {
			return this.digin(itemData.get(segments[start]).toString(), segments, start + 1);
		}
		return itemData.getString(segments[start]);
	}

}
