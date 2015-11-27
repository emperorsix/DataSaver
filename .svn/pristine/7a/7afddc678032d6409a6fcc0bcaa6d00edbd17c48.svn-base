package com.iweipeng;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * ContextClosedEvent ContextRefreshedEvent ContextStartedEvent
 * ContextStoppedEvent RequestHandleEvent
 * 
 * @author qingfengpei
 *
 */

@Component
public class AppContainerListener implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger		logger	= Logger.getLogger(AppContainerListener.class);

	@Autowired
	private RedisDataConverter	dataConverter;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Spring容器初始化完成 开始执行转换程序.");
		dataConverter.parser();
	}

}
