package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUtilTest {

	@Test
	void testEscape1() {
		try {
			
			String testMsg = "hello world!\\nName:\\tJohn doe";
			String result = StringUtil.escape(testMsg);
			
			assertEquals("hello world!\nName:\tJohn doe", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testEscape2() {
		try {
			
			String testMsg = "\\0\\b\\f\\n\\r\\t\\\\\\\'\\\"";
			String result = StringUtil.escape(testMsg);
			
			assertEquals("\0\b\f\n\r\t\\\'\"", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

}
