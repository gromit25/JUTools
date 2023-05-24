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
				protected int[] makeTimeListByExpType(String exp, CronTimeUnit unit) throws Exception {
					
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
				protected int[] makeTimeListByExpType(String exp, CronTimeUnit unit) throws Exception {
					
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
				protected int[] makeTimeListByExpType(String exp, CronTimeUnit unit) throws Exception {
					
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
			protected abstract int[] makeTimeListByExpType(String exp, CronTimeUnit unit) throws Exception;
			
			/**
			 * 생성자
			 * 
			 * @param timePStr
			 */
			TimeExpType(String timePStr) {
				this.timeP = Pattern.compile(timePStr);
			}
			
			/**
			 * 크론 시간 표현 방식별로 
			 * 
			 * @param exp
			 * @param unit
			 * @return
			 */
			static int[] makeTimeList(String exp, CronTimeUnit unit) throws Exception {
				
				// 적합한 시간 단위별 표현 종류를 찾음
				TimeExpType type = null;
				for(TimeExpType candidateType: TimeExpType.values()) {
					if(candidateType.match(exp) == true) {
						type = candidateType;
						break;
					}
				}
				
				// 없을 경우, 예외 발생
				if(type == null) {
					throw new Exception("invalid cron expression:" + exp);
				}
				
				// 시간 단위별 표현에서 시간 목록을 반환
				return type.makeTimeListByExpType(exp, unit);
			}
			
			/**
			 * 주어진 시간 표현이 현재 표현방법 코드와 일치하는지 여부 반환<br>
			 * 일치하면 true, 일치하지 않으면 false
			 * 
			 * @param timeExp 검사할 시간 표현
			 * @return 일치 여부 반환
			 */
			private boolean match(String timeExp) throws Exception {
				return this.timeP.matcher(timeExp).matches();
			}
		}
		
		/** 크론 표현의 정규 표현식 문자열 */
		private static String cronExpPStr;
		
		static {
			
			// 클래스 로딩시 크론 표현의 정규 표현식 문자열을 만듦
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
			this.mins = TimeExpType.makeTimeList(cronExpM.group("min"), CronTimeUnit.MIN);
			this.hours = TimeExpType.makeTimeList(cronExpM.group("hour"), CronTimeUnit.HOUR);
			this.days = TimeExpType.makeTimeList(cronExpM.group("day"), CronTimeUnit.DAY);
			this.months = TimeExpType.makeTimeList(cronExpM.group("month"), CronTimeUnit.MONTH);
			this.daysOfWeek = TimeExpType.makeTimeList(cronExpM.group("dayOfWeek"), CronTimeUnit.WEEK);
		}
		
		/**
		 * 현재 시간에서 가장 가까운 다음 실행 시간 반환
		 * 
		 * @return 가장 가까운 다음 실행 시간(단위: ms)
		 */
		public long getNextTimeInMillis() {
			
			// 현재 시간을 가져옴
			Calendar cal = new GregorianCalendar();
			cal.setTimeInMillis(System.currentTimeMillis());
			
			return getNextTimeInMillis(cal);
		}
		
		/**
		 * 기준 시간(baseTime)에서 가장 가까운 다음 실행 시간 반환
		 * 
		 * @param baseTime 기준 시간
		 * @return 가장 가까운 다음 실행 시간(단위: ms)
		 */
		public long getNextTimeInMillis(Calendar baseTime) {
			
			// 시간 단위 별(분/시/일/월/년도, 요일 대신 년도) 현재 시간을 가져옴 
			int min = baseTime.get(Calendar.MINUTE);
			int hour = baseTime.get(Calendar.HOUR_OF_DAY);
			int day = baseTime.get(Calendar.DAY_OF_MONTH);
			int month = baseTime.get(Calendar.MONTH) + 1;
			int year = baseTime.get(Calendar.YEAR);
			
			// 분의 다음 시간을 가져옴
			NextTime minNext = this.getNextTime(new NextTime(min, true), this.mins);
			min = minNext.getTime();
			
			// 시의 다음 시간을 가져옴
			NextTime hourNext = this.getNextTime(new NextTime(hour, minNext.isRolled()), this.hours);
			hour = hourNext.getTime();
			
			// 날짜 및 월의 다음 시간을 가져옴
			NextTime dayNext = null;
			NextTime monthNext = null;
			
			// 다음 날짜가 존재하지 않는 날짜이면, 존재하는 날짜가 나올때 까지 while 문 반복
			// ex) 2023.02.30이면 다시 다음 날짜를 가져와 검사
			//
			// 또한, 다음 날짜가 설정된 요일 목록에 없는 경우에도 다음 날짜를 다시 가져오도록 함 
			do {
				
				dayNext = this.getNextTime(new NextTime(day, hourNext.isRolled()), this.days);
				day = dayNext.getTime();
				monthNext = this.getNextTime(new NextTime(month, dayNext.isRolled()), this.months);
				month = monthNext.getTime();
				
				// 월이 한바퀴 돌아(roll) 월 목록의 처음으로 이동하면
				// 년도를 하나 올림
				if(monthNext.isRolled() == true) {
					year++;
				}
				
				// 다음 날짜가 존재하는 날짜인지, 요일 목록에 있는 날짜인지 확인 
			} while(DateUtil.isValidDate(year, month, day) == false || isValidDayOfWeek(year, month, day) == false);
			
			// 다음 날짜를 설정하고 long 값으로 반환
			Calendar nextTime = new GregorianCalendar();
			
			nextTime.set(Calendar.YEAR, year);
			nextTime.set(Calendar.MONTH, month-1);
			nextTime.set(Calendar.DAY_OF_MONTH, day);
			nextTime.set(Calendar.HOUR_OF_DAY, hour);
			nextTime.set(Calendar.MINUTE, min);
			nextTime.set(Calendar.SECOND, 0);
			nextTime.set(Calendar.MILLISECOND, 0);
			
			return nextTime.getTimeInMillis();
		}
		
		/**
		 * 시간 단위 별(분/시/일/월/요일) 시간 목록 중<br>
		 * 현재 시간(cur)가 가장 가까운 다음 시간 및 시간 목록의 처음으로 이동(roll)하였는지 여부 반환<br>
		 * ex)<br>
		 * 만일 timeList=[1,3,8] 이고, 현재 시간이 7 이면<br>
		 * 가까운 다음 시간은 8이고, roll 여부는 false 임<br>
		 * <br>
		 * 만일 timeList=[1,3,8] 이고, 현재 시간이 9 이면<br>
		 * 가까운 다음 시간은 1이고, roll 여부는 true 임<br>
		 * 
		 * @param cur 현재 시간 및 이전 시간 단위에서 roll이 있었는지 여부
		 * @param timeList 시간 목록
		 * @return 가장 가까운 시간과 시간 목록의 처음으로 이동(roll)하였는지 여부
		 */
		private NextTime getNextTime(NextTime cur, int[] timeList) {
			
			// 시간 목록을 순회하면서, 시간 목록의 시간이 현재 시간과 같거나 큰 경우 처리함
			for(int index = 0; index < timeList.length; index++) {
				
				int time = timeList[index];
				
				if(cur.getTime() == time) {
					
					if(cur.isRolled() == true) {
						if(index + 1 == timeList.length) {
							
							// 시간 목록의 시간과 현재 시간이 같고,
							// 시간 목록에서 가장 가까운 시간이 시간 목록의 가장 마지막에 있고,
							// 이전 시간 단위에서 roll이 있었던 경우
							// ex)
							// timeList=[1,3,8], cur의 시간 값이 8이고, 이전 시간에서 roll 이 true 인 경우
							// 1, true를 반환함
							return new NextTime(timeList[0], true);
							
						} else {
							
							// 시간 목록의 시간과 현재 시간이 같고,
							// 시간 목록에서 가장 가까운 시간이 시간 목록의 가장 마지막이 아니고
							// 이전 시간 단위에서 roll이 있었던 경우
							// ex)
							// timeList=[1,3,8], cur의 시간 값이 3이고, 이전 시간에서 roll 이 true 인 경우
							// 8, false를 반환함
							return new NextTime(timeList[index + 1], false);
						}
					} else {
						
						// 시간 목록의 시간과 현재 시간이 일치하면서,
						// 이전 시간 단위에서 roll이 없었던 경우
						// ex)
						// timeList=[1,3,8], cur의 시간 값이 8이고, 이전 시간에서 roll 이 false 인 경우
						// 8, false를 반환함
						return new NextTime(timeList[index], false);
					}
					
				} else if(cur.getTime() < time) {
					
					// 시간 목록의 시간이 현재 시간보다 큰 경우
					// ex)
					// timeList=[1,3,8], cur의 시간 값이 6일 경우,
					//   -> 이때는, 이전 시간 roll여부와 상관 없이, time 값(즉 timeList[index])을 설정함
					//   -> 만일 cur 시간 값이 9일 경우, 1,3,8 중 9보다 큰값이 없기 때문에
					//      for 문을 빠져 나가게 됨
					// 8, false를 반환함
					return new NextTime(timeList[index], false);
				}
			}
			
			// 시간 목록의 시간이 현재 시간보다 같거나 큰 경우가 없는 경우
			// ex)
			// timeList=[1,3,8], cur의 시간 값이 9일 경우,
			// 1, true를 반환함
			return new NextTime(timeList[0], true);
		}
		
		@Getter
		private class NextTime {
			
			private int time;
			private boolean rolled;
			
			NextTime(int time, boolean rolled) {
				this.time = time;
				this.rolled = rolled;
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
