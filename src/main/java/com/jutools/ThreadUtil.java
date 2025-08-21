package com.jutools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 스레드 처리관련 Utility
 *
 * @author jmsohn
 */
public class ThreadUtil {

	/**
	 * ExecutorService 객체의 shutdown 수행
	 *
	 * @param execSvc shutdown 할 ExecutorService 객체
	 */
	public static void shutdown(ExecutorService execSvc) throws Exception {

		// 입력값 검증
		if(execSvc == null || execSvc.isShutdown() == true) {
			return;
		}

		// 셧다운 수행
		execSvc.shutdown();
		
		try {
      
			// 모든 태스크가 완료될 때까지 지정된 시간(60초) 대기
			if (execSvc.awaitTermination(60, TimeUnit.SECONDS) == false) {

				// 시간 초과 시 즉시 종료 시도
				execSvc.shutdownNow(); 

				// 즉시 종료를 기다림
				execSvc.awaitTermination(60, TimeUnit.SECONDS);
			}
      
		} catch (InterruptedException ie) {
      
			// awaitTermination 중 인터럽트 발생 시 즉시 종료
			execSvc.shutdownNow();
      
			// 현재 스레드의 인터럽트 상태를 유지
			Thread.currentThread().interrupt();
		}
	}
}
