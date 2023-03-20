package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.jutools.olexp.OLExp;

class OLExpTest {

	@Test
	void testComparison1_1() {
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
	void testComparison1_2() {
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
	void testComparison1_3() {
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
	void testComparison1_4() {
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
	void testComparison2_1() {
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
	void testComparison2_2() {
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
	void testComparison2_3() {
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
	void testComparison2_4() {
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
	void testEquality1_1() {
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
	void testEquality1_2() {
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
	void testEquality2_1() {
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

}
