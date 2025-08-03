package com.jutools;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jutools.script.olexp.OLExp;
import com.jutools.script.parser.exception.ParseException;
import com.jutools.script.parser.exception.UnexpectedEndException;

import lombok.Data;

/**
 * OLExp 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class OLExpTest {
	
	/**
	 * 테스트용 클래스
	 */
	@Data
	public static class TestVO {
		
		private List<String> receivers;
		private String subject;
		private String body;
		private AttachVO file;
		private Map<String, String> map;
		
		public TestVO() {
			
			this.subject = "subject 입니다.";
			this.body = "내용 입니다.";
			this.file = new AttachVO("test.txt");
			
			this.receivers = new ArrayList<>();
			this.receivers.add("abc@abc.com");
			this.receivers.add("def@def.org");
			this.receivers.add("hij@hij.co.kr");
			this.receivers.add("lmn@lmn.com");
			
			this.map = new HashMap<>();
			this.map.put("test1", "test1 입니다.");
			this.map.put("test2", "test2 입니다.");
			this.map.put("test3", "test3 입니다.");
		}
	}
	
	@Data
	public static class AttachVO {
		
		private String filename;
		
		public AttachVO(String filename) {
			this.filename = filename;
		}
	}
	
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
	public void testCalculate1_3() {
		try {

			Double result = OLExp.compile("10 + 3 - 1")
								.execute()
								.pop(Double.class);
			
			assertEquals(12.0, result, 0.1);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testCalculate1_4() {
		try {

			Double result = OLExp.compile("10 + (3 - 1.2)+2*2")
								.execute()
								.pop(Double.class);
			
			assertEquals(15.8, result, 0.01);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testCalculate1_5() {
		try {

			Double result = OLExp.compile("2*4*3 / (2*2) + 1")
								.execute()
								.pop(Double.class);
			
			assertEquals(7.0, result, 0.01);
			
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
	public void testBoolean1_1() {
		try {

			HashMap<String, Object> values = new HashMap<String, Object>();
			
			Boolean result = OLExp.compile("1 == 1 and 1 != 1")
					.execute(values).pop(Boolean.class);
			
			assertFalse(result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("an exception is occured");
		}
	}
	
	@Test
	public void testBoolean1_2() {
		try {

			HashMap<String, Object> values = new HashMap<String, Object>();
			
			Boolean result = OLExp.compile("1 == 1 and 2 == 2")
					.execute(values).pop(Boolean.class);
			
			assertTrue(result);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("an exception is occured");
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
			fail("exception is occured");
		}
	}
	
	@Test
	public void testMethodCall4_1() {
		
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();
			
			String result = OLExp.compile("now('yyyy-MM-dd')")
				.execute(values).pop(String.class);
			
			String compare = LocalDateTime.now().format(
				DateTimeFormatter.ofPattern("yyyy-MM-dd")
			);
			
			assertEquals(compare, result);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}
	
	@Test
	public void testUngrammar1() {
		try {
			OLExp.compile("* 12");
		} catch(ParseException pex) {
			assertEquals(1, pex.getPos());
		} catch(Exception ex) {
			fail();
		}
	}
	
	@Test
	public void testUngrammar2() {
		try {
			OLExp.compile("2 * 10 + +");
		} catch(ParseException pex) {
			assertEquals(10, pex.getPos());
		} catch(Exception ex) {
			fail();
		}
	}
	
	@Test
	public void testUngrammar3() {
		try {
			// TODO 위치 확인 필요
			OLExp.compile("2 * (10 +) +");
		} catch(ParseException pex) {
			assertEquals(10, pex.getPos());
		} catch(Exception ex) {
			fail();
		}
	}
	
	@Test
	public void testUngrammar4() {
		try {
			OLExp.compile("2 * (10 + var1) *");
			fail();
		} catch(UnexpectedEndException uex) {
			assertEquals(17, uex.getPos());
		} catch(Exception ex) {
			fail();
		}
	}
	
	@Test
	public void testUngrammar5() {
		try {
			OLExp.compile("2 * (10 + 1var1) *");
			fail();
		} catch(ParseException pex) {
			assertEquals(12, pex.getPos());
		} catch(Exception ex) {
			fail();
		}
	}
	
	@Test
	public void testUngrammar6() {
		
		try {
			OLExp.compile("()");
			fail();
		} catch(ParseException pex) {
			assertEquals(2, pex.getPos());
		} catch(Exception ex) {
			fail();
		}
	}
	
	@Test
	public void testAttr1() throws Exception {
		
		// 테스트용 데이터 생성
		TestVO vo = new TestVO();
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("message", vo);
		
		// 테스트
		String result = OLExp
				.compile("'subject: ' + message.subject")
				.execute(values)
				.pop(String.class);
		
		// 결과 확인
		assertEquals("subject: subject 입니다.", result);
	}
	
	@Test
	public void testAttr2() throws Exception {
		
		// 테스트용 데이터 생성
		TestVO vo = new TestVO();
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("message", vo);
		
		// 테스트
		String result = OLExp
				.compile("'file: ' + message.file.filename")
				.execute(values)
				.pop(String.class);
		
		// 결과 확인
		assertEquals("file: test.txt", result);
	}
	
	@Test
	public void testElement1() throws Exception {
		
		// 테스트용 데이터 생성
		List<String> receivers = new ArrayList<>();
		receivers.add("abc@abc.com");
		receivers.add("def@def.org");
		receivers.add("hij@hij.co.kr");
		receivers.add("lmn@lmn.com");
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("receivers", receivers);
		values.put("index", 0);
		
		// 테스트
		String result = OLExp
				.compile("'receiver(' + index + '): ' + receivers[index]")
				.execute(values)
				.pop(String.class);
		
		// 결과 확인
		assertEquals("receiver(0): abc@abc.com", result);
	}
	
	@Test
	public void testElement2() throws Exception {
		
		// 테스트용 데이터 생성
		TestVO vo = new TestVO();
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("message", vo);
		values.put("index", 0);
		
		// 테스트
		String result1 = OLExp
				.compile("'receiver(' + index + '): ' + message.receivers[index]")
				.execute(values)
				.pop(String.class);
		
		String result2 = OLExp
				.compile("'receiver(' + (index + 2) + '): ' + message.receivers[index + 2]")
				.execute(values)
				.pop(String.class);
		
		String result3 = OLExp
				.compile("'map(test1):' + message.map['test1']")
				.execute(values)
				.pop(String.class);
		
		// 결과 확인
		assertEquals("receiver(0): abc@abc.com", result1);
		assertEquals("receiver(2): hij@hij.co.kr", result2);
		assertEquals("map(test1):test1 입니다.", result3);
	}
	
	@Test
	public void testElement3() throws Exception {
		
		// 테스트용 데이터 생성
		TestVO vo = new TestVO();
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("message", vo);
		values.put("index", 0);

		// 테스트
		String result = OLExp
				.compile("if(message.receivers[index] == null, '', message.receivers[index])")
				.execute(values)
				.pop(String.class);
		
		// 결과 확인
		assertEquals("abc@abc.com", result);
	}
	
	@Test
	public void testLoadTrue1() throws Exception {
		
		// 테스트
		Boolean result = OLExp
				.compile("true")
				.execute()
				.pop(Boolean.class);
		
		assertTrue(result);
	}
	
	@Test
	public void testLoadTrue2() throws Exception {
		
		// 데이터 설정
		Map<String, Object> values = new HashMap<>();
		values.put("value", true);
		
		// 테스트
		Boolean result = OLExp
				.compile("value == true")
				.execute(values)
				.pop(Boolean.class);
		
		assertTrue(result);
	}
	
	@Test
	public void testLoadTrue3() throws Exception {
		
		// 데이터 설정
		Map<String, Object> values = new HashMap<>();
		values.put("value", true);
		
		// 테스트
		Boolean result = OLExp
				.compile("value != true")
				.execute(values)
				.pop(Boolean.class);
		
		assertFalse(result);
	}
	
	@Test
	public void testLoadFalse1() throws Exception {
		
		// 테스트
		Boolean result = OLExp
				.compile("false")
				.execute()
				.pop(Boolean.class);
		
		assertFalse(result);
	}
	
	@Test
	public void testLoadFalse2() throws Exception {
		
		// 데이터 설정
		Map<String, Object> values = new HashMap<>();
		values.put("value", false);
		
		// 테스트
		Boolean result = OLExp
				.compile("value == false")
				.execute(values)
				.pop(Boolean.class);
		
		assertTrue(result);
	}
	
	@Test
	public void testLoadFalse3() throws Exception {
		
		// 데이터 설정
		Map<String, Object> values = new HashMap<>();
		values.put("value", false);
		
		// 테스트
		Boolean result = OLExp
				.compile("value != false")
				.execute(values)
				.pop(Boolean.class);
		
		assertFalse(result);
	}
	
	@Test
	public void testLoadNull1() throws Exception {
		
		// 테스트
		Object result = OLExp
				.compile("null")
				.execute()
				.pop(Object.class);
		
		assertNull(result);
	}
	
	@Test
	public void testLoadNull2() throws Exception {
		
		// 데이터 설정
		Map<String, Object> values = new HashMap<>();
		values.put("value", null);
		
		// 테스트
		Boolean result = OLExp
				.compile("value == null")
				.execute(values)
				.pop(Boolean.class);
		
		assertTrue(result);
	}
	
	@Test
	public void testLoadNull3() throws Exception {
		
		// 데이터 설정
		Map<String, Object> values = new HashMap<>();
		values.put("value", null);
		
		// 테스트
		Boolean result = OLExp
				.compile("value != null")
				.execute(values)
				.pop(Boolean.class);
		
		assertFalse(result);
	}
	
	@Test
	public void testShortCircuit1() throws Exception {
		
		OLExp exp = OLExp.compile("true or false");
		System.out.println(exp.toString());
		
		Boolean result = exp.execute().pop(Boolean.class);
		System.out.println("RESULT:" + result);
	}
	
	@Test
	public void testShortCircuit2() throws Exception {
		
		OLExp exp = OLExp.compile("false and true");
		System.out.println(exp.toString());
		
		Boolean result = exp.execute().pop(Boolean.class);
		System.out.println("RESULT:" + result);
	}
	
	@Test
	public void testShortCircuit3() throws Exception {
		
		OLExp exp = OLExp.compile("2 < 3 + 1 or 1 != 1");
		System.out.println(exp.toString());
		
		Boolean result = exp.execute().pop(Boolean.class);
		System.out.println("RESULT:" + result);
	}
	
	@Test
	public void testShortCircuit4() throws Exception {
		
		OLExp exp = OLExp.compile("false and 1 == 1 or true");
		System.out.println(exp.toString());
		
		Boolean result = exp.executeForDebug().pop(Boolean.class);
		System.out.println("RESULT:" + result);
	}
	
	@Test
	public void testList1() throws Exception {
		
		OLExp exp = OLExp.compile("[]");
		System.out.println(exp);
		
		@SuppressWarnings("unchecked")
		List<Object> list = exp.execute().pop(List.class);
		
		System.out.println("RESULT:" + list.size());
	}
	
	@Test
	public void testList2() throws Exception {
		
		OLExp exp = OLExp.compile("['test 입니다.', 10, a+10]");
		System.out.println(exp);
		
		Map<String, Object> values = new HashMap<>();
		values.put("a", 1);
		
		@SuppressWarnings("unchecked")
		List<Object> list = exp.execute(values).pop(List.class);
		
		System.out.println("RESULT:" + list.size());
		System.out.println(list);
	}
}
