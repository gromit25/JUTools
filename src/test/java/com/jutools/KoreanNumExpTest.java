package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * KoreanNumExp 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class KoreanNumExpTest {

	@Test
	public void testToKorean1() throws Exception {
		String msg = KoreanNumExp.toKorean(12);
		assertEquals("십이", msg);
	}

	@Test
	public void testToKorean2() throws Exception {
		String msg = KoreanNumExp.toKorean(321);
		assertEquals("삼백이십일", msg);
	}
	
	@Test
	public void testToKorean3() throws Exception {
		String msg = KoreanNumExp.toKorean(1234);
		assertEquals("천이백삼십사", msg);
	}
	
	@Test
	public void testToKorean4() throws Exception {
		String msg = KoreanNumExp.toKorean(56789);
		assertEquals("오만육천칠백팔십구", msg);
	}

	@Test
	public void testToKorean5() throws Exception {
		String msg = KoreanNumExp.toKorean(500000);
		assertEquals("오십만", msg);
	}
	
	@Test
	public void testToKorean6() throws Exception {
		String msg = KoreanNumExp.toKorean(111111);
		assertEquals("십일만천백십일", msg);
	}
	
	@Test
	public void testToKorean7() throws Exception {
		String msg = KoreanNumExp.toKorean(1500010, " ");
		assertEquals("백오십만 십", msg);
	}
	
	@Test
	public void testToKorean8() throws Exception {
		String msg = KoreanNumExp.toKorean(54775807);
		assertEquals("오천사백칠십칠만오천팔백칠", msg);
	}

	@Test
	public void testToKorean9() throws Exception {
		String msg = KoreanNumExp.toKorean(9223372036854775807L, " ");
		assertEquals("구백이십이경 삼천삼백칠십이조 삼백육십팔억 오천사백칠십칠만 오천팔백칠", msg);
	}
	
	@Test
	public void testToKorean10() throws Exception {
		String msg = KoreanNumExp.toKorean(-9223372036854775807L);
		assertEquals("마이너스 구백이십이경삼천삼백칠십이조삼백육십팔억오천사백칠십칠만오천팔백칠", msg);
	}

	@Test
	public void testToKorean11() throws Exception {
		String msg = KoreanNumExp.toKorean(-1);
		assertEquals("마이너스 일", msg);
	}
	
	@Test
	public void testToKorean12() throws Exception {
		String msg = KoreanNumExp.toKorean(0);
		assertEquals("영", msg);
	}
	
	@Test
	public void testToKorean13() throws Exception {
		String msg = KoreanNumExp.toKorean("123");
		assertEquals("백이십삼", msg);
	}
	
	@Test
	public void testToKorean14() throws Exception {
		String msg = KoreanNumExp.toKorean("123.034");
		assertEquals("백이십삼점영삼사", msg);
	}
	
	@Test
	public void testToKorean15() throws Exception {
		String msg = KoreanNumExp.toKorean("00123.034");
		assertEquals("백이십삼점영삼사", msg);
	}
	
	@Test
	public void testToKorean16() throws Exception {
		String msg = KoreanNumExp.toKorean("0.034");
		assertEquals("영점영삼사", msg);
	}
	
	@Test
	public void testToKorean17() throws Exception {
		String msg = KoreanNumExp.toKorean("-0.034");
		assertEquals("마이너스 영점영삼사", msg);
	}
	
	@Test
	public void testToKorean18() throws Exception {
		String msg = KoreanNumExp.toKorean("-100.034");
		assertEquals("마이너스 백점영삼사", msg);
	}
	
	@Test
	public void testToLong1_1() throws Exception {
		long result = KoreanNumExp.toLong("십이");
		assertEquals(12, result);
	}
	
	@Test
	public void testToLong1_2() throws Exception {
		long result = KoreanNumExp.toLong("삼백이십일");
		assertEquals(321, result);
	}
	
	@Test
	public void testToLong1_3() throws Exception {
		long result = KoreanNumExp.toLong("천이백삼십사");
		assertEquals(1234, result);
	}
	
	@Test
	public void testToLong1_4() throws Exception {
		long result = KoreanNumExp.toLong("오만육천칠백팔십구");
		assertEquals(56789, result);
	}
	
	@Test
	public void testToLong1_5() throws Exception {
		long result = KoreanNumExp.toLong("오십만");
		assertEquals(500000, result);
	}
	
	@Test
	public void testToLong1_6() throws Exception {
		long result = KoreanNumExp.toLong("십일만천백십일");
		assertEquals(111111, result);
	}
	
	@Test
	public void testToLong1_7() throws Exception {
		long result = KoreanNumExp.toLong("백오십만 십", " ");
		assertEquals(1500010, result);
	}
	
	@Test
	public void testToLong1_8() throws Exception {
		long result = KoreanNumExp.toLong("오천사백칠십칠만오천팔백칠");
		assertEquals(54775807, result);
	}
	
	@Test
	public void testToLong1_9() throws Exception {
		long result = KoreanNumExp.toLong("구백이십이경 삼천삼백칠십이조 삼백육십팔억 오천사백칠십칠만 오천팔백칠", " ");
		assertEquals(9223372036854775807L, result);
	}
	
	@Test
	public void testToLong1_10() throws Exception {
		long result = KoreanNumExp.toLong("마이너스 구백이십이경삼천삼백칠십이조삼백육십팔억오천사백칠십칠만오천팔백칠");
		assertEquals(-9223372036854775807L, result);
	}
	
	@Test
	public void testToLong1_11() throws Exception {
		long result = KoreanNumExp.toLong("마이너스 일");
		assertEquals(-1, result);
	}
	
	@Test
	public void testToLong1_12() throws Exception {
		long result = KoreanNumExp.toLong("영");
		assertEquals(0, result);
	}
	
	@Test
	public void testToLong2_1() throws Exception {
		try {
			KoreanNumExp.toLong("테스트", "");
		} catch(Exception ex) {
			
		}
	}
	
	@Test
	public void testToLong2_2() throws Exception {
		try {
			KoreanNumExp.toLong("십이테스트", "");
		} catch(Exception ex) {
			
		}
	}
}
