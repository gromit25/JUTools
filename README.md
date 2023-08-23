# JUTools(Java Utility Tools)
-----------------------------------
Java 관련 Utility 기능 모음
주의) lombok:1.18.26 을 사용하고 있음. 개발툴에 설정 필요

### BytesUtil   
----------------------------------    
> TCP/UDP 통신, File read/write 등에서 byte 단위로 다루기 위한 Utility   

### CronJob   
----------------------------------    
> 일정시간 간격으로 Thread를 실행시키는 Utility    
> 리눅스 크론잡과 동일한 형식으로 설정 가능
```java
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
```

