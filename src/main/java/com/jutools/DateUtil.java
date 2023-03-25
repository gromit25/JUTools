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
	 * 날짜 문자열 반환
	 * ex) delimiter : "/"
	 *     -> "2023/03/25"
	 * 
	 * @param delimiter 일자 구분자
	 * @param date 날짜
	 * @return 날짜 문자열
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
	
	/**
	 * 날짜 문자열 반환
	 * ex) delimiter : "/"
	 *     -> "2023/03/25"
	 * 
	 * @param delimiter 일자 구분자
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(String delimiter, Date date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		return getDateStr(delimiter, cal);
	}
	
	/**
	 * 날짜 문자열 반환
	 * ex) delimiter : "/"
	 *     -> "2023/03/25"
	 * 
	 * @param delimiter 일자 구분자
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(String delimiter, long date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date);
		
		return getDateStr(delimiter, cal);
	}

	/**
	 * 날짜 문자열 반환
	 * ex) "2023/03/25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Calendar date) throws Exception {
		return getDateStr("/", date);
	}
	
	/**
	 * 날짜 문자열 반환
	 * ex) "2023/03/25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Date date) throws Exception {
		return getDateStr("/", date);
	}

	/**
	 * 날짜 문자열 반환
	 * ex) "2023/03/25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(long date) throws Exception {
		return getDateStr("/", date);
	}

	public static Calendar lunarToGregorian(int year, int month, int day) throws Exception {
		return null;
	}
	
	public static Calendar lunarToGregorian(Calendar date) throws Exception {
		return null;
	}
	
	public static Calendar lunarToGregorian(Date date) throws Exception {
		return null;
	}
	
	public static Calendar lunarToGregorian(long date) throws Exception {
		return null;
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
	 * 날짜 문자열 반환
	 * ex) delimiter : ":"
	 *     -> "16:30:45"
	 * 
	 * @param delimiter 시간 구분자
	 * @param time 시간
	 * @return 시간 문자열
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
	
	/**
	 * 날짜 문자열 반환
	 * ex) delimiter : ":"
	 *     -> "16:30:45"
	 * 
	 * @param delimiter 시간 구분자
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(String delimiter, Date time) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(time);
		
		return getTimeStr(delimiter, cal);
	}
	
	/**
	 * 날짜 문자열 반환
	 * ex) delimiter : ":"
	 *     -> "16:30:45"
	 * 
	 * @param delimiter 시간 구분자
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(String delimiter, long time) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		
		return getTimeStr(delimiter, cal);
	}

	/**
	 * 날짜 문자열 반환
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Calendar time) throws Exception {
		return getTimeStr(":", time);
	}

	/**
	 * 날짜 문자열 반환
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Date time) throws Exception {
		return getTimeStr(":", time);
	}

	/**
	 * 날짜 문자열 반환
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(long time) throws Exception {
		return getTimeStr(":", time);
	}
	
	/**
	 * 날짜에 일자를 더함(음수 가능)
	 * 
	 * @param date 날짜
	 * @param day 더할 날짜
	 */
	public static void addDay(Calendar date, int day) throws Exception {
		
		if(date == null) {
			throw new Exception("date is null");
		}
		
		date.add(Calendar.DATE, day);
	}
	
	/**
	 * 날짜에 일자를 더함(음수 가능)
	 * 
	 * @param date 날짜
	 * @param day 더할 날짜
	 * @return 더해진 날짜(ms)
	 */
	public static long addDay(long date, int day) {
		long milsInDay = day * 1000 * 60 * 60 * 24;
		return date + milsInDay;
	}
	
	/**
	 * 날짜에 일자를 더함(음수 가능)
	 * 
	 * @param date 날짜
	 * @param day 더할 날짜
	 */
	public static void addDay(Date date, int day) throws Exception {
		
		if(date == null) {
			throw new Exception("date is null");
		}
		
		long newDate = addDay(date.getTime(), day);
		date.setTime(newDate);
	}

}
