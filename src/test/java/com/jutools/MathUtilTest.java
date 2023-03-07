package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MathUtilTest {

	@Test
	void testCalculate1_1() {
		try {
			
			double result = MathUtil.calculate("-123.45");
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate1_2() {
		try {
			
			double result = MathUtil.calculate("-12,345.67");
			assertEquals(-12345.67, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	
	@Test
	void testCalculate2_1() {
		try {
			
			double result = MathUtil.calculate("(-123.45)");
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	void testCalculate3_1() {
		try {
			
			double result = MathUtil.calculate("3 * 4");
			assertEquals(12, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate3_2() {
		try {
			
			double result = MathUtil.calculate("3 * 4 / 2");
			assertEquals(6, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate3_3() {
		try {
			
			double result = MathUtil.calculate("3,000 * 4 / 2");
			assertEquals(6000, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	
	@Test
	void testCalculate4_1() {
		try {
			
			double result = MathUtil.calculate("3*4 + 2");
			assertEquals(14, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate4_2() {
		try {
			
			double result = MathUtil.calculate("3*(4 + 2,000)");
			assertEquals(6012, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate4_3() {
		try {
			
			double result = MathUtil.calculate("3 +4 - 2");
			assertEquals(5, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate4_4() {
		try {
			
			double result = MathUtil.calculate("3.14 +4,000 - 2");
			assertEquals(4001.14, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	
	@Test
	void testCalculate4_5() {
		try {
			
			double result = MathUtil.calculate("3*4/ 2 + -2 * 2.5 - 1");
			assertEquals(0, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate4_6() {
		try {
			
			double result = MathUtil.calculate("3,000*4/(2,000 + -1,000.0)* 2.5 - 1");
			assertEquals(29, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testToThousandCommaStr1_1() {
		String value = MathUtil.toThousandCommaStr((int)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr1_2() {
		String value = MathUtil.toThousandCommaStr(Integer.MAX_VALUE);
		assertEquals("2,147,483,647", value);
	}
	
	@Test
	void testToThousandCommaStr1_3() {
		String value = MathUtil.toThousandCommaStr(Integer.MIN_VALUE);
		assertEquals("-2,147,483,648", value);
	}

	@Test
	void testToThousandCommaStr2_1() {
		String value = MathUtil.toThousandCommaStr((long)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr2_2() {
		String value = MathUtil.toThousandCommaStr(Long.MAX_VALUE);
		assertEquals("9,223,372,036,854,775,807", value);
	}
	
	@Test
	void testToThousandCommaStr2_3() {
		String value = MathUtil.toThousandCommaStr(Long.MIN_VALUE);
		assertEquals("-9,223,372,036,854,775,808", value);
	}
	
	@Test
	void testToThousandCommaStr3_1() {
		String value = MathUtil.toThousandCommaStr((float)10000.123);
		assertEquals("10,000.123", value);
	}
	
	@Test
	void testToThousandCommaStr3_2() {
		String value = MathUtil.toThousandCommaStr((float)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr3_3() {
		String value = MathUtil.toThousandCommaStr(Float.MAX_VALUE);
		assertEquals("340,282,346,638,528,860,000,000,000,000,000,000,000", value);
	}
	
	@Test
	void testToThousandCommaStr3_4() {
		String value = MathUtil.toThousandCommaStr(Float.MIN_VALUE);
		assertEquals("0", value);
	}
	
	@Test
	void testToThousandCommaStr3_5() {
		String value = MathUtil.toThousandCommaStr(Float.MAX_VALUE * -1);
		assertEquals("-340,282,346,638,528,860,000,000,000,000,000,000,000", value);
	}
	
	@Test
	void testToThousandCommaStr4_1() {
		String value = MathUtil.toThousandCommaStr((double)10000.123);
		assertEquals("10,000.123", value);
	}
	
	@Test
	void testToThousandCommaStr4_2() {
		String value = MathUtil.toThousandCommaStr((float)10000);
		assertEquals("10,000", value);
	}
	
	@Test
	void testToThousandCommaStr4_3() {
		String value = MathUtil.toThousandCommaStr(Double.MAX_VALUE);
		assertEquals("179,769,313,486,231,570,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000", value);
	}
	
	@Test
	void testToThousandCommaStr4_4() {
		String value = MathUtil.toThousandCommaStr(Double.MIN_VALUE);
		assertEquals("0", value);
	}
	
	@Test
	void testToThousandCommaStr4_5() {
		String value = MathUtil.toThousandCommaStr(Double.MAX_VALUE * -1);
		assertEquals("-179,769,313,486,231,570,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000", value);
	}

}
