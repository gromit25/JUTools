package com.jutools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * StringUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class StringUtilTest {
	
	@Test
	public void testEscape1() {
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
	public void testEscape2() {
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
	public void testEscape3() {
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
	public void testFind1() {
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
	public void testFind2() {
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
	public void testFind3() {
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
	public void testFind4() {
		try {
			
			String testMsg = "aaaBBbbbb"; 
			int[] result = StringUtil.find(testMsg, true, "ccc", "bb");
			
			assertEquals(-1, result[0]);
			assertEquals(3, result[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testJoin1() {
		try {
			 
			String result = StringUtil.join("|", "abc", "def");
			
			assertEquals("abc|def", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testJoin2() {
		try {
			 
			String result = StringUtil.join("|", "abc", "def", "ghi");
			
			assertEquals("abc|def|ghi", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testJoin3() {
		try {
			 
			String result = StringUtil.join("|", new String[]{"abc", "def", "ghi"});
			
			assertEquals("abc|def|ghi", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard1() {
		try {
			
		    String pattern = "abc*def";
		    String input = "abc111def";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard2_1() {
		try {
			
		    String pattern = "abc?def";
		    String input = "abc1def";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard2_2() {
		try {
			
		    String pattern = "test?1";
		    String input = "test11";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard2_3() {
		try {
			
		    String pattern = "test*1";
		    String input = "test11";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard2_4() {
		try {
			
		    String pattern = "test?*1";
		    String input = "test11";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard3() {
		try {
			
		    String pattern = "abc?def";
		    String input = "abc11def";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	@Test
	public void testMatchWildcard4() {
		try {
			
		    String pattern = "abc";
		    String input = "abcdef";

		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard5() {
		try {
			
		    String pattern = "abc";
		    String input = "abc";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard6() {
		try {
			
		    String pattern = "*";
		    String input = "abcdef";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard7() {
		try {
			
		    String pattern = "??";
		    String input = "ab";
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard8() {
		try {
			
		    String pattern = "???";
		    String input = "ab";
		    
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard9() {
		try {
			
		    String pattern = "??";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard10() {
		try {
			
		    String pattern = "a??";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard11() {
		try {
			
		    String pattern = "a?*";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard12() {
		try {
		    
		    String pattern = "a?*";
		    String input = "a";
		    
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard13() {
		try {
			
		    String pattern = "?*a";
		    String input = "ba";

		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard14() {
		try {
			
		    String pattern = "?*a";
		    String input = "a";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard15() {
		try {
			
		    String pattern = "A?*";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(pattern, true, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard16() {
		try {
			
		    String pattern = "A?*";
		    String input = "abc";
		    
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard17() {
		try {
			
		    String pattern = "abc?DEF";
		    String input = "ABC1def";
			
		    boolean result = StringUtil.matchWildcard(pattern, true, input);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard18() {
		try {
			
		    String pattern = "abc?DEF";
		    String input = "ABC1def";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMatchWildcard19() {
		try {
			
		    String pattern = "abcdef";
		    String input = "def";
			
		    boolean result = StringUtil.matchWildcard(pattern, input);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testChangeStr1() {
		try {
			
			String str = "abcdefghijk";
			String toStr = "0123456789";
			
			// JDK 1.8 이상일 경우 오류 발생함
			StringUtil.changeStr(str, toStr);
			
			assertEquals("0123456789", str);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testSplitFirst1() {
		try {
			
			String[] splited = StringUtil.splitFirst("test1> test2", "\\s*>\\s*");
			
			assertEquals("test1", splited[0]);
			assertEquals("test2", splited[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testSplitFirst2() {
		try {
			
			String[] splited = StringUtil.splitFirst("test1> test2 >test3", "\\s*>\\s*");
			
			assertEquals("test1", splited[0]);
			assertEquals("test2 >test3", splited[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testSplitFirstN1() {
		try {
			
			String[] splited = StringUtil.splitFirstN("test1> test2 >test3 > test4", "\\s*>\\s*", 2);
			
			assertEquals("test1", splited[0]);
			assertEquals("test2", splited[1]);
			assertEquals("test3 > test4", splited[2]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testSplitFirstN2() {
		try {
			
			String[] splited = StringUtil.splitFirstN("test1", "\\s*>\\s*", 2);
			
			assertEquals("test1", splited[0]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	public void testSplitLast1() {
		try {
			
			String[] splited = StringUtil.splitLast("test1> test2", "\\s*>\\s*");
			
			assertEquals("test1", splited[0]);
			assertEquals("test2", splited[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testSplitLast2() {
		try {
			
			String[] splited = StringUtil.splitLast("test1> test2 >test3", "\\s*>\\s*");
			
			assertEquals("test1> test2", splited[0]);
			assertEquals("test3", splited[1]);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testTrimMultiLine1() {
		try {
			
			String msg = "   Hello world!    ";
			String trimedMsg = StringUtil.trimMultiLine(msg);
			
			assertEquals("Hello world!", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testTrimMultiLine2() {
		try {
			
			String msg = "";
			String trimedMsg = StringUtil.trimMultiLine(msg);
			
			assertEquals("", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testTrimMultiLine3() {
		try {
			
			String msg = "   \t  \t  ";
			String trimedMsg = StringUtil.trimMultiLine(msg);
			
			assertEquals("", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testTrimMultiLine4() {
		try {
			
			String msg = null;
			String trimedMsg = StringUtil.trimMultiLine(msg);
			
			assertNull(trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testTrimMultiLine5() {
		try {
			
			String msg = "  Hello \t world!  \n  \t    test   ";
			String trimedMsg = StringUtil.trimMultiLine(msg);
			
			assertEquals("Hello \t world!\ntest", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testTrimMultiLine6() {
		try {
			
			String msg = "  Hello \t world! \r \r\n  \t  \n  \t    test   ";
			String trimedMsg = StringUtil.trimMultiLine(msg);
			
			assertEquals("Hello \t world!\r\n\ntest", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testTrimMultiLine7() {
		try {
			
			String msg = " \r\n Hello \t world! \r \r\n  \t  \r\n  \t    test   ";
			String trimedMsg = StringUtil.trimMultiLine(msg, true);
			
			assertEquals("Hello \t world!\r\ntest", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testLTrimMultiLine1() {
		try {
			
			String msg = "   Hello World!   ";
			String trimedMsg = StringUtil.ltrimMultiLine(msg);
			
			assertEquals("Hello World!   ", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testRTrimMultiLine1() {
		try {
			
			String msg = "   Hello World!   ";
			String trimedMsg = StringUtil.rtrimMultiLine(msg);
			
			assertEquals("   Hello World!", trimedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
