package com.jutools;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.jutools.cache.Cache;
import com.jutools.cache.Loader;

/**
 * 캐시 테스트 클래스
 * 
 * @author jmsohn
 */
public class CacheTest {

	@Test
	public void testCache1() throws Exception {
		
		HashMap<String, String> values = new HashMap<>();
		values.put("test1", "test1 value");
		values.put("test2", "test2 value");
		
		Loader<String> loader = (key) -> {
			return values.get(key);
		};
		
		Cache<String> cache = CacheService.create(loader, 10 * 1000);
		assertEquals("test1 value", cache.get("test1"));
	}
	
	@Test
	public void testCache2() throws Exception {
		
		HashMap<String, String> values = new HashMap<>();
		values.put("test1", "test1 value");
		values.put("test2", "test2 value");
		
		Cache<String> cache = CacheService
			.create(
				(key) -> {
					return values.get(key);
				},
				1000, "* * * * * *")
			.putAll(values);
		
		// 추가된 데이터 정상 여부 확인
		assertEquals(2, cache.size());

		// 0.8초 후 "test1"에 대해 참조후 1.6 초 후 데이터의 개수를 확인
		// "test2"는 1초 후 expire 되기 때문에 최종적으로 "test1" 만 남음
		Thread.sleep(800);	
		assertEquals("test1 value", cache.get("test1"));
		Thread.sleep(800);
		assertEquals(1, cache.size());
	}
}
