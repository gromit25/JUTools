package com.jutools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.junit.Test;

import lombok.Getter;
import lombok.Setter;

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
	public void testToArray3() {
		try {

			Set<String> testSet = new HashSet<>();
			testSet.add("test 1");
			testSet.add("test 2");
			testSet.add("test 2");
			testSet.add("test 3");
			
			String[] array = TypeUtil.toArray(testSet, String.class);
			
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

	public static class GetterTest {
		@Getter
		private int field1;
		@Getter
		private String field2;
		@Getter
		private boolean field3;
		private String field4;
	}

	@Test
	public void testGetGetter1() {
		try {
			
			Method method = TypeUtil.getGetter(GetterTest.class, "field1");
			assertEquals("getField1", method.getName());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGetGetter2() {
		try {
			
			Method method = TypeUtil.getGetter(GetterTest.class, "field2");
			assertEquals("getField2", method.getName());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGetGetter3() {
		try {
			
			Method method = TypeUtil.getGetter(GetterTest.class, "field3");
			assertEquals("isField3", method.getName());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGetGetter4() {
		try {
			
			TypeUtil.getGetter(GetterTest.class, "field4");
			assertTrue(false);
			
		} catch(Exception ex) {
			assertTrue(true);
		}
	}
	
	public static class SetterTest {
		@Setter
		private int field1;
		@Setter
		private String field2;
		@Setter
		private boolean field3;
		private String field4;
	}

	@Test
	public void testGetSetter1() {
		try {
			
			Method method = TypeUtil.getSetter(SetterTest.class, "field1");
			assertEquals("setField1", method.getName());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGetSetter2() {
		try {
			
			Method method = TypeUtil.getSetter(SetterTest.class, "field2");
			assertEquals("setField2", method.getName());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGetSetter3() {
		try {
			
			Method method = TypeUtil.getSetter(SetterTest.class, "field3");
			assertEquals("setField3", method.getName());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGetSetter4() {
		try {
			
			TypeUtil.getSetter(SetterTest.class, "field4");
			assertTrue(false);
			
		} catch(Exception ex) {
			assertTrue(true);
		}
	}
}
