package com.jutools;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.jutools.olexp.OLExp;

public class OLExpTest {

	@Test
	public void testComparison1_1() {
		try {

			Boolean result = OLExp.compile("1 > 2")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testComparison1_2() {
		try {

			Boolean result = OLExp.compile("2 >= 2")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testComparison1_3() {
		try {

			Boolean result = OLExp.compile("2 <3")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testComparison1_4() {
		try {

			Boolean result = OLExp.compile("3<=3")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testComparison2_1() {
		try {

			Boolean result = OLExp.compile("1 + 3 > 2")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testComparison2_2() {
		try {

			Boolean result = OLExp.compile("1 >= 2 - 3")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testComparison2_3() {
		try {

			Boolean result = OLExp.compile("1 + 2 <= 2*3 - 3")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testComparison2_4() {
		try {

			Boolean result = OLExp.compile("6 + 2 > 2*(3 - 1)")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testEquality1_1() {
		try {

			Boolean result = OLExp.compile("1 == 3")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testEquality1_2() {
		try {

			Boolean result = OLExp.compile("1 != 3")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(true, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testEquality2_1() {
		try {

			Boolean result = OLExp.compile("1 + 2 != 3")
								.execute()
								.pop(Boolean.class);
			
			assertEquals(false, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testStore1_1() {
		
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();

			OLExp.compile("a = 10")
				.execute(values);
			
			assertEquals(10.0, (double)(values.get("a")), 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

}
