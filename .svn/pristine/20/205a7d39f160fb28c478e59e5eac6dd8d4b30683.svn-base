package com.iweipeng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {

	private static Logger		logger		= LoggerFactory.getLogger(Launcher.class);

	private static final String	CONFIG_FILE	= "applicationContext.xml";

	@SuppressWarnings("resource")
	public static void startUp() {
		new ClassPathXmlApplicationContext(CONFIG_FILE);
	}

	public static void main(String[] args) {
		logger.info("数据解析转换程序开始执行...");
		Launcher.startUp();
	}

}
