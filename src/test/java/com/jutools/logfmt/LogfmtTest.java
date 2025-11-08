package com.jutools.logfmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

/**
 * LogfmtParser 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class LogfmtTest {
	
	@Test
	public void testToMap1() throws Exception {
		
		String log = "item1=test item2=123 item3=123.45";
		
		Map<String, Object> map = Logfmt.toMap(log);
		
		assertEquals("test", map.get("item1"));
		assertEquals(123, map.get("item2"));
		assertEquals(123.45, map.get("item3"));
	}
	
	@Test
	public void testToMap2() throws Exception {
		
		String log = "item1=\"test\r\n hello world!\"";
		
		Map<String, Object> map = Logfmt.toMap(log);
		
		assertEquals("test\r\n hello world!", map.get("item1"));
	}

	@Test
	public void testToMap3() throws Exception {
		
		String log = "item1=test item2= item3=123.45";
		
		Map<String, Object> map = Logfmt.toMap(log);
		
		assertEquals("", map.get("item2"));
	}
	
	@Test
	public void testToMapException1() throws Exception {
		
		String log = "item1=\"test\r\n hello world!";
		
		try {
			
			Logfmt.toMap(log);
			fail();
			
		} catch(Exception ex) {
			
			assertTrue(true);
		}
	}
}
