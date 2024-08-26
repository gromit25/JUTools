package com.jutools.stat;

/**
 * 데이터를 저장하기 위한 콜백용 인터페이스<br>
 * R-DB나 기타 시계열 DB 등에 저장하기 위한 용도
 * 
 * @author jmsohn
 */
@FunctionalInterface
public interface DataLoader {
	
	/**
	 * 데이터를 저장하기 위한 콜백 메소드
	 * 
	 * @param baseTime 기준 시간
	 * @param data 데이터
	 * @param stat 통계량
	 * @param isLast reset 되기 이전의 최종 호출 여부
	 */
	public void load(long baseTime, double data, Statistic stat, boolean isLast);
}
