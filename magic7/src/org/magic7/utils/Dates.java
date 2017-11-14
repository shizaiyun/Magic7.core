package org.magic7.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;


public class Dates {
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_SHORT_FORMAT = "yyyyMMdd";
	public static final String DATE_yyyy_MM_FORMAT = "yyyyMM";
	public static final String DATE_SHORT_FORMAT_SLASH = "MM/dd/yyyy";
	public static final String DATE_SHORT_FORMAT_YMD = "yyyy/MM/dd HH:mm";
	public static final String DATE_dd_MM_YY_SLASH = "dd/MM/yyyy";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_SHORT_FORMAT = "yyyyMMddHHmmss";
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	public static final String TIME_SHORT_FORMAT = "HHmmss";
	private static final String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	
	public enum TimeZoneEnum {
		Beijing("Beijing","GMT+8:00"),
		SouthKorea("South Korea","GMT+9:00"),
		Japan("Japan","GMT+9:00"),
		Canada_Ottawa("Canada/Ottawa","GMT-5:00"),
		America_Washington("America/Washington","GMT-5:00"),
		HongKong("HongKong","GMT+8:00"),
		Singapore("Singapore","GMT+8:00"),
		London("London","GMT"),
		Berlin("Berlin","GMT+1:00"),
		Finland("Finland","GMT+2:00"),
		London_DaylightSavingTime("London_DaylightSavingTime","GMT+1:00"),
		Berlin_DaylightSavingTime("Berlin_DaylightSavingTime","GMT+2:00"),
		Finland_DaylightSavingTime("Finland_DaylightSavingTime","GMT+3:00");
		private String name;
		private String value;
		private TimeZoneEnum(String name,String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		public static TimeZoneEnum getTimeZone(String name) {
			if(name.equals(SouthKorea.name))
				return SouthKorea;
			if(name.equals(Japan.name))
				return Japan;
			if(name.equals(Canada_Ottawa.name))
				return Canada_Ottawa;
			if(name.equals(America_Washington.name))
				return America_Washington;
			if(name.equals(HongKong.name))
				return HongKong;
			if(name.equals(Singapore.name))
				return Singapore;
			if(name.equals(London.name))
				return London;
			if(name.equals(Berlin.name))
				return Berlin;
			if(name.equals(Finland.name))
				return Finland;
			if(name.equals(London_DaylightSavingTime.name))
				return London_DaylightSavingTime;
			if(name.equals(Berlin_DaylightSavingTime.name))
				return Berlin_DaylightSavingTime;
			if(name.equals(Finland_DaylightSavingTime.name))
				return Finland_DaylightSavingTime;
			if(name.equals(Beijing.name))
				return Beijing;
			return null;
		}
	} 
	
