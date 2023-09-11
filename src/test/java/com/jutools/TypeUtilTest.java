package com.jutools;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Vector;

import org.junit.Test;

/**
 * TypeUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class TypeUtilTest {

	@Test
	public void testToInteger1() {
		try {

			int result = TypeUtil.toInteger(1);
			
			assertEquals(1, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testToInteger2() {
		try {

			int result = TypeUtil.toInteger("1");
			
			assertEquals(1, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testToArray1() {
		try {

			ArrayList<String> testArray = new ArrayList<>();
			testArray.add("test 1");
			testArray.add("test 2");
			testArray.add("test 3");
			
			String[] array = TypeUtil.toArray(testArray, String.class);
			
			assertEquals(3, array.length);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testToArray2() {
		try {

			Vector<String> testArray = new Vector<>();
			testArray.add("test 1");
			testArray.add("test 2");
			testArray.add("test 3");
			
			String[] array = TypeUtil.toArray(testArray, String.class);
			
			assertEquals(3, array.length);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testIsPrimitive1() {
		assertTrue(TypeUtil.isPrimitive(int.class));
	}

	@Test
	public void testIsPrimitive2() {
		assertTrue(TypeUtil.isPrimitive(Integer.class));
	}
	
	@Test
	public void testIsPrimitive3() {
		assertFalse(TypeUtil.isPrimitive(String.class));
	}
	
	@Test
	public void testIsPrimitive4() {
		assertFalse(TypeUtil.isPrimitive(null));
	}
	
	@Test
	public void testGetPrimitiveSize1() {
		try {
			assertEquals(1, TypeUtil.getPrimitiveSize(byte.class));
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	public void testGetPrimitiveSize2() {
		try {
			assertEquals(4, TypeUtil.getPrimitiveSize(Integer.class));
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGetPrimitiveSize3() {
		try {
			TypeUtil.getPrimitiveSize(String.class);
		} catch(Exception ex) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetPrimitiveSize4() {
		try {
			TypeUtil.getPrimitiveSize(null);
		} catch(Exception ex) {
			assertTrue(true);
		}
	}

}
