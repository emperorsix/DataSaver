package com.iweipeng.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iweipeng.annotation.DataSource;

public class DataSourceAspectMiddleware {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceAspectMiddleware.class);

	public void before(JoinPoint point) {
		Object target = point.getTarget();
		String method = point.getSignature().getName();

		Class<?>[] classz = target.getClass().getInterfaces();

		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		try {
			Method m = classz[0].getMethod(method, parameterTypes);
			if (m != null && m.isAnnotationPresent(DataSource.class)) {
				DataSource data = m.getAnnotation(DataSource.class);
				DataSourceContextHolder.putDataSource(data.value());
				logger.info("当前DataSource为：" + data.value());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}