package com.jutools.cache;

/**
 * 캐시에 데이터를 제공할 인터페이스
 * 
 * @param <T> 반환 타입
 * @author jmsohn
 */
@FunctionalInterface
public interface Loader<T> {
	
	/**
	 * 주어진 키에 해당하는 데이터를 반환
	 * 
	 * @param key 키
	 * @return 데이터
	 */
	public T get(String key) throws Exception;

}
