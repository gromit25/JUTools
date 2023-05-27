package com.jutools;

import org.junit.jupiter.api.Test;

class CronJobTest {

	@Test
	void test() throws Exception {
		
		CronJob.builder()
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
			.build()
			.run();
	}

}
