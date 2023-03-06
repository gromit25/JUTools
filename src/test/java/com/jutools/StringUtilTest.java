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
			
			double result = StringUtil.calculate("3*4/ 2 + -2 * 2.5 - 1");
			assertEquals(0, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate9() {
		try {
			
			double result = StringUtil.calculate("3*4/(2 + -1)* 2.5 - 1");
			assertEquals(29, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testToThousandCommaStr1_1() {
		String value = StringUtil.toThousandCommaStr((int)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr1_2() {
		String value = StringUtil.toThousandCommaStr(Integer.MAX_VALUE);
		assertEquals("2,147,483,647", value);
	}
	
	@Test
	void testToThousandCommaStr1_3() {
		String value = StringUtil.toThousandCommaStr(Integer.MIN_VALUE);
		assertEquals("-2,147,483,648", value);
	}

	@Test
	void testToThousandCommaStr2_1() {
		String value = StringUtil.toThousandCommaStr((long)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr2_2() {
		String value = StringUtil.toThousandCommaStr(Long.MAX_VALUE);
		assertEquals("9,223,372,036,854,775,807", value);
	}
	
	@Test
	void testToThousandCommaStr2_3() {
		String value = StringUtil.toThousandCommaStr(Long.MIN_VALUE);
		assertEquals("-9,223,372,036,854,775,808", value);
	}
	
	@Test
	void testToThousandCommaStr3_1() {
		String value = StringUtil.toThousandCommaStr((float)10000.123);
		assertEquals("10,000.123", value);
	}
	
	@Test
	void testToThousandCommaStr3_2() {
		String value = StringUtil.toThousandCommaStr((float)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr3_3() {
		String value = StringUtil.toThousandCommaStr(Float.MAX_VALUE);
		assertEquals("340,282,346,638,528,860,000,000,000,000,000,000,000", value);
	}
	
	@Test
	void testToThousandCommaStr3_4() {
		String value = StringUtil.toThousandCommaStr(Float.MIN_VALUE);
		assertEquals("0", value);
	}
	
	@Test
	void testToThousandCommaStr3_5() {
		String value = StringUtil.toThousandCommaStr(Float.MAX_VALUE * -1);
		assertEquals("-340,282,346,638,528,860,000,000,000,000,000,000,000", value);
	}
	
	@Test
	void testToThousandCommaStr4_1() {
		String value = StringUtil.toThousandCommaStr((double)10000.123);
		assertEquals("10,000.123", value);
	}
	
	@Test
	void testToThousandCommaStr4_2() {
		String value = StringUtil.toThousandCommaStr((float)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr4_3() {
		String value = StringUtil.toThousandCommaStr(Double.MAX_VALUE);
		assertEquals("179,769,313,486,231,570,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000", value);
	}
	
	@Test
	void testToThousandCommaStr4_4() {
		String value = StringUtil.toThousandCommaStr(Double.MIN_VALUE);
		assertEquals("0", value);
	}
	
	@Test
	void testToThousandCommaStr4_5() {
		String value = StringUtil.toThousandCommaStr(Double.MAX_VALUE * -1);
		assertEquals("-179,769,313,486,231,570,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000", value);
	}

}
