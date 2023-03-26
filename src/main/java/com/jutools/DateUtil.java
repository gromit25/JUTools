package com.jutools;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lombok.Data;

/**
 * 날짜 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class DateUtil {
	
	/** 기본 날짜 구분자 */
	private static String DEFAULT_DATE_DELIMITER = "-";
	/** 기본 시간 구분자 */
	private static String DEFAULT_TIME_DELIMITER = ":";
	
	/**
	 * 날짜 문자열 반환<br>
	 * ex) delimiter : "-"<br>
	 *     -> "2023-03-25"
	 * 
	 * @param delimiter 일자 구분자
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(String delimiter, Calendar date) throws Exception {
		
		if(date == null) {
			throw new NullPointerException("date is null");
		}
		
		if(delimiter == null) {
			throw new NullPointerException("delimiter is null");
		}
		
		String year = Integer.toString(date.get(Calendar.YEAR));
		String month = String.format("%02d", date.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", date.get(Calendar.DAY_OF_MONTH));
		
		return StringUtil.join(delimiter, year, month, day);
	}
	
	/**
	 * 날짜 문자열 반환<br>
	 * ex) delimiter : "-"<br>
	 *     -> "2023-03-25"
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
	 * 날짜 문자열 반환<br>
	 * ex) delimiter : "-"<br>
	 *     -> "2023-03-25"
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
	 * 날짜 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Calendar date) throws Exception {
		return getDateStr(DEFAULT_DATE_DELIMITER, date);
	}
	
	/**
	 * 날짜 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Date date) throws Exception {
		return getDateStr(DEFAULT_DATE_DELIMITER, date);
	}

	/**
	 * 날짜 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(long date) throws Exception {
		return getDateStr(DEFAULT_DATE_DELIMITER, date);
	}

	/**
	 * 날짜 문자열 반환<br>
	 * ex) delimiter : ":"<br>
	 *     -> "16:30:45"
	 * 
	 * @param delimiter 시간 구분자
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(String delimiter, Calendar time) throws Exception {
		
		if(time == null) {
			throw new NullPointerException("time is null");
		}
		
		if(delimiter == null) {
			throw new NullPointerException("delimiter is null");
		}
		
		String hour = String.format("%02d", time.get(Calendar.HOUR_OF_DAY));
		String minute = String.format("%02d", time.get(Calendar.MINUTE));
		String second = String.format("%02d", time.get(Calendar.SECOND));
		
		return StringUtil.join(delimiter, hour, minute, second);
	}
	
	/**
	 * 날짜 문자열 반환<br>
	 * ex) delimiter : ":"<br>
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
	 * 날짜 문자열 반환<br>
	 * ex) delimiter : ":"<br>
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
	 * 날짜 문자열 반환<br>
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Calendar time) throws Exception {
		return getTimeStr(DEFAULT_TIME_DELIMITER, time);
	}

	/**
	 * 날짜 문자열 반환<br>
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Date time) throws Exception {
		return getTimeStr(DEFAULT_TIME_DELIMITER, time);
	}

	/**
	 * 날짜 문자열 반환<br>
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(long time) throws Exception {
		return getTimeStr(DEFAULT_TIME_DELIMITER, time);
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
	
	// ----------------- 음력 관련 --------------------
	
	/**
	 * 음력 날짜 클래스
	 * 
	 * @author jmsohn
	 */
	@Data
	public static class LunarDate {
		
		/** 음력 년도 */
		private int year;
		/** 음력 달(1-12) */
		private int month;
		/** 음력 날짜 */
		private int day;
		/** 윤달 여부 */
		private boolean intercalation;
		
	}
	
	/**
	 * 양력 날짜를 음력 날짜로 변환<br>
	 * 
	 * @param year 양력 년도
	 * @param month 양력 달(1-12)
	 * @param day 양력 날짜
	 * @return 음력 날짜
	 */
	public static LunarDate toLunar(int year, int month, int day) throws Exception {
		return null;
	}

	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환<br>
	 * ex) delimiter : "-"<br>
	 *     -> "2023-03-25"
	 * 
	 * @param delimiter 일자 구분자
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(String delimiter, Calendar date) throws Exception {
		
		if(date == null) {
			throw new NullPointerException("date is null");
		}
		
		if(delimiter == null) {
			throw new NullPointerException("delimiter is null");
		}
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		
		LunarDate lunar = toLunar(year, month, day);
		
		String lunarYear = Integer.toString(lunar.getYear());
		String lunarMonth = String.format("%02d", lunar.getMonth());
		String lunarDay = String.format("%02d", lunar.getDay()); 
		
		return StringUtil.join(delimiter, lunarYear, lunarMonth, lunarDay);
	}

	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환<br>
	 * ex) delimiter : "-"<br>
	 *     -> "2023-03-25"
	 * 
	 * @param delimiter 일자 구분자
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(String delimiter, Date date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		return getLunarStr(delimiter, cal);
	}
	
	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환<br>
	 * ex) delimiter : "-"<br>
	 *     -> "2023-03-25"
	 * 
	 * @param delimiter 일자 구분자
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(String delimiter, long date) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date);
		
		return getLunarStr(delimiter, cal);
	}
	
	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(Calendar date) throws Exception {
		return getLunarStr(DEFAULT_DATE_DELIMITER, date);
	}

	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(Date date) throws Exception {
		return getLunarStr(DEFAULT_DATE_DELIMITER, date);
	}

	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(long date) throws Exception {
		return getLunarStr(DEFAULT_DATE_DELIMITER, date);
	}

}
