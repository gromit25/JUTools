package com.jutools.stat;

/**
 * 데이터 수집을 위한 콜백용 인터페이스<br>
 * 관측 대상으로 부터 데이터를 일정 주기로 읽어와서 반환
 * 
 * @author jmsohn
 */
@FunctionalInterface
public interface DataAcquistor {
	
	/**
	 * 데이터 수집 메소드
	 * 
	 * @return 수집된 데이터
	 */
	public double acquire();
}
