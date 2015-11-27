package com.iweipeng.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.iweipeng.RedisDataConverter;

public class DataSaverJob {

	private Logger				logger	= LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisDataConverter	dataConverter;

	public void work() {
		logger.info("定时转换程序开始运行.");
		dataConverter.parser();
	}

}
