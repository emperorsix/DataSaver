package com.iweipeng.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static final String		DATE_FORMAT_PATTERN	= "yyyy-MM-dd hh:mm:ss";

	public static final DateFormat	DATE_FORMAT			= new SimpleDateFormat(DATE_FORMAT_PATTERN);

	public static Date parse(String date) throws ParseException {
		return DATE_FORMAT.parse(date);
	}

	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}
}
