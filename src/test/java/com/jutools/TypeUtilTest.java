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

}
