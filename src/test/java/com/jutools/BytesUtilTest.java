/**
 * 
 */
package com.jutools;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * BytesUtil 클래스에 대한 단위 테스트 클래스
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
}
