package com.jutools;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CronJobTest {

	@Test
	void test() throws Exception {
		
		CronJob job = CronJob.builder()
			.cronExp("* * * * *")
			.job(new Runnable() {
				@Override
				public void run() {
					try {
						long cur = System.currentTimeMillis();
						System.out.println("CRON:" + DateUtil.getTimeStr(cur));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			})
			.build();
		
		job.run();
		
		Thread.sleep(70 * 1000);	// 1분 10초 뒤 중단
		job.stop();
		
		assertTrue(true);
	}

}
