package com.malaz.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {
	private final static int START_OF_WEEK = Calendar.SUNDAY;
	private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy/MM");
	private final static SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd");
	
	private static void setMinTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
	}
	
	private static void setMaxTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
	}
	
	
	public static class DateRange {
		public Date firstDate;
		public Date endDate;
	}
		
	public static class YearRangeGenerator {
		private List<DateRange> ranges;
		
		public static YearRangeGenerator getInstance() {
			return new YearRangeGenerator();
		}
		
		private YearRangeGenerator() {
			this.ranges = new ArrayList<DateRange>();
			generate();
		}
		
		private void generate() {
			int applicationStartingDate = 2013;
			int yearUntil = applicationStartingDate + 5;
			
			Calendar calendar = Calendar.getInstance();
		
			for(int i=applicationStartingDate; i<yearUntil; i++) {
				calendar.set(i, 0, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				setMinTime(calendar);
				Date start = calendar.getTime();				
				
				calendar.set(i, 11, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				setMaxTime(calendar);
				Date end = calendar.getTime();
				
				DateRange range = new DateRange();
				range.firstDate = start;
				range.endDate = end;
				
				this.ranges.add(range);
			}
		}
		
		public List<DateRange> getRanges() {
			return this.ranges;
		}
	}

	public static class MonthlyRangeGenerator {
		private List<DateRange> ranges;
		
		public static MonthlyRangeGenerator getInstance() {
			return new MonthlyRangeGenerator();
		}
		
		private MonthlyRangeGenerator() {
			this.ranges = new ArrayList<DateRange>();
			generate();
		}
		
		private void generate() {
			Calendar calendar = Calendar.getInstance();
			
			for(int i=0; i<12; i++) {
				calendar.set(Calendar.MONTH, i);
				
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				setMinTime(calendar);
				Date start = calendar.getTime();
				
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				setMaxTime(calendar);
				Date end = calendar.getTime();		
				
				DateRange range = new DateRange();
				range.firstDate = start;
				range.endDate = end;
				
				this.ranges.add(range);
			}
		}
		
		public List<DateRange> getRanges() {
			return this.ranges;
		}
	}
	
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
	
	public static String dayFormat(Date date) {
		return dayFormat.format(date);
	}
	
	public static String yearFormat(Date date) {
		return yearFormat.format(date);
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
		
		// Generate the dates
		Date[] dates = new Date[7];
		for (int i=0; i<7; i++) {
			dates[i] = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return dates;
	}
}
