package com.jutools;


import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import com.jutools.bytesmap.BytesMap;

import lombok.Data;

/**
 * BytesUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class BytesUtilTest {
	
	@Test
	public void testCompare1() throws Exception {
		
		String test1 = "123456789";
		String test2 = "123456789";
		
		int result = BytesUtil.compare(test1.getBytes(), test2.getBytes());

		assertEquals(0, result);
	}
	
	@Test
	public void testCompare2() throws Exception {
		
		String test1 = "123456789";
		String test2 = "123456";
		
		int result = BytesUtil.compare(test1.getBytes(), test2.getBytes());

		assertEquals(1, result);
	}

	@Test
	public void testCompare3() throws Exception {
		
		String test1 = "12345";
		String test2 = "123456789";
		
		int result = BytesUtil.compare(test1.getBytes(), test2.getBytes());

		assertEquals(-1, result);
	}
	
	@Test
	public void testCompare4() throws Exception {
		
		String test1 = "123556789";
		String test2 = "123456789";
		
		int result = BytesUtil.compare(test1.getBytes(), test2.getBytes());

		assertEquals(1, result);
	}
	
	@Test
	public void testCompare5() throws Exception {
		
		String test1 = "123456789";
		String test2 = "123556789";
		
		int result = BytesUtil.compare(test1.getBytes(), test2.getBytes());

		assertEquals(-1, result);
	}
	
	@Test
	public void testStartsWith1() throws Exception {
		
		String target = "123456789";
		String prefix = "123";
		
		boolean result = BytesUtil.startsWith(target.getBytes(), prefix.getBytes());

		assertTrue(result);
	}
	
	@Test
	public void testStartsWith2() throws Exception {
		
		String target = "123456789";
		String prefix = "125";
		
		boolean result = BytesUtil.startsWith(target.getBytes(), prefix.getBytes());

		assertFalse(result);
	}
	
	@Test
	public void testEndsWith1() throws Exception {
		
		String target = "123456789";
		String prefix = "789";
		
		boolean result = BytesUtil.endsWith(target.getBytes(), prefix.getBytes());

		assertTrue(result);
	}
	
	@Test
	public void testEndsWith2() throws Exception {
		
		String target = "123456789";
		String prefix = "779";
		
		boolean result = BytesUtil.endsWith(target.getBytes(), prefix.getBytes());

		assertFalse(result);
	}

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
		byte[] read = BytesUtil.readAllBytes(testFile);
		assertEquals(1597, read.length);

	}
	
	@Test
	public void testReadAllBytes2() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		try {
			BytesUtil.readAllBytes(testFile, 0);
			assertTrue(false);
		} catch(Exception ex) {
			assertTrue(true);
		}

	}
	
	@Test
	public void testReadNBytes1() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		byte[] read = BytesUtil.readNBytes(testFile, 10);
		assertTrue("별 헤는".equals(new String(read)));
	}
	
	@Test
	public void testReadNBytes2() throws Exception {
		
		File testFile = new File("resources/read_test.txt");
		
		byte[] read = BytesUtil.readNBytes(testFile, 20, 10);
		assertTrue("별 헤는 밤 - 윤".equals(new String(read)));
	}
	
	@Test
	public void testMapping1() throws Exception {
		
		byte[] attr1 = TypeUtil.floatToBytes((float)10.1);
		byte[] attr2 = TypeUtil.longToBytes(123);
		byte[] attr3 = TypeUtil.intToBytes(555);
		
		byte[] msg = BytesUtil.concat(attr1, attr3, attr2);
		
		BytesMappingTest1 map = BytesUtil.mapping(msg, BytesMappingTest1.class);
		
		assertEquals(10.1, map.getAttr1(), 0.1);
		assertEquals(123, (long)map.getAttr2());
		assertEquals(555, map.getAttr3());
	}
	
	@Data
	public static class BytesMappingTest1 {
		@BytesMap(order=1)
		public float attr1;
		@BytesMap(order=3)
		private Long attr2;
		@BytesMap(order=2)
		protected int attr3;
	}
	
	@Test
	public void testMapping2() throws Exception {
		
		byte[] attr1 = "00000010.1".getBytes();
		byte[] attr2 = "00123".getBytes();
		byte[] attr3 = "00555".getBytes();
		
		byte[] msg = BytesUtil.concat(attr1, attr2, attr3);
		
		BytesMappingTest2 map = BytesUtil.mapping(msg, BytesMappingTest2.class);
		
		assertEquals(10.1, map.getAttr1(), 0.1);
		assertEquals(123, (long)map.getAttr2());
		assertEquals(555, map.getAttr3());
	}
	
	@Data
	public static class BytesMappingTest2 {
		@BytesMap(order=1, size=10)
		public float attr1;
		@BytesMap(order=2, size=5)
		private Long attr2;
		@BytesMap(order=3, size=5)
		protected int attr3;
	}
	
	@Test
	public void testMapping3() throws Exception {
		
		byte[] msg = "APRV0112345678123456780000001000bracelet            ".getBytes();
		BytesMappingTest3 map = BytesUtil.mapping(msg, BytesMappingTest3.class);
		
		assertEquals("1234567812345678", map.getCardNo());
		assertEquals(1000, map.getAmt());
		assertEquals("bracelet", map.getProductName());
	}
	
	@Data
	public static class BytesMappingTest3 {
		
		@BytesMap(order=1, size=16, skip=6)
		private String cardNo;
		@BytesMap(order=2, size=10)
		protected int amt;
		@BytesMap(order=3, size=20, method="com.jutools.BytesUtilTest$BytesMappingTest3.processProductName")
		public String productName;
		
		public static String processProductName(byte[] bytes) {
			return new String(bytes).trim();
		}
	}

}


