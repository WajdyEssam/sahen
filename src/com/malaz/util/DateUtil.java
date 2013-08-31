package com.malaz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {
	private final static int START_OF_WEEK = Calendar.SUNDAY;
	private final static SimpleDateFormat sqliteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);	
		
	private final static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
	private final static SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy", Locale.US);	
	private final static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
	
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
		
	public static class DayRangeGenerator {
		private List<DateRange> ranges;
		
		public static DayRangeGenerator getInstance() {
			return new DayRangeGenerator();
		}
		
		private DayRangeGenerator() {
			this.ranges = new ArrayList<DateRange>();
			generate();
		}
		
		private void generate() {
			Calendar calendar = Calendar.getInstance();
			
			int delta = -calendar.get(Calendar.DAY_OF_WEEK) + START_OF_WEEK;
			calendar.add(Calendar.DAY_OF_MONTH, delta );
			
			for (int i=0; i<7; i++) {
				setMinTime(calendar);
				Date start = calendar.getTime();
				setMaxTime(calendar);
				Date end = calendar.getTime();
				
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				
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
	
	public static Date formatStringDate(String stringDate) throws ParseException {
        Date formattedDate = sqliteFormat.parse(stringDate);        
        return formattedDate;
	}
	
	public static String formatDate(Date date) {
		return sqliteFormat.format(date);
	}
	
	public static String dayFormat(Date date) {
		return dayFormat.format(date);
	}
	
	public static String yearFormat(Date date) {
		return yearFormat.format(date);
	}
	
	public static String monthFormat(Date date) {
		return monthFormat.format(date);
	}
	
	public static int getDayIndex(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);		
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
}
