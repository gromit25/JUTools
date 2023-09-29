package com.jutools;

import java.util.function.Supplier;

import com.jutools.cache.Cache;

/**
 * 캐시 서비스 클래스
 * 
 * @author jmsohn
 */
public class CacheService {
	
	/**
	 * 캐시 객체 생성 
	 * 
	 * @param supplier 캐시 데이터 공급 객체
	 * @param retainTime 캐시 보유기간, 단위: millisecond
	 * @param cleanUpPeriod 캐시 정리 주기
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Supplier<T> supplier, long retainTime, String cleanUpPeriod) throws Exception {
		return new Cache<T>(supplier, retainTime, cleanUpPeriod);
	}
	
	/**
	 * 캐시 객체 생성<br>
	 * 1분 단위로 캐시 정리 
	 * 
	 * @param supplier 캐시 데이터 공급 객체
	 * @param retainTime 캐시 보유기간, 단위: millisecond
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Supplier<T> supplier, long retainTime) throws Exception {
		return create(supplier, retainTime, "* * * * *");
	}
	
	/**
	 * 캐시 객체 생성<br>
	 * 캐시 보유 시간 1분<br>
	 * 1분 단위로 캐시 정리
	 * 
	 * @param supplier 캐시 데이터 공급 객체
	 * @return 캐시 객체
	 */
	public static <T> Cache<T> create(Supplier<T> supplier) throws Exception {
		return create(supplier, 60 * 1000);
	}

}
