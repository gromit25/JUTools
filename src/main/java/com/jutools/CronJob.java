package com.jutools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

/**
 * 크론잡 수행 클래스
 * 
 * @author jmsohn
 */
public class CronJob {
	
	/**
	 * 크론 표현식 클래스
	 * 
	 * @author jmsohn
	 */
	public static class CronExp {
		
		/**
		 * 크론 표현식 설정 가능한 시간의 종류 코드<br>
		 * <pre>
		 * ex) * * * * *
		 *     분 시 일 월 요일
		 * </pre>
		 * 
		 * @author jmsohn
		 */
		private enum CronTimeUnit {
			
			MIN(0, 59),
			HOUR(0, 23),
			DAY(1, 31),
			MONTH(1, 12),
			WEEK(0, 7);		// 0, 7은 일요일, 1부터 월요일, 6은 토요일
			
			/** 최소값 */
			@Getter
			private int lower;
			/** 최대값 */
			@Getter
			private int upper;
			
			/**
			 * 생성자
			 * 
			 * @param lower 최소값
			 * @param upper 최대값
			 */
			CronTimeUnit(int lower, int upper) {
				this.lower = lower;
				this.upper = upper;
			}
		}
		
		/**
		 * 크론 표현식에서 시간 단위별(분/시/일/월/요일) 표현 방법 코드
		 * 
		 * @author jmsohn
		 */
		private enum TimeExpType {
			
			FIXED_LIST("[0-9]+(\\,[0-9]+)*") { // 고정된 시간 형태 : 0,15,30,45
				
				@Override
				int[] makeTimeList(String exp, CronTimeUnit unit) throws Exception {
					
					//
					Pattern fixedTimeP = Pattern.compile("[0-9]+");
					Matcher fixedTimeM = fixedTimeP.matcher(exp);
					
					ArrayList<Integer> timeList = new ArrayList<>();
					while(fixedTimeM.find() == true) {
						
						int time = Integer.parseInt(fixedTimeM.group());
						if(time > unit.getUpper() || time < unit.getLower()) {
							throw new Exception("invalid value:" + time);
						}
						
						timeList.add(time);
					}
					
					return TypeUtil.toIntArray(timeList);
				}
				
			},	
			RANGE("[0-9]+\\-[0-9]+") {		// 시간 범위 형태 : 0-30
				
				@Override
				int[] makeTimeList(String exp, CronTimeUnit unit) throws Exception {
					
					//
					Pattern rangeP = Pattern.compile("(?<lower>[0-9]+)\\-(?<upper>[0-9]+)");
					Matcher rangeM = rangeP.matcher(exp);
					
					if(rangeM.matches() == false) {
						throw new Exception("invalid cron expression:" + exp);
					}
					
					//
					int lower = Integer.parseInt(rangeM.group("lower"));
					int upper = Integer.parseInt(rangeM.group("upper"));
					
					if(lower >= upper) {
						throw new Exception("invalid range(lower, upper): (" + lower + ", " + upper + ")");
					}
					
					if(lower > unit.getUpper() || upper > unit.getUpper()) {
						throw new Exception("invalid range(lower, upper): (" + lower + ", " + upper + ")");
					}
					
					//
					ArrayList<Integer> timeList = new ArrayList<>();
					for(int index = lower; index <= upper; index++) {
						timeList.add(index);
					}
					
					return TypeUtil.toIntArray(timeList);
				}
			},			
			REPEAT("\\*(\\/[0-9]+)?") {		// 반복 형태 : */10
				
				@Override
				int[] makeTimeList(String exp, CronTimeUnit unit) throws Exception {
					
					//
					Pattern rangeP = Pattern.compile("\\*(\\/(?<divider>[0-9]+))?");
					Matcher rangeM = rangeP.matcher(exp);
					
					if(rangeM.matches() == false) {
						throw new Exception("invalid cron expression:" + exp);
					}
					
					//
					int divider = 1;
					
					String dividerStr = rangeM.group("divider");
					if(dividerStr != null) {	
						divider = Integer.parseInt(dividerStr);
					}
					
					//
					ArrayList<Integer> timeList = new ArrayList<>();
					for(int index = unit.getLower(); index <= unit.getUpper(); index++) {
						if(index % divider == 0) {
							timeList.add(index);
						}
					}
					
					return TypeUtil.toIntArray(timeList);
				}
				
			};
			
