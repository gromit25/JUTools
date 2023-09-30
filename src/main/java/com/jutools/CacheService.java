package com.jutools;

import com.jutools.cache.Cache;
import com.jutools.cache.Loader;

/**
 * 캐시 서비스 클래스<br>
 * 자주 사용되는 코드성 데이터를 DB나 File에서 매번 읽어오지 않고, 일정기간 메모리에 저장 및 필요시 반환    
 * 
 * @author jmsohn
 */
public class CacheService {
	
	/**
	 * 캐시 객체 생성 
	 * 
	 * @param loader 캐시 데이터 공급 객체
	 * @param retainTime 캐시 보유기간, 단위: millisecond
	 * @param cleanUpPeriod 캐시 정리 주기
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Loader<T> loader, long retainTime, String cleanUpPeriod) throws Exception {
		return new Cache<T>(loader, retainTime, cleanUpPeriod);
	}
	
	/**
	 * 캐시 객체 생성<br>
	 * 10분 단위로 캐시 정리 
	 * 
	 * @param loader 캐시 데이터 공급 객체
	 * @param retainTime 캐시 보유기간, 단위: millisecond
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Loader<T> loader, long retainTime) throws Exception {
		return create(loader, retainTime, "*/10 * * * *");
	}
	
	/**
	 * 캐시 객체 생성<br>
	 * 캐시 보유 시간 10분<br>
	 * 10분 단위로 캐시 정리
	 * 
	 * @param loader 캐시 데이터 공급 객체
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Loader<T> loader) throws Exception {
		return create(loader, 10 * 60 * 1000);
	}

}
