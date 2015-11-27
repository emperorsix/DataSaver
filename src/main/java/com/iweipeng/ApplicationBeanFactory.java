package com.iweipeng;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * 使用@Autowired注解自动装配{@link BeanFactory}
 * 
 * @author qingfengpei
 *
 */
@Component
public class ApplicationBeanFactory extends SpringBeanAutowiringSupport {

	@Autowired
	private BeanFactory	beanFactory;

	private ApplicationBeanFactory() {
	};

	private static ApplicationBeanFactory	instance;

	static {
		instance = new ApplicationBeanFactory();
	}

	@SuppressWarnings("unchecked")
	public <T> T getBeanById(String beanId) {
		return (T) beanFactory.getBean(beanId);
	}

	public static ApplicationBeanFactory getApplicationBeanFactory() {
		return instance;
	}
}
