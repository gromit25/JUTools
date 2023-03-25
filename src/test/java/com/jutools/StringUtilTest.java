package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class StringUtilTest {
	
	@Test
	void test() {
		
		System.out.println(Math.pow(0.0, 0.0));

	}

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
	void testFind1() {
		try {
			
			String testMsg = "hello world!"; 
			int[] result = StringUtil.find(testMsg, true, "hello", "world");
			
			assertEquals(0, result[0]);
			assertEquals(6, result[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testFind2() {
		try {
			
			String testMsg = "aaabbbbbb"; 
			int[] result = StringUtil.find(testMsg, true, "aabb", "bb");
			
			assertEquals(1, result[0]);
			assertEquals(3, result[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testFind3() {
		try {
			
			String testMsg = "aaabbbbbb"; 
			int[] result = StringUtil.find(testMsg, true, "ccc", "bb");
			
			assertEquals(-1, result[0]);
			assertEquals(3, result[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testJoin1() {
		try {
			 
			String result = StringUtil.join("|", "abc", "def");
			
			assertEquals("abc|def", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testJoin2() {
		try {
			 
			String result = StringUtil.join("|", "abc", "def", "ghi");
			
			assertEquals("abc|def|ghi", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testJoin3() {
		try {
			 
			String result = StringUtil.join("|", new String[]{"abc", "def", "ghi"});
			
			assertEquals("abc|def|ghi", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testReplaceEnterToBR1() {
		try {
			
			String contents = "Hello World!\r\nThis is Test.";
			String result = StringUtil.replaceEnterToBr(contents);
			
			assertEquals("Hello World!<br>\r\nThis is Test.", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testReplaceEnterToBR2() {
		try {
			
			String contents = "Hello World!\nThis is Test.";
			String result = StringUtil.replaceEnterToBr(contents);
			
			assertEquals("Hello World!<br>\r\nThis is Test.", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testIsValidFileName1() {
		try {
			
			String fileName = "test.xls";
			boolean result = StringUtil.isValidFileName(fileName, -1, ".doc", ".xls");
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testIsValidFileName2() {
		try {
			
			String fileName = "테스트.ppt";
			boolean result = StringUtil.isValidFileName(fileName, -1, ".doc", ".xls");
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testIsValidFileName3() {
		try {
			
			String fileName = "테스트.jsp\0.doc";
			boolean result = StringUtil.isValidFileName(fileName, -1, ".doc", ".xls");
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testIsValidFileName4() {
		try {
			
			String fileName = "테스트.doc";
			boolean result = StringUtil.isValidFileName(fileName, 5, ".doc", ".xls");
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
