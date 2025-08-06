package com.jutools.event;

import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 특정 시간 동안 변화(touch)가 없으면 이벤트 발생시키는 클래스
 * 
 * @author jmsohn
 */
public class Timer<T extends TimeoutEvent> {
	
	/** 중단 여부 */
	private boolean stop;
	
	/** 대기 기간(ms) */
	private long timeout;
	
	/** 최종 변화(touch)된 시간(ms) */
	private volatile long lastTouched = 0L;
	
	/** 대기 스레드 */
	private Thread timeoutThread;
	
	/** 이벤트 리스너 목록 */
	private List<Consumer<T>> listenerList;

	/**
	 * 생성자
	 * 
	 * @param timeout 대기 시간(ms)
	 */
	public Timer(long timeout) throws Exception {

		// 입력값 검증
		// 최소 1초 이상
		if(timeout < 1000L) {
			throw new IllegalArgumentException("timeout value must be greater than 1000: " + timeout);
		}
		
		this.timeout = timeout;
		
		this.listenerList = new Vector<>();
	}
	
	/**
	 * 이벤트 리스너 추가
	 * 
	 * @param listener 추가할 이벤트 리스너
	 * @return 현재 객체
	 */
	public Timer<T> add(Consumer<T> listener) throws Exception {
		
		if(listener == null) {
			throw new IllegalArgumentException("listener is null.");
		}
		
		this.listenerList.add(listener);
		
		return this;
	}
	
	/**
	 * touch 수행
	 */
	public void touch() {
		this.lastTouched = System.currentTimeMillis();
	}
	
	/**
	 * 이벤트 대기 시작
	 * 
  	 * @param eventSupplier timeout이 발생했을 때 event 객체를 생성하여 반환하는 공급자
	 * @return 현재 객체
	 */
	public Timer<T> run(Supplier<T> eventSupplier) throws Exception {
		
		if(this.stop == false) {
			throw new IllegalStateException("thread is aleady started.");
		}
		
		this.stop = false;
		
		// 대기 스레드 생성
		this.timeoutThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				long waitTime = timeout;
				
				while(stop == false) {
					
					try {
						
						// 다음 타임 아웃까지 대기
						Thread.sleep(waitTime);
						
						// 현재 시간
						long cur = System.currentTimeMillis();
						
						// 타임 아웃 시간이 지나지 않았을 경우 다시 대기
						if(cur - lastTouched < timeout) {
							
							waitTime = lastTouched + timeout - cur;
							continue;
						}

						waitTime = timeout;
						
						// 이벤트 생성
						T event = eventSupplier.get();
						if(event == null) {
							continue;
						}

						// 이덴트 객체 시간 설정
						event.setTimestamp(cur);
						event.setLastTouchedTimestamp(lastTouched);
						
						// 리스너에게 전달
						for(Consumer<T> listener: listenerList) {
							listener.accept(event);
						}
						
					} catch(InterruptedException iex) {
						stop = true;
					}
				}
			}
		});
		
		// 대기 스레드 시작
		this.timeoutThread.start();
		
		return this;
	}
	
	/**
	 * 대기 스레드 중단
	 * 
	 * @return 현재 객체
	 */
	public Timer<T> stop() throws Exception {
		
		if(this.stop == true || this.timeoutThread.isAlive() == false) {
			throw new IllegalStateException("thread is aleady stopped.");
		}
		
		this.stop = true;
		
		if(this.timeoutThread != null && this.timeoutThread.isAlive() == true) {
			this.timeoutThread.interrupt();
		}
		
		return this;
	}
}
