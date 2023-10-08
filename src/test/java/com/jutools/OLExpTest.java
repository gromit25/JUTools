package com.jutools;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.jutools.olexp.OLExp;

/**
 * OLExp 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class OLExpTest {
	
	@Test
	public void testCalculate1_1() {
		try {

			HashMap<String, Object> values = new HashMap<String, Object>();
			values.put("test", 10);
			
			Double result = OLExp.compile("test")
								.execute(values)
								.pop(Double.class);
			
			assertEquals(10.0, result, 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testCalculate1_2() {
		try {

			Double result = OLExp.compile("10")
								.execute()
								.pop(Double.class);
			
			assertEquals(10.0, result, 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testString1_1() {
		try {

			String result = OLExp.compile("'a'")
								.execute()
								.pop(String.class);
			
			assertEquals("a", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testString1_2() {
		try {

			String result = OLExp.compile("'a' + 1")
								.execute()
								.pop(String.class);
			
			assertEquals("a1", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testString1_3() {
		try {

			String result = OLExp.compile("1 + 'abc'")
								.execute()
								.pop(String.class);
			
			assertEquals("1abc", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testString1_4() {
		try {

			String result = OLExp.compile("(12 + 1/2) + 'a'")
								.execute()
								.pop(String.class);
			
			assertEquals("12.5a", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testString1_5() {
		try {

			String result = OLExp.compile("'abc' + 'def'")
								.execute()
								.pop(String.class);
			
			assertEquals("abcdef", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testString1_6() {
		try {

			String result = OLExp.compile("'홍' + '길동'")
								.execute()
								.pop(String.class);
			
			assertEquals("홍길동", result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

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

			OLExp.compile("a=10")
				.execute(values);
			
			assertEquals(10.0, (double)(values.get("a")), 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testStore1_2() {
		
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();

			OLExp.compile("a =(3*4/ 2 + -2 * 2.5) * 10")
				.execute(values);
			
			assertEquals(10.0, (double)(values.get("a")), 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testStore1_3() {
		
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();
			values.put("compare", 10);

			OLExp.compile("a = (3*4/ 2 + -2 * 2.5) * 10 == compare")
				.execute(values);
			
			assertEquals(true, (boolean)(values.get("a")));
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMethodCall1_1() {
		
		try {
			
			Double result = OLExp.compile("pow(10, 2)")
								.execute()
								.pop(Double.class);
			
			assertEquals(100.0, result, 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMethodCall1_2() {
		
		try {
			
			Double result = OLExp.compile("(pow(10, 2) + 10)/2")
								.execute()
								.pop(Double.class);
			
			assertEquals(55.0, result, 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMethodCall1_3() {
		
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();
			
			OLExp.compile("a123=(pow(10, 2) + 10)/2 - 5")
				.execute(values);
			
			assertEquals(50.0, (double)(values.get("a123")), 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMethodCall2_1() {
		
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();
			
			boolean result = OLExp.compile("matchW('test?1', 'test11')")
				.execute(values).pop(Boolean.class);
			
			assertTrue(result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMethodCall3_1() {
		
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();
			
			boolean result = OLExp.compile("matchR('test.1', 'test11')")
				.execute(values).pop(Boolean.class);
			
			assertTrue(result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
