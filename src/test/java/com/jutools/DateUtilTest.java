package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class DateUtilTest {

	@Test
	void testGetDateStr1() {
		try {
			
			String dateStr = DateUtil.getDateStr(new Date());
			System.out.println(dateStr);
			
		} catch(Exception ex) {
			
		}
	}
	
	@Test
	void testGetTimeStr1() {
		try {
			
			String timeStr = DateUtil.getTimeStr(new Date());
			System.out.println(timeStr);
			
		} catch(Exception ex) {
			
		}
	}


}
