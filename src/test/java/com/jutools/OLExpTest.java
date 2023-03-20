package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.jutools.olexp.OLExp;

class OLExpTest {

	@Test
	void testCalculate1_1() {
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

}
