package com.jutools;

/**
 *
 *
 * @author jmsohn
 */
public class MapUtil {
  
	/**
	 * 맵의 데이터를 캐스트하여 반환 
	 * 
	 * @param <T> 캐스트할 타입
	 * @param map 맵
	 * @param key 키
	 * @param clazz 캐스타할 타입 클래스
	 * @return 캐스팅된 값
	 */
	public <T> T get(Map<String, ?> map, String key, Class<T> clazz) throws Exception {
		
		// 입력값 검증
		if(map == null) {
			throw new IllegalArgumentException("'map' is null.");
		}
		
		if(key == null) {
			throw new IllegalArgumentException("'key' is null.");
		}
		
		if(clazz == null) {
			throw new IllegalArgumentException("'clazz' is null.");
		}
		
		// 키에 해당하는 값 획득
		Object value = map.get(key);
		
		// 지정된 타입으로 캐스팅하여 반환
		return clazz.cast(value);
	}
}
