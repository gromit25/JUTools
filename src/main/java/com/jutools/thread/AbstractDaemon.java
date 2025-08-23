package com.jutools.thread;

/**
 * 데몬 프로그램을 위한 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractDaemon {
	
	/** 데몬 스레드 */
	private Thread daemonThread;

	/**
	 * 실제 업무 로직
	 * 
	 * @throws InterruptedException 인터럽트 호출시 발생되는 예외
	 */
	protected abstract void process() throws InterruptedException;
	
	/**
	 * 데몬 스레드 생성 및 실행
	 */
	public void run() {
		
		// 데몬 스레드 생성
		this.daemonThread = new Thread(
			new Runnable() {
				public void run() {
					
					while(daemonThread.isInterrupted() == false) {
						
						try {
							process();
						} catch(InterruptedException iex) {
							daemonThread.interrupt();
						}
					}
				}
			}
		);
		
		// 데몬 스레드 실행
		this.daemonThread.start();
	}

	/**
	 * 데몬 스레드 중단
	 */
	public void stop() {
		
		if(this.daemonThread == null) {
			return;
		}
		
		this.daemonThread.interrupt();
	}
	
	/**
	 * 데몬이 동작 중인지 여부 반환
	 * 
	 * @return 데몬 동작 여부
	 */
	public boolean isAlive() {
		return this.daemonThread != null && this.daemonThread.isAlive() == true;
	}
}
