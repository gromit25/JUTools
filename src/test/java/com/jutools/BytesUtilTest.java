package com.jutools;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.junit.Test;

/**
 * BytesUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class BytesUtilTest {

	@Test
	public void testSplit1() throws Exception {
		
		String test1 = "123|||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertEquals(2, splitedList.size());
		assertEquals("123", new String(splitedList.get(0)));
		assertEquals("6789", new String(splitedList.get(1)));
	}
	
	@Test
	public void testSplit2() throws Exception {
		
		String test1 = "123||45|||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertEquals(2, splitedList.size());
		assertEquals("123||45", new String(splitedList.get(0)));
		assertEquals("6789", new String(splitedList.get(1)));
	}
	
	@Test
	public void testSplit3() throws Exception {
		
		String test1 = "123||||||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertEquals(3, splitedList.size());
		assertEquals("123", new String(splitedList.get(0)));
		assertEquals("", new String(splitedList.get(1)));
		assertEquals("6789", new String(splitedList.get(2)));
	}
	
	@Test
	public void testSplit4() throws Exception {
		
		String test1 = "123|||||||6789";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertEquals(3, splitedList.size());
		assertEquals("123", new String(splitedList.get(0)));
		assertEquals("", new String(splitedList.get(1)));
		assertEquals("|6789", new String(splitedList.get(2)));
	}

	@Test
	public void testSplit5() throws Exception {
		
		String test1 = "123|||6789|";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertEquals(2, splitedList.size());
		assertEquals("123", new String(splitedList.get(0)));
		assertEquals("6789|", new String(splitedList.get(1)));
	}
	
	@Test
	public void testSplit6() throws Exception {
		
		String test1 = "123|||6789|||";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes());
	       
		assertEquals(2, splitedList.size());
		assertEquals("123", new String(splitedList.get(0)));
		assertEquals("6789", new String(splitedList.get(1)));
	}
	
	@Test
	public void testSplit7() throws Exception {
		
		String test1 = "123|||6789|||";
	       
		ArrayList<byte[]> splitedList = BytesUtil.split(test1.getBytes(), "|||".getBytes(), true);
	       
		assertEquals(3, splitedList.size());
		assertEquals("123", new String(splitedList.get(0)));
		assertEquals("6789", new String(splitedList.get(1)));
		assertEquals("", new String(splitedList.get(2)));
	}
	
	@Test
	public void testConcat1() throws Exception {

		String src1 = "abcd";
		String src2 = "efgh";

		byte[] concatenated = BytesUtil.concat(src1.getBytes(), src2.getBytes());
		String concatenatedStr = new String(concatenated);

		assertEquals("abcdefgh", concatenatedStr);
	}
	
	@Test
	public void testConcat2() throws Exception {

		String src1 = "abcd";
		String src2 = "efgh";
		String src3 = "ijklmn";

		byte[] concatenated = BytesUtil.concat(src1.getBytes(), src2.getBytes(), src3.getBytes());
		String concatenatedStr = new String(concatenated);

		assertEquals("abcdefghijklmn", concatenatedStr);
	}
	
	@Test
	public void testConcat3() throws Exception {
		
		byte[] array = null;
		byte[] result = BytesUtil.concat(array);
		
		assertEquals(0, result.length);
	}
	
	@Test
	public void testConcat4() throws Exception {
		
		byte[] concatenated = BytesUtil.concat();
		assertEquals(0, concatenated.length);
	}
	
	@Test
	public void testConcat5() throws Exception {

		String src1 = "abcd";
		String src2 = "efgh";

		byte[] concatenated = BytesUtil.concat(src1.getBytes(), null, src2.getBytes());
		String concatenatedStr = new String(concatenated);

		assertEquals("abcdefgh", concatenatedStr);
	}
	
	@Test
	public void testIndexOf1() throws Exception {

		String src1 = "abcd||efghi";
		String src2 = "||";

		int index = BytesUtil.indexOf(src1.getBytes(), src2.getBytes());

		assertEquals(4, index);
	}
	
	@Test
	public void testIndexOf2() throws Exception {

		String src1 = "abcd||efghi";
		String src2 = "||a";

		int index = BytesUtil.indexOf(src1.getBytes(), src2.getBytes());

		assertEquals(-1, index);
	}
	
	@Test
	public void testIndexOf3() throws Exception {

		String src1 = "aaaabaccc";
		String src2 = "ab";

		int index = BytesUtil.indexOf(src1.getBytes(), src2.getBytes());

		assertEquals(3, index);
	}
	
	@Test
	public void testReadAllBytes1() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		try(FileInputStream is = new FileInputStream(testFile)) {
			byte[] read = BytesUtil.readAllBytes(is);
			assertEquals(1597, read.length);
		}

	}
	
	@Test
	public void testReadAllBytes2() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		try(FileInputStream is = new FileInputStream(testFile)) {
			BytesUtil.readAllBytes(is, 0);
			assertTrue(false);
		} catch(Exception ex) {
			assertTrue(true);
		}

	}

}
