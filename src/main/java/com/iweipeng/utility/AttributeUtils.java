package com.iweipeng.utility;

import org.springframework.util.StringUtils;

public class AttributeUtils {

	public static String getStringName(String name) {
		if (StringUtils.isEmpty(name)) {
			return IParameters.EMPTY_TABLE_STRING;
		}
		return "\"" + name + "\"";
	}
}
