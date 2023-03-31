package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TypeUtilTest {

	@Test
	void testToInteger1() {
		try {

			int result = TypeUtil.toInteger(1);
			
			assertEquals(1, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	void testToInteger2() {
		try {

			int result = TypeUtil.toInteger("1");
			
			assertEquals(1, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

}
