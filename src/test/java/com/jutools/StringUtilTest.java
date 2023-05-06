package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class StringUtilTest {
	
	@Test
	void test() throws Exception  {

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
			int[] result = StringUtil.find(testMsg, false, "hello", "world");
			
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
			int[] result = StringUtil.find(testMsg, false, "aabb", "bb");
			
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
			int[] result = StringUtil.find(testMsg, false, "ccc", "bb");
			
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
	
	@Test
	void testIsValidFileName5() {
		try {
			
			String fileName = "테스트.doc";
			boolean result = StringUtil.isValidFileName(fileName);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testIsValidFileName6() {
		try {
			
			String fileName = "테스트.dOc";
			boolean result = StringUtil.isValidFileName(fileName);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testIsValidFileName7() {
		try {
			
			String fileName = "테스트.dOcabc";
			boolean result = StringUtil.isValidFileName(fileName);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard1() {
		try {
			
		    String pattern = "abc*def";
		    String input = "abc111def";
			
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard2() {
		try {
			
		    String pattern = "abc?def";
		    String input = "abc1def";
			
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard3() {
		try {
			
		    String pattern = "abc?def";
		    String input = "abc11def";
			
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	@Test
	void testMatchWildcard4() {
		try {
			
		    String pattern = "abc";
		    String input = "abcdef";

		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard5() {
		try {
			
		    String pattern = "abc";
		    String input = "abc";
			
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard6() {
		try {
			
		    String pattern = "*";
		    String input = "abcdef";
			
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard7() {
		try {
			
		    String pattern = "??";
		    String input = "ab";
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard8() {
		try {
			
		    String pattern = "???";
		    String input = "ab";
		    
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard9() {
		try {
			
		    String pattern = "??";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard10() {
		try {
			
		    String pattern = "a??";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard11() {
		try {
			
		    String pattern = "a?*";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard12() {
		try {
		    
		    String pattern = "a?*";
		    String input = "a";
		    
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard13() {
		try {
			
		    String pattern = "?*a";
		    String input = "ba";

		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard14() {
		try {
			
		    String pattern = "?*a";
		    String input = "a";
			
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard15() {
		try {
			
		    String pattern = "A?*";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(input, pattern, true);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard16() {
		try {
			
		    String pattern = "A?*";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard17() {
		try {
			
		    String pattern = "abc?DEF";
		    String input = "ABC1def";
			
		    boolean result = StringUtil.matchWildcard(input, pattern, true);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testMatchWildcard18() {
		try {
			
		    String pattern = "abc?DEF";
		    String input = "ABC1def";
			
		    boolean result = StringUtil.matchWildcard(input, pattern);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testEncryptSHA512_1() {
		try {
			
			String msg = "Hello World";
			String encryptedMsg = StringUtil.encryptSHA512(msg);
			
			System.out.println(encryptedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testEncryptDecryptAES1() {
		try {
			
			String key = StringUtil.genAES128Key();
			String msg = "Hello World";
			
			String encryptedMsg = StringUtil.encryptAES(key, msg);
			String decryptedMsg = StringUtil.decryptAES(key, encryptedMsg);
			
			assertEquals(msg, decryptedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testEncryptDecryptAES2() {
		try {
			
			// key는 16 byte 이어야 함
			String key = StringUtil.genAES128Key();
			String msg = "테스트 입니다.";
			
			String encryptedMsg = StringUtil.encryptAES(key, msg);
			String decryptedMsg = StringUtil.decryptAES(key, encryptedMsg);
			
			assertEquals(msg, decryptedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testGenAES128Key() {
		try {
			
			String key = StringUtil.genAES128Key();
			System.out.println(key);
			
			assertEquals(32, key.length());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testGenAES256Key() {
		try {
			
			String key = StringUtil.genAES256Key();
			System.out.println(key);
			
			assertEquals(64, key.length());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testChangeStr1() {
		try {
			
			String str = "abcdefghijk";
			String toStr = "0123456789";
			
			StringUtil.changeStr(str, toStr);
			
			assertEquals("0123456789", str);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
