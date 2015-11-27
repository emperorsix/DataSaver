package com.iweipeng.datasource;

public class DataSourceContextHolder {

	public static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

	public static void putDataSource(String name) {
		threadLocal.set(name);
	}

	public static String getDataSouce() {
		return threadLocal.get();
	}
}
