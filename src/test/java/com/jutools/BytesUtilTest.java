/**
 * 
 */
package com.jutools;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * BytesUtil Ŭ������ ���� ���� �׽�Ʈ Ŭ����
 * 
 * @author jmsohn
 */
public class BytesUtilTest {

	@Test
	public void testSplit1() throws Exception {
		
		String test1 = "123|||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
	
	@Test
	public void testSplit2() throws Exception {
		
		String test1 = "123||45|||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123||45"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
	
	@Test
	public void testSplit3() throws Exception {
		
		String test1 = "123||||||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
	
	@Test
	public void testSplit4() throws Exception {
		
		String test1 = "123|||||||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("|6789"));
	}

	@Test
	public void testSplit5() throws Exception {
		
		String test1 = "123|||6789|";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789|"));
	}
	
	@Test
	public void testSplit6() throws Exception {
		
		String test1 = "123|||6789|||";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertTrue(splitedList.size() == 2);
		assertTrue(new String(splitedList.get(0)).equals("123"));
		assertTrue(new String(splitedList.get(1)).equals("6789"));
	}
}
