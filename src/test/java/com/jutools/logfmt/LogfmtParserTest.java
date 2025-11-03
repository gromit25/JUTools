package com.jutools.logfmt;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

/**
 * LogfmtParser 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class LogfmtParserTest {
	
	@Test
	public void testParse1() throws Exception {
		
		String log = "item1=test item2=123 item3=123.45";
		
		LogfmtParser parser = new LogfmtParser();
		Map<String, Object> map = parser.parse(log);
		
		assertEquals("test", map.get("item1"));
		assertEquals(123, map.get("item2"));
		assertEquals(123.45, map.get("item3"));
	}
	
	@Test
	public void testParse2() throws Exception {
		
		String log = "item1=\"test\r\n hello world!\"";
		
		LogfmtParser parser = new LogfmtParser();
		Map<String, Object> map = parser.parse(log);
		
		assertEquals("test\r\n hello world!", map.get("item1"));
	}
}
