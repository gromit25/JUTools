package com.jutools;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * FileUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class FileUtilTest {

	@Test
	public void testReadAllBytes1() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		byte[] read = FileUtil.readAllBytes(testFile);
		assertEquals(1597, read.length);

	}
	
	@Test
	public void testReadAllBytes2() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		try {
			FileUtil.readAllBytes(testFile, 0);
			assertTrue(false);
		} catch(Exception ex) {
			assertTrue(true);
		}

	}
	
	@Test
	public void testReadNBytes1() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		byte[] read = FileUtil.readNBytes(testFile, 10);
		assertTrue("별 헤는".equals(new String(read)));
	}
	
	@Test
	public void testReadNBytes2() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		byte[] read = FileUtil.readNBytes(testFile, 20, 10);
		assertTrue("별 헤는 밤 - 윤".equals(new String(read)));
	}
}
