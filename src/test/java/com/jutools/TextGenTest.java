package com.jutools;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Test
	public void testTextGen6() throws Exception {
		
		List<String> array = new ArrayList<>();
		array.add("john doe");
		
		Map<String, Object> values = new HashMap<>();
		values.put("a", array);
		values.put("i", 0);
		
		String formatText = "${if(a[i] == null, '', a[i])}";
		String message = TextGen.compile(formatText).gen(values);
		
		System.out.println(message);
	}
}
