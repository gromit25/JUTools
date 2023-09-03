package com.jutools;

import java.util.Date;

import org.junit.Test;

/**
 * DateUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class DateUtilTest {

	@Test
	public void testGetDateStr1() {
		try {
			
			String dateStr = DateUtil.getDateStr(new Date());
			System.out.println(dateStr);
			
		} catch(Exception ex) {
			
		}
	}
	
	@Test
	public void testGetTimeStr1() {
		try {
			
			String timeStr = DateUtil.getTimeStr(new Date());
			System.out.println(timeStr);
			
		} catch(Exception ex) {
			
		}
	}


}