			/** 시간 단위별 표현에 대한 정규표현식 패턴 객체 */
			private Pattern timeP;
			
			/**
			 * 주어진 표현식을 사용하여 시간 목록을 만듦<br>
			 * ex) FIXED_LIST 타입에 0,10,20이 입력되면<br>
			 *     int[]{0, 10, 20} 만들어 반환함
			 * 
			 * @param exp 시간별 크론 표현식
			 * @param unit 시간 종류(분/시/일/월/요일)
			 * @return 시간 목록 배열
			 */
			abstract int[] makeTimeList(String exp, CronTimeUnit unit) throws Exception;
			
			/**
			 * 생성자
			 * 
			 * @param timePStr
			 */
			TimeExpType(String timePStr) {
				this.timeP = Pattern.compile(timePStr);
			}
			
			/**
			 * 주어진 시간 표현이 현재 표현방법 코드와 일치하는지 여부 반환<br>
			 * 일치하면 true, 일치하지 않으면 false
			 * 
			 * @param timeExp 검사할 시간 표현
			 * @return 일치 여부 반환
			 */
			boolean match(String timeExp) throws Exception {
				return this.timeP.matcher(timeExp).matches();
			}
		}
		
		private static String cronExpPStr;
		
		static {
			String timePStr = "[0-9]+(\\,[0-9]+)*|[0-9]+\\-[0-9]+|\\*(\\/[0-9]+)?";
			CronExp.cronExpPStr = 
					"^(?<min>" + timePStr + ") "
					+ "(?<hour>" + timePStr + ") "
					+ "(?<day>" + timePStr + ") "
					+ "(?<month>" + timePStr + ") "
					+ "(?<dayOfWeek>" + timePStr + ")$";
		}
		
		/** 크론 시간 표현 원본 */
		private String cronExp;
		/** 분 */
		private int[] mins;
		/** 시간 */
		private int[] hours;
		/** 날짜 */
		private int[] days;
		/** 월 */
		private int[] months;
		/** 요일 : 0-7, 0과 7은 일요일, 1은 월요일, 6은 토요일 */
		private int[] daysOfWeek;
		
		/**
		 * 크론 표현식 객체 생성 메소드
		 * 
		 * @param cronExp 크론 표현식
		 * @return 크론 표현식 객체
		 */
		public static CronExp create(String cronExp) throws Exception {
			return new CronExp(cronExp);
		}
		
		/**
		 * 생성자
		 * 
		 * @param cronExp 크론 표현식
		 */
		private CronExp(String cronExp) throws Exception {
			
			this.cronExp = cronExp;
			
			Pattern cronExpP = Pattern.compile(CronExp.cronExpPStr);
			Matcher cronExpM = cronExpP.matcher(cronExp);
			
			if(cronExpM.matches() == false) {
				throw new Exception("invalid cron expression: " + cronExp);
			}

			// 크론 표현식에 설정된 유효 시간을 설정함
			this.mins = this.makeTimeList(cronExpM.group("min"), CronTimeUnit.MIN);
			this.hours = this.makeTimeList(cronExpM.group("hour"), CronTimeUnit.HOUR);
			this.days = this.makeTimeList(cronExpM.group("day"), CronTimeUnit.DAY);
			this.months = this.makeTimeList(cronExpM.group("month"), CronTimeUnit.MONTH);
			this.daysOfWeek = this.makeTimeList(cronExpM.group("dayOfWeek"), CronTimeUnit.WEEK);
		}
		
		/**
		 * 
		 * @param exp
		 * @param unit
		 * @return
		 */
		private int[] makeTimeList(String exp, CronTimeUnit unit) throws Exception {
			
			TimeExpType type = null;
			for(TimeExpType candidateType: TimeExpType.values()) {
				if(candidateType.match(exp) == true) {
					type = candidateType;
					break;
				}
			}
			
			if(type == null) {
				throw new Exception("invalid cron expression:" + exp);
			}
			
			return type.makeTimeList(exp, unit);
		}
		
		/**
		 * 
		 * @return
		 */
		public long getNextTimeInMillis() {
			return getNextTimeInMillis(new GregorianCalendar());
		}
		
