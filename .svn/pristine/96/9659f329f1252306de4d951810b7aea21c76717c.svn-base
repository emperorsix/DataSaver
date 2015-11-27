package com.iweipeng.interpret;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 按照redis data解析规则，定义不同的具体的解析规则来转换数据。
 * 
 * @author qingfeng
 *
 */
public abstract class AbstractExpression {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public abstract void interpret(Context context);

	public static enum ExpressionRule {
		SIMPLE, SINGLE_VALUE_LIST, OBJECT_LIST
	}
}
