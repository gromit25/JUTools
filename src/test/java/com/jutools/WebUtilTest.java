package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * WebUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class WebUtilTest {

	@Test
	public void testReplaceEnterToBR1() {
		try {
			
			String contents = "Hello World!\r\nThis is Test.";
			String result = WebUtil.replaceEnterToBr(contents);
			
			assertEquals("Hello World!<br>\r\nThis is Test.", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testReplaceEnterToBR2() {
		try {
			
			String contents = "Hello World!\nThis is Test.";
			String result = WebUtil.replaceEnterToBr(contents);
			
			assertEquals("Hello World!<br>\r\nThis is Test.", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsValidFileName1() {
		try {
			
			String fileName = "test.xls";
			boolean result = WebUtil.isValidFileName(fileName, -1, ".doc", ".xls");
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsValidFileName2() {
		try {
			
			String fileName = "테스트.ppt";
			boolean result = WebUtil.isValidFileName(fileName, -1, ".doc", ".xls");
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsValidFileName3() {
		try {
			
			String fileName = "테스트.jsp\0.doc";
			boolean result = WebUtil.isValidFileName(fileName, -1, ".doc", ".xls");
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsValidFileName4() {
		try {
			
			String fileName = "테스트.doc";
			boolean result = WebUtil.isValidFileName(fileName, 5, ".doc", ".xls");
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsValidFileName5() {
		try {
			
			String fileName = "테스트.doc";
			boolean result = WebUtil.isValidFileName(fileName);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsValidFileName6() {
		try {
			
			String fileName = "테스트.dOc";
			boolean result = WebUtil.isValidFileName(fileName);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsValidFileName7() {
		try {
			
			String fileName = "테스트.dOcabc";
			boolean result = WebUtil.isValidFileName(fileName);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
