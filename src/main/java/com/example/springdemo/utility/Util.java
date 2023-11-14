package com.example.springdemo.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;



@Component
public class Util {
	
	public static final String DATE_FORMAT_SHORT = "dd-MM-yyyy";
	public static final String DATE_FORMAT_LONG = "dd-MM-yyyy HH:mm:ss";
	public static final String DATETIME_SEPARATOR = "T";
	public static final String BLANK_SEPARATOR = " ";
	
	private Util() {
		
	}
	
	/**
	 * Checks if is today.
	 * 
	 * @param date the date
	 * @return the boolean
	 */
	public static Boolean isToday(Date date) {
		
		Calendar c = Calendar.getInstance();
		
		Calendar dataIn = Calendar.getInstance();
		
		c.setTime(date);
		
		return (c.get(Calendar.ERA) == dataIn.get(Calendar.ERA) && c.get(Calendar.YEAR) == dataIn.get(Calendar.YEAR)
				&& c.get(Calendar.DAY_OF_YEAR) == dataIn.get(Calendar.DAY_OF_YEAR));
	}
	
	/**
	 * 
	 * @param date
	 * @param dateFormatIn
	 * @param dateFormatOut
	 * @return
	 * @throws ParseException
	 */
	public static String convert(String date, String dateFormatIn, String dateFormatOut) throws ParseException {
		
		SimpleDateFormat sdfIn = new SimpleDateFormat(dateFormatIn);
		SimpleDateFormat sdfOut = new SimpleDateFormat(dateFormatOut);
		Date parseDate = sdfIn.parse(date);
		return sdfOut.format(parseDate);
		
	}
	
	/**
	 * 
	 * @param date
	 * @param dateFormatOut
	 * @return
	 */
	public static String convert(Date date, String dateFormatOut) {
		
		SimpleDateFormat sdfOut = new SimpleDateFormat(dateFormatOut);
		return sdfOut.format(date);
	}
	
	/**
	 * To fixed hour of day.
	 * 
	 * @param date the date
	 * @param begining the begining
	 * @return the date
	 */
	public static Date toFixedHourOfDay(Date date, boolean begining) {
		
		if(date == null) {
			date = new Date();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		if(begining) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}else {
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		
		return cal.getTime();
	}
	
	public static String buildSystemDate(String dateFormatSimple) {
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatSimple);
		
		return sdf.format(now);
	}
}
