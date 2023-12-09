package com.jutools;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * 
 * 
 * @author jmsohn
 */
public class TextGenTest {

	@Test
	public void testTextGen1() throws Exception {
		
		HashMap<String, Object> values = new HashMap<>();
		String formatText = "this is test.";
		String message = TextGen.compile(formatText).gen(values);
		
		assertEquals(formatText, message);
	}
	
	@Test
	public void testTextGen2() throws Exception {
		
		HashMap<String, Object> values = new HashMap<>();
		values.put("name", "john doe");
		
		String formatText = "hello, ${name}";
		String message = TextGen.compile(formatText).gen(values);
		
		assertEquals("hello, john doe", message);
	}
	
	@Test
	public void testTextGen3() throws Exception {
		
		HashMap<String, Object> values = new HashMap<>();
		values.put("severity", "fatal");
		
		String formatText = "severity: ${if(severity == 'fatal', 'red', 'yellow')} ";
		String message = TextGen.compile(formatText).gen(values);
		
		assertEquals("severity: red ", message);
	}
	
	@Test
	public void testTextGen4() throws Exception {
		
		HashMap<String, Object> values = new HashMap<>();
		values.put("name", "john doe");
		
		String formatText = "hello, ${na";
		String message = TextGen.compile(formatText).gen(values);
		
		assertEquals("hello, ${na", message);
	}
	
	@Test
	public void testTextGen5() throws Exception {
		
		HashMap<String, Object> values = new HashMap<>();
		values.put("name", "john doe");
		
		String formatText = "hello, ${name}.. ${na";
		String message = TextGen.compile(formatText).gen(values);
		
		assertEquals("hello, john doe.. ${na", message);
	}
}
