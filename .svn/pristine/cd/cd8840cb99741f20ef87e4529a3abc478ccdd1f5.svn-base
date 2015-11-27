package com.iweipeng;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 使用{@link ApplicationContextAware}获得{@link ApplicationContext}
 * 
 * @author qingfengpei
 *
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext	applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	private ApplicationContextUtil() {

	}

	public static synchronized ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
