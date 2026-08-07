package com.jutools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * CollectionUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class CollectionUtilTest {

	@Test
	public void testMinus1() {
		
		List<String> p1 = List.of("1", "2", "3", "4", "5");
		List<String> p2 = List.of("3", "7");
		
		List<String> result = CollectionUtil.minus(p1, p2);
		
		assertEquals("[1, 2, 4, 5]", result.toString());
	}
	
	@Test
	public void testMinus2() {
		
		List<String> p1 = List.of("1", "2", "3", "4", "5");
		List<String> p2 = null;
		
		List<String> result = CollectionUtil.minus(p1, p2);
		
		assertEquals("[1, 2, 3, 4, 5]", result.toString());
	}
	
	@Test
	public void testMinus3() {
		
		List<String> p1 = null;
		List<String> p2 = List.of("3", "7");
		
		List<String> result = CollectionUtil.minus(p1, p2);
		
		assertEquals("[]", result.toString());
	}
	
	@Test
	public void testContainsAll1() {
		
		Map<String, String> map = Map.of(
			"A", "test1",
			"B", "test2",
			"C", "test3"
		);
		
		boolean result = CollectionUtil.containsAll(map, "B", "C");
		
		assertTrue(result);
	}
	
	@Test
	public void testContainsAll2() {
		
		Map<String, String> map = Map.of(
			"A", "test1",
			"B", "test2",
			"C", "test3"
		);
		
		boolean result = CollectionUtil.containsAll(map, "B", "D");
		
		assertFalse(result);
	}
	
	@Test
	public void testContainsAll3() {
		
		Map<String, String> map = Map.of(
			"A", "test1",
			"B", "test2",
			"C", "test3"
		);
		
		boolean result = CollectionUtil.containsAll(map);
		
		assertTrue(result);
	}
}
