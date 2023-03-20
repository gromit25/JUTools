package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OLExpTest {

	@Test
	void testCalculate1_1() {
		try {
			
			double result = OL.calculate("-123.45");
			assertEquals(-123.45, result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

}
