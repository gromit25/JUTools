/**
 * 
 */
package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * BytesUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
class BytesUtilTest {

	@Test
	void testSplit1() throws Exception {
		
		String test1 = "123|||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
	
	@Test
	void testSplit2() throws Exception {
		
		String test1 = "123||45|||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123||45"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
	
	@Test
	void testSplit3() throws Exception {
		
		String test1 = "123||||||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
	
	@Test
	void testSplit4() throws Exception {
		
		String test1 = "123|||||||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("|6789"));
	}

	@Test
	void testSplit5() throws Exception {
		
		String test1 = "123|||6789|";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789|"));
	}
	
	@Test
	void testSplit6() throws Exception {
		
		String test1 = "123|||6789|||";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
	
	@Test
	void testConcat1() throws Exception {

		String src1 = "abcd";
		String src2 = "efgh";

		byte[] concatenated = BytesUtil.concat(src1.getBytes(), src2.getBytes());
		String concatenatedStr = new String(concatenated);

		assertEquals("abcdefgh", concatenatedStr);
	}
	
	@Test
	void testConcat2() throws Exception {

		String src1 = "abcd";
		String src2 = "efgh";
		String src3 = "ijklmn";

		byte[] concatenated = BytesUtil.concat(src1.getBytes(), src2.getBytes(), src3.getBytes());
		String concatenatedStr = new String(concatenated);

		assertEquals("abcdefghijklmn", concatenatedStr);
	}
	
	@Test
	void testConcat3() throws Exception {
		
		try {
			BytesUtil.concat(null);
			fail("exception is expected");
		} catch(Exception ex) {
			assertTrue(true);
		}
	}
	
	@Test
	void testConcat4() throws Exception {
		
		byte[] concatenated = BytesUtil.concat();
		assertEquals(0, concatenated.length);
	}
	
	@Test
	void testConcat5() throws Exception {

		String src1 = "abcd";
		String src2 = "efgh";

		byte[] concatenated = BytesUtil.concat(src1.getBytes(), null, src2.getBytes());
		String concatenatedStr = new String(concatenated);

		assertEquals("abcdefgh", concatenatedStr);
	}
}
