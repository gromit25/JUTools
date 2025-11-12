package com.jutools;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
}
