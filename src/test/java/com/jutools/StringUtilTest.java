package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.jutools.mathexp.instructions.Instructions;
import com.jutools.mathexp.parser.script.ArithmaticParser;
import com.jutools.mathexp.parser.script.FactorParser;
import com.jutools.mathexp.parser.script.TermParser;

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
			
			ArithmaticParser parser = new ArithmaticParser();
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
			
			ArithmaticParser parser = new ArithmaticParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("(-123.45)")).getResult(); 
			
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	void testCalculate3() {
		try {
			
			ArithmaticParser parser = new ArithmaticParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("3 * 4" )).getResult(); 
			
			assertEquals(12, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate4() {
		try {
			
			ArithmaticParser parser = new ArithmaticParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("3 * 4 / 2" )).getResult(); 
			
			assertEquals(6, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate5() {
		try {
			
			ArithmaticParser parser = new ArithmaticParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("3*4 + 2" )).getResult(); 
			
			assertEquals(14, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate6() {
		try {
			
			ArithmaticParser parser = new ArithmaticParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("3 +4 - 2" )).getResult(); 
			
			assertEquals(5, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testCalculate7() {
		try {
			
			ArithmaticParser parser = new ArithmaticParser();
			double result = (double)Instructions.create()
					.execute(parser.parse("3*4/ 2 + (-2 * 2)" )).getResult(); 
			
			assertEquals(2, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