		/**
		 * 
		 * @return
		 */
		public long getNextTimeInMillis(Calendar baseTime) {
			
			//
			int min = baseTime.get(Calendar.MINUTE);
			int hour = baseTime.get(Calendar.HOUR_OF_DAY);
			int day = baseTime.get(Calendar.DAY_OF_MONTH);
			int month = baseTime.get(Calendar.MONTH) + 1;
			int year = baseTime.get(Calendar.YEAR);
			
			//
			NextTime minNext = this.getNextTime(new NextTime(min, true), this.mins);
			min = minNext.getTime();
			NextTime hourNext = this.getNextTime(new NextTime(hour, minNext.isRotate()), this.hours);
			hour = hourNext.getTime();
			
			NextTime dayNext = null;
			NextTime monthNext = null;
			
			do {
				
				dayNext = this.getNextTime(new NextTime(day, hourNext.isRotate()), this.days);
				day = dayNext.getTime();
				monthNext = this.getNextTime(new NextTime(month, dayNext.isRotate()), this.months);
				month = monthNext.getTime();
				
				if(monthNext.isRotate() == true) {
					year++;
				}
				
			} while(DateUtil.isValidDate(year, month, day) == false || isValidDayOfWeek(year, month, day) == false);
			
			//
			baseTime.set(Calendar.YEAR, year);
			baseTime.set(Calendar.MONTH, month-1);
			baseTime.set(Calendar.DAY_OF_MONTH, day);
			baseTime.set(Calendar.HOUR_OF_DAY, hour);
			baseTime.set(Calendar.MINUTE, min);
			baseTime.set(Calendar.SECOND, 0);
			baseTime.set(Calendar.MILLISECOND, 0);
			
			//
			return baseTime.getTimeInMillis();
		}
		
		/**
		 * 
		 * @param cur
		 * @param timeList
		 * @return
		 */
		private NextTime getNextTime(NextTime cur, int[] timeList) {
			
			//
			for(int index = 0; index < timeList.length; index++) {
				
				int time = timeList[index];
				
				if(cur.getTime() == time) {
					
					if(cur.isRotate() == true) {
						if(index + 1 == timeList.length) {
							return new NextTime(timeList[0], true);
						} else {
							return new NextTime(timeList[index + 1], false);
						}
					} else {
						return new NextTime(timeList[index], false);
					}
					
				} else if(cur.getTime() < time) {
					return new NextTime(timeList[index], false);
				}
			}
			
			//
			return new NextTime(timeList[0], true);
		}
		
		@Getter
		private class NextTime {
			
			private int time;
			private boolean rotate;
			
			NextTime(int time, boolean rotate) {
				this.time = time;
				this.rotate = rotate;
			}
		}
		
		/**
		 * 주어진 날짜가 설정된 요일 목록에 있는지 여부 반환<br>
		 * 있으면 true, 없으면 false 
		 * 
		 * @param year 년도
		 * @param month 월(1~12)
		 * @param day 날짜
		 * @return 요일 목록에 있는지 여부
		 */
		private boolean isValidDayOfWeek(int year, int month, int day) {
			
			// 주어진 날짜로 생성
			Calendar cal = new GregorianCalendar(year, month-1, day);
			
			// Calendar의 요일은 1-일요일 ~ 7-토요일임
			// cron 표현식에서는 0-일요일, 1-월요일 ~ 6-토요일, 7-일요일이기 떄문에 맞쳐주기 위해 1을 뺌
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
			
			// 주어진 날짜의 요일이 대상 목록에 있는지 확인
			for(int index = 0; index < this.daysOfWeek.length; index++) {
				
				if(dayOfWeek == this.daysOfWeek[index]) {
					return true;
				}
				
				if(dayOfWeek == 0 && this.daysOfWeek[index] == 7) {
					return true;
				}
			}
			
			return false;
		}
		
		/**
		 * 
		 */
		@Override
		public String toString() {
			
			StringBuilder builder = new StringBuilder();
			
			builder
				.append(this.cronExp).append("\n")
				.append(toString(this.mins)).append("\n")
				.append(toString(this.hours)).append("\n")
				.append(toString(this.days)).append("\n")
				.append(toString(this.months)).append("\n")
				.append(toString(this.daysOfWeek));
			
			return builder.toString();
		}
		
		private static String toString(int[] array) {
			
			if(array == null) {
				return "";
			}
			
			StringBuilder builder = new StringBuilder();
			
			for(int index = 0; index < array.length; index++) {
				
				if(index != 0) {
					builder.append(",");
				}
				
				builder.append(array[index]);
			}
			
			return builder.toString();
		}
	}

}
