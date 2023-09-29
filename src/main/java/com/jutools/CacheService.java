package com.jutools;

import com.jutools.cache.Cache;
import com.jutools.cache.Loader;

/**
 * 캐시 서비스 클래스
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
	 * 1분 단위로 캐시 정리 
	 * 
	 * @param loader 캐시 데이터 공급 객체
	 * @param retainTime 캐시 보유기간, 단위: millisecond
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Loader<T> loader, long retainTime) throws Exception {
		return create(loader, retainTime, "* * * * *");
	}
	
	/**
	 * 캐시 객체 생성<br>
	 * 캐시 보유 시간 1분<br>
	 * 1분 단위로 캐시 정리
	 * 
	 * @param loader 캐시 데이터 공급 객체
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Loader<T> loader) throws Exception {
		return create(loader, 60 * 1000);
	}

}
