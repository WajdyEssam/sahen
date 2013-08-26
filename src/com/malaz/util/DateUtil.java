package com.malaz.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {
	private final static int START_OF_WEEK = Calendar.SUNDAY;
	private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static class WeekRangeGenerator {
		private String firstDate;
		private String lastDate;
		
		public static WeekRangeGenerator generate() {
			return new WeekRangeGenerator();
		}
		
		private WeekRangeGenerator() {
			List<Date> dates = Arrays.asList(genearteSevenDays(new Date()));
			this.firstDate = format.format(dates.get(0)) + " 00:00:00";
			this.lastDate = format.format(dates.get(dates.size()-1)) + " 23:59:59";
		}
		
		public String getFirstDate() {
			return this.firstDate;
		}
		
		public String getLastDate() {
			return this.lastDate;
		}
	}
	
	public static Date formatStringDate(String stringDate) throws ParseException {
        Date formattedDate = format.parse(stringDate);        
        return formattedDate;
	}
	
	public static String formatDate(Date date) {
		return format.format(date);
	}
	
	public static int getDayIndex(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);		
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	private static Date[] genearteSevenDays(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		
		// get the first day in this week
		int delta = -calendar.get(GregorianCalendar.DAY_OF_WEEK) + START_OF_WEEK;
		calendar.add(Calendar.DAY_OF_MONTH, delta );
		
		// genearate the dates
		Date[] dates = new Date[7];
		for (int i=0; i<7; i++) {
			dates[i] = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return dates;
	}
}
