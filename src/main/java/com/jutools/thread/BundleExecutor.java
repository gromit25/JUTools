package com.jutools.thread;

import java.util.List;

/**
 * List형 데이터를 분할하여 스레드로 처리하는 클래스
 * 
 * @author jmsohn
 */
public class BundleExecutor {
	
	/** 분할 처리할 스레드 목록 */
	private Thread[] workers;
	
	/** 분할된 List 처리 객체 */
	private BundleTask task;
	
	/** 스레드의 daemon 설정 여부 */
	private boolean isDaemon = true;
	
	/**
	 * 생성자
	 * 
	 * @param workerCnt 스레드의 수
	 * @param task 분할된 List 처리 객체
	 */
	public BundleExecutor(int workerCnt, BundleTask task) throws Exception {
		
		// 입력값 검증
		if(workerCnt < 1) {
			throw new IllegalArgumentException("worker count must be greater than 1:" + workerCnt);
		}

		if(task == null) {
			throw new NullPointerException("task obj is null.");
		}
		
		// 스레드 개수 만큼 스레드 생성
		this.workers = new Thread[workerCnt];

		// task 설정
		this.task = task;
		
	}

	/**
	 * 주어진 목록을 분할된 스레드로 실행
	 * 
	 * @param list 분할 실행할 목록
	 */
	public BundleExecutor run(List<?> list) throws Exception {
		
		// 목록이 null 이거나 데이터가 없는 경우 즉시 반환
		if(list == null || list.size() == 0) {
			return this;
		}
		
		// 묶음의 크기 계산
		int bundleSize = (int)Math.ceil((double)list.size()/(double)this.workers.length);
		if(bundleSize == 0) {
			bundleSize = 1;
		}
		
		// Thread 생성
		for(int index = 0; index < this.workers.length; index++) {
			
			Integer bundleId = index;
			Integer start = index * bundleSize;
			Integer end = (index + 1 == this.workers.length)?list.size():start + bundleSize; 
			
			// 나누어진 묶음별로 Thread 생성
			this.workers[index] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					task.consume(list, bundleId, start, end);
				}
			});
			
			this.workers[index].setDaemon(this.isDaemon);
		}
		
		// Thread 실행
		for(Thread worker: this.workers) {
			worker.start();
		}
		
		return this;
		
	} // end of run
	
	/**
	 * 모든 분할 스레드가 완료될 때까지 대기 
	 * 
	 * @return 현재 객체
	 */
	public BundleExecutor join() throws Exception {
		
		if(this.workers == null) {
			throw new Exception("workers is null.");
		}
		
		for(Thread worker: workers) {
			worker.join();
		}
		
		return this;
	}
	
	/**
	 * 분할된 스레드에 Daemon 여부 설정
	 * 
	 * @param on Daemon 여부
	 * @return 현재 객체
	 */
	public BundleExecutor setDaemon(boolean on) throws Exception {
		
		if(this.workers == null) {
			throw new Exception("workers is null.");
		}
		
		this.isDaemon = on;
		
		return this;
	}
}
