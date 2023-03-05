package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.jutools.mathexp.instructions.Instructions;
import com.jutools.mathexp.parser.script.FactorParser;

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
			
			FactorParser parser = new FactorParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("-123.45")).getResult(); 
			
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate2() {
		try {
			
			FactorParser parser = new FactorParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("(-123.45)")).getResult(); 
			
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

}