	public static Date getFirstDayOfMonth(Date date) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH,1);
        return truncateDate(cal.getTime());
	}
	public static Date getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();   
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH,0);
        return addSeconds(truncateDate(addDay(cal.getTime())),-1);
	}
	public static String currentDateTimeStringStandard(){
		return currentDateTimeString(DATETIME_FORMAT);
	}
	
	public static String currentDateTimeString(String format){
		return DateFormatUtils.format(new Date(), format);
	}
	public static String format(Date date, String dateFormat) {
		return DateFormatUtils.format(date, dateFormat);
	}
	public static int compareDate(Date date, Date date2, int type) {
		switch(type){
		case Calendar.YEAR:
		case Calendar.MONTH:
		case Calendar.DAY_OF_MONTH:
			compareDate(DateUtils.ceiling(date, type), DateUtils.ceiling(date2, type)); 
		default:
			break;
		}
		return compareDate(date, date2);
	}
	/**
	 * 比较日期前后
	 * date > date2 则返回 1
	 * date < date2 则返回 -1
	 * date = date2 则返回 0
	 * @param date
	 * @param date2
	 * @return
	 */
	public static int compareDate(Date date, Date date2) {
		if(date==null||date2==null)
			return 2;
		
		if (date.after(date2)){
			return 1;
		}
		if (date.before(date2))
			return -1;
		
		return 0;
	}
	
	public static Date truncateDate(Date date) {
		return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
	}
	public static Date addSeconds(Date queryDate, Integer add){
		return DateUtils.addSeconds(queryDate, add);
	}
	
	public static Date parseDate(String strDate, String dateFormat) {
		if (null == strDate)
			return null;
		
		Date parseDate = null;
		try {
			parseDate = DateUtils.parseDate(strDate, new String[]{dateFormat});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseDate;
	}
	
	public static Date parseDate(String strDate, String dateFormat,TimeZone zone) {
		if (null == strDate||"".equals(strDate))
			return null;
		SimpleDateFormat sformat = new SimpleDateFormat(dateFormat);
		sformat.setTimeZone(zone);
		Date parseDate = null;
		try {
			parseDate = sformat.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseDate;
	}
	public static Date addDay(Date currDate) {
		if (null == currDate)
			return new Date();
		return DateUtils.addDays(currDate, 1);
	}
	public static Date addDay(Date currDate, int day) {
		if (null == currDate)
			return new Date();
		return DateUtils.addDays(currDate, day);
	}	
	public static Date addMonth(Date date) {
		if (null == date)
			return new Date();
		return DateUtils.addMonths(date, 1);
	}
	public static Date addMonth(Date date,int monthNum) {
		if (null == date)
			return new Date();
		return DateUtils.addMonths(date, monthNum);
	}
	public static Date getDayEnd(Date date) {
		if (null == date)
			return null;
		
		return parseDate(format(date, DATE_FORMAT) + " 23:59:59", DATETIME_FORMAT);
	}
	
	public static Date addMinutes(Date queryDate, Integer add){
		return DateUtils.addMinutes(queryDate, add);
	}

	public static Date addHour(Date queryDate) {
		return addHour(queryDate, 1);
	}
	
	public static Date addHour(Date queryDate, Integer add){
		return DateUtils.addHours(queryDate, add);
	}
	
	public static Integer getYear(Date queryDate){
		Integer year = -1;
		if (null == queryDate){
			return year; 
		}
		String yearString = DateFormatUtils.format(queryDate, "yyyy");
		try{
			year = Integer.parseInt(yearString);
		}catch(Exception e){}
		return year;
	}
	public static Integer getMonth(Date queryDate){
		Integer mon = 1;
		if (null == queryDate){
			return mon; 
		}
		String monString = DateFormatUtils.format(queryDate, "MM");
		try{
			mon = Integer.parseInt(monString);
		}catch(Exception e){}
		return mon;
	}
	public static Integer getDay(Date queryDate){
		Integer day = 1;
		if (null == queryDate){
			return day; 
		}
		String dayString = DateFormatUtils.format(queryDate, "dd");
		try{
			day = Integer.parseInt(dayString);
		}catch(Exception e){}
		return day;
	}

	public static String getWeekOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
	}	
	
	public static String dateToStr(Date date, String format) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		return sformat.format(date);
	}
	
	public static String dateToStr(Date date, String format,TimeZone timeZone) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		sformat.setTimeZone(timeZone);
		return sformat.format(date);
	}
	public static Date transformDate(String date) {
		if (date == null || date.trim().isEmpty())
			return null;
		Date result = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
			result = format.parse(date);
		} catch (ParseException e1) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				result = format.parse(date);
			} catch (ParseException e2) {
				try {
					SimpleDateFormat format = new SimpleDateFormat(DATE_dd_MM_YY_SLASH);
					result = format.parse(date);
				} catch (ParseException e3) {
					throw new RuntimeException(e3);
				}
			}
		}
		return result;
	}
	public static Date getDayStart(Date date) {
		if (null == date)
			return null;
		
		return parseDate(format(date, DATE_FORMAT) + " 00:00:00", DATETIME_FORMAT);
	}
	public static Date addYears(Date date,int yearNum) {
		if (null == date)
			return new Date();
		return DateUtils.addYears(date,yearNum);
	}
	public static boolean isEndQuarter(Date date) {
		boolean retval = false;
		Calendar calendar = getCalendar(date);
		int month = calendar.get(Calendar.MONTH);
		if ((month + 1) % 3 == 0) {
			retval = true;
		}
		return retval;
	}
	public static boolean isMidYear(Date date) {
		boolean retval = false;
		Calendar calendar = getCalendar(date);
		int month = calendar.get(Calendar.MONTH);
		if ((month + 1) % 6 == 0) {
			retval = true;
		}
		return retval;
	}
	public static boolean isEndYear(Date date) {
		boolean retval = false;
		Calendar calendar = getCalendar(date);
		int month = calendar.get(Calendar.MONTH);
		if ((month + 1) % 12 == 0) {
			retval = true;
		}
		return retval;
	}
	public static Calendar getCalendar(Date date) {
		if (date == null || "".equals(date))
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
}
