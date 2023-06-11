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
	 * @param date 날짜
	 * @param delimiter 일자 구분자
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Calendar date, String delimiter) throws Exception {
		
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
	 * @param date 날짜
	 * @param delimiter 일자 구분자
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Date date, String delimiter) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		return getDateStr(cal, delimiter);
	}
	
	/**
	 * 날짜 문자열 반환<br>
	 * ex) delimiter : "-"<br>
	 *     -> "2023-03-25"
	 * 
	 * @param date 날짜
	 * @param delimiter 일자 구분자
	 * @return 날짜 문자열
	 */
	public static String getDateStr(long date, String delimiter) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date);
		
		return getDateStr(cal, delimiter);
	}

	/**
	 * 날짜 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Calendar date) throws Exception {
		return getDateStr(date, DEFAULT_DATE_DELIMITER);
	}
	
	/**
	 * 날짜 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(Date date) throws Exception {
		return getDateStr(date, DEFAULT_DATE_DELIMITER);
	}

	/**
	 * 날짜 문자열 반환<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String getDateStr(long date) throws Exception {
		return getDateStr(date, DEFAULT_DATE_DELIMITER);
	}

	/**
	 * 시간 문자열 반환<br>
	 * ex) delimiter : ":"<br>
	 *     -> "16:30:45"
	 * 
	 * @param time 시간
	 * @param delimiter 시간 구분자
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Calendar time, String delimiter) throws Exception {
		
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
	 * 시간 문자열 반환<br>
	 * ex) delimiter : ":"<br>
	 *     -> "16:30:45"
	 * 
	 * @param time 시간
	 * @param delimiter 시간 구분자
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Date time, String delimiter) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(time);
		
		return getTimeStr(cal, delimiter);
	}
	
	/**
	 * 시간 문자열 반환<br>
	 * ex) delimiter : ":"<br>
	 *     -> "16:30:45"
	 * 
	 * @param time 시간
	 * @param delimiter 시간 구분자
	 * @return 시간 문자열
	 */
	public static String getTimeStr(long time, String delimiter) throws Exception {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		
		return getTimeStr(cal, delimiter);
	}

	/**
	 * 시간 문자열 반환<br>
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Calendar time) throws Exception {
		return getTimeStr(time, DEFAULT_TIME_DELIMITER);
	}

	/**
	 * 시간 문자열 반환<br>
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(Date time) throws Exception {
		return getTimeStr(time, DEFAULT_TIME_DELIMITER);
	}

	/**
	 * 시간 문자열 반환<br>
	 * ex) "16:30:45"
	 * 
	 * @param time 시간
	 * @return 시간 문자열
	 */
	public static String getTimeStr(long time) throws Exception {
		return getTimeStr(time, DEFAULT_TIME_DELIMITER);
	}
	
	/**
	 * 날짜-시간 문자열 반환<br>
	 * ex)<br>
	 * dateDelimiter: "-"<br>
	 * timeDelimiter: ":"<br>
	 * -> "2023-03-25 16:30:45"
	 * 
	 * @param dateTime 날짜시간
	 * @param dateDelimiter 날짜 구분자
	 * @param timeDelimiter 시간 구분자
	 * @return 날짜-시간 문자열 
	 */
	public static String getDateTimeStr(Calendar dateTime, String dateDelimiter, String timeDelimiter) throws Exception {
		
		// 날짜 문자열 생성
		String dateStr = getDateStr(dateTime, dateDelimiter);
		// 시간 문자열 생성
		String timeStr = getTimeStr(dateTime, timeDelimiter);
		
		// 날짜-시간 문자열 생성 후 반환
		return StringUtil.join(" ", dateStr, timeStr);
	}

	/**
	 * 날짜-시간 문자열 반환<br>
	 * ex)<br>
	 * dateDelimiter: "-"<br>
	 * timeDelimiter: ":"<br>
	 * -> "2023-03-25 16:30:45"
	 * 
	 * @param dateTime 날짜시간
	 * @param dateDelimiter 날짜 구분자
	 * @param timeDelimiter 시간 구분자
	 * @return 날짜-시간 문자열 
	 */
	public static String getDateTimeStr(Date dateTime, String dateDelimiter, String timeDelimiter) throws Exception {
		
		// 날짜-시간 문자열 생성 후 반환
		return getDateTimeStr(toCalendar(dateTime), dateDelimiter, timeDelimiter);
	}
	
	/**
	 * 날짜-시간 문자열 반환<br>
	 * ex)<br>
	 * dateDelimiter: "-"<br>
	 * timeDelimiter: ":"<br>
	 * -> "2023-03-25 16:30:45"
	 * 
	 * @param dateTime 날짜시간
	 * @param dateDelimiter 날짜 구분자
	 * @param timeDelimiter 시간 구분자
	 * @return 날짜-시간 문자열 
	 */
	public static String getDateTimeStr(long dateTime, String dateDelimiter, String timeDelimiter) throws Exception {
		
		// 날짜-시간 문자열 생성 후 반환
		return getDateTimeStr(toCalendar(dateTime), dateDelimiter, timeDelimiter);
	}
	
	/**
	 * 날짜-시간 문자열 반환<br>
	 * ex) "2023-03-25 16:30:45"
	 * 
	 * @param dateTime 날짜시간
	 * @return 날짜-시간 문자열 
	 */
	public static String getDateTimeStr(Calendar dateTime) throws Exception {
		
		// 날짜-시간 문자열 생성 후 반환
		return getDateTimeStr(dateTime, DEFAULT_DATE_DELIMITER, DEFAULT_TIME_DELIMITER);
	}
	
	/**
	 * 날짜-시간 문자열 반환<br>
	 * ex) "2023-03-25 16:30:45"
	 * 
	 * @param dateTime 날짜시간
	 * @return 날짜-시간 문자열 
	 */
	public static String getDateTimeStr(Date dateTime) throws Exception {
		
		// 날짜-시간 문자열 생성 후 반환
		return getDateTimeStr(dateTime, DEFAULT_DATE_DELIMITER, DEFAULT_TIME_DELIMITER);
	}

	/**
	 * 날짜-시간 문자열 반환<br>
	 * ex) "2023-03-25 16:30:45"
	 * 
	 * @param dateTime 날짜시간
	 * @return 날짜-시간 문자열 
	 */
	public static String getDateTimeStr(long dateTime) throws Exception {
		
		// 날짜-시간 문자열 생성 후 반환
		return getDateTimeStr(dateTime, DEFAULT_DATE_DELIMITER, DEFAULT_TIME_DELIMITER);
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
	
	/**
	 * Date -> long(millis)
	 * 
	 * @param date Date 날짜 객체
	 * @return 날짜의 long 값
	 */
	public static long toMillis(Date date) throws Exception {
		
		if(date == null) {
			throw new IllegalArgumentException("date is null");
		}
		
		return date.getTime();
	}

	/**
	 * Calendar -> long(millis)
	 * 
	 * @param cal Calendar 날짜 객체
	 * @return 날짜의 long 값
	 */
	public static long toMillis(Calendar cal) throws Exception {

		if(cal == null) {
			throw new IllegalArgumentException("cal is null");
		}
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * long(mills) -> Date
	 * 
	 * @param millis 날짜의 long 값
	 * @return Date 날짜 객체
	 */
	public static Date toDate(long millis) {
		return new Date(millis);
	}

	/**
	 * Calendar -> Date
	 * 
	 * @param cal Calendar 날짜 객체
	 * @return Date 날짜 객체
	 */
	public static Date toDate(Calendar cal) {

		if(cal == null) {
			throw new IllegalArgumentException("cal is null");
		}

		return cal.getTime();
	}
	
	/**
	 * long(millis) -> Calendar
	 * 
	 * @param millis 날짜의 long 값
	 * @return Calendar 날짜 객체
	 */
	public static Calendar toCalendar(long millis) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(millis);
		
		return cal;
	}
	
	/**
	 * date -> Calendar
	 * 
	 * @param date Date 날짜 객체
	 * @return Calendar 날짜 객체
	 */
	public static Calendar toCalendar(Date date) {
		
		if(date == null) {
			throw new IllegalArgumentException("date is null");
		}
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		return cal;
	}
	
	/**
	 * 날짜가 유효한 지 여부를 반환<br>
	 * 유효하면 true, 유효하지 않으면 false<br>
	 * ex) year:2023, month:2, day:30 이면 false
	 * 
	 * @param year 년도
	 * @param month 월(1~12)
	 * @param day(1~31)
	 * @return 날짜의 유효성 여부
	 */
	public static boolean isValidDate(int year, int month, int day) {
		
		if(month > 12 || month < 1) {
			return false;
		}
		
		if(day > 31 || day < 1) {
			return false;
		}
		
		if(month == 2) {
			
			int lastDay = 28;
			if(isLeapYear(year) == true) {
				lastDay = 29;
			}
			
			if(day <= lastDay && day >= 1) {
				return true;
			} else {
				return false;
			}
			
		} else if( month == 1 || month == 3  || month == 5 || month == 7 ||
			month == 8 || month == 10 || month == 12) {
			
			if(day <= 31 && day >= 1) {
				return true;
			} else {
				return false;
			}
			
		} else {
			
			if(day <= 30 && day >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 년도가 윤년인지 여부를 반환<br>
	 * 윤년이면 true, 윤년이 아니면 false
	 * 
	 * @param year 년도
	 * @return 윤년 여부
	 */
	public static boolean isLeapYear(int year) {
		if (year % 4 != 0) {
			return false;
		} else if (year % 100 != 0) {
			return true;
		} else if (year % 400 != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	// ----------------- 음력 관련 --------------------
	// 개발 중
	
	/**
	 * 음력 날짜 클래스 - 개발 중
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
	 * 양력 날짜를 음력 날짜로 변환 - 개발 중
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
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환 - 개발 중<br>
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
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환 - 개발 중<br>
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
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환 - 개발 중<br>
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
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환 - 개발 중<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(Calendar date) throws Exception {
		return getLunarStr(DEFAULT_DATE_DELIMITER, date);
	}

	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환 - 개발 중<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(Date date) throws Exception {
		return getLunarStr(DEFAULT_DATE_DELIMITER, date);
	}

	/**
	 * 양력 날짜를 음력 날짜로 변환 후 문자열 반환 - 개발 중<br>
	 * ex) "2023-03-25"
	 * 
	 * @param date 날짜
	 * @return 음력 날짜 문자열
	 */
	public static String getLunarStr(long date) throws Exception {
		return getLunarStr(DEFAULT_DATE_DELIMITER, date);
	}

}
