package com.jutools;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 날짜 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class DateUtil {
	
	/**
	 * 
	 * @param delimiter
	 * @param date
	 * @return
	 */
	public static String getDateStr(String delimiter, Calendar date) throws Exception {
		
		if(date == null) {
			throw new Exception("date is null");
		}
		
		String year = Integer.toString(date.get(Calendar.YEAR));
		String month = String.format("%02d", date.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", date.get(Calendar.DAY_OF_MONTH));
		
		return StringUtil.join(delimiter, year, month, day);
	}
	
	public static String getDateStr(String delimiter, Date date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		return getDateStr(delimiter, cal);
	}
	
	public static String getDateStr(String delimiter, long date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date);
		
		return getDateStr(delimiter, cal);
	}

	public static String getDateStr(Calendar date) throws Exception {
		return getDateStr("/", date);
	}
	
	public static String getDateStr(Date date) throws Exception {
		return getDateStr("/", date);
	}

	public static String getDateStr(long date) throws Exception {
		return getDateStr("/", date);
	}
	
	public static String getLunarStr(String delimiter, Calendar date) throws Exception {
		return null;
	}
	
	public static String getLunarStr(String delimiter, Date date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		return getLunarStr(delimiter, cal);
	}
	
	public static String getLunarStr(String delimiter, long date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date);
		
		return getLunarStr(delimiter, cal);
	}
	
	public static String getLunarStr(Calendar date) throws Exception {
		return getLunarStr("/", date);
	}
	
	public static String getLunarStr(Date date) throws Exception {
		return getLunarStr("/", date);
	}
	
	public static String getLunarStr(long date) throws Exception {
		return getLunarStr("/", date);
	}
	
	/**
	 * 
	 * 
	 * @param delimiter
	 * @param time
	 * @return
	 */
	public static String getTimeStr(String delimiter, Calendar time) throws Exception {
		
		if(time == null) {
			throw new Exception("time is null");
		}
		
		String hour = String.format("%02d", time.get(Calendar.HOUR_OF_DAY));
		String minute = String.format("%02d", time.get(Calendar.MINUTE));
		String second = String.format("%02d", time.get(Calendar.SECOND));
		
		return StringUtil.join(delimiter, hour, minute, second);
	}
	
	public static String getTimeStr(String delimiter, Date time) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(time);
		
		return getTimeStr(delimiter, cal);
	}
	
	public static String getTimeStr(String delimiter, long time) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		
		return getTimeStr(delimiter, cal);
	}

	public static String getTimeStr(Calendar time) throws Exception {
		return getTimeStr(":", time);
	}

	public static String getTimeStr(Date time) throws Exception {
		return getTimeStr(":", time);
	}

	public static String getTimeStr(long time) throws Exception {
		return getTimeStr(":", time);
	}

}
