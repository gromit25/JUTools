package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jutools.stat.StatisticService;

public class StatisticServiceTest {

	@Test
	public void test() throws Exception {
		
		StatisticService svc = new StatisticService(
		"* * * * * *",		// 매초 데이터 수집
		"*/10 * * * * *",	// 매 10초마다 리셋
		() -> {
			
			double value = StatUtil.genNormalDistribution(10, 2);
			System.out.println("DATA: " + value);
			
			return value;
		},
		null);
		
		svc.start();
		Thread.sleep(20 * 1000);
		svc.stop();
	}
}
