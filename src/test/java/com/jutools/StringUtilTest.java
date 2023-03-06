package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class StringUtilTest {

	@Test
	void testEscape1() {
		try {
			
			String testMsg = "hello world!\\n이름:\\tJohn doe";
			String result = StringUtil.escape(testMsg);
			
			assertEquals("hello world!\n이름:\tJohn doe", result);
			
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
	
	@Test
	void testEscape3() {
		try {
			
			// "가나다라마바사"
			String testMsg = "\\uAC00\\uB098\\uB2E4\\uB77C\\uB9C8\\uBC14\\uC0AC"; 
			String result = StringUtil.escape(testMsg);
			
			assertEquals("가나다라마바사", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	void testCalculate1() {
		try {
			
			double result = StringUtil.calculate("-123.45");
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate2() {
		try {
			
			double result = StringUtil.calculate("(-123.45)");
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	void testCalculate3() {
		try {
			
			double result = StringUtil.calculate("3 * 4");
			assertEquals(12, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate4() {
		try {
			
			double result = StringUtil.calculate("3 * 4 / 2");
			assertEquals(6, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate5() {
		try {
			
			double result = StringUtil.calculate("3*4 + 2");
			assertEquals(14, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate6() {
		try {
			
			double result = StringUtil.calculate("3*(4 + 2)");
			assertEquals(18, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate7() {
		try {
			
			double result = StringUtil.calculate("3 +4 - 2");
			assertEquals(5, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate8() {
		try {
			
			double result = StringUtil.calculate("3*4/ 2 + -2 * 2 - 1");
			assertEquals(1, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
