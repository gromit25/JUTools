package com.jutools;

/**
 * 스레드 처리관련 Utility
 *
 * @author jmsohn
 */
public class ThreadUtil {

	public static void shutdown(ExecutorService execSvc) throws Exception {
    
		if(this.execSvc == null || this.execSvc.isShutdonw() == true) {
			return;
		}
    
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
