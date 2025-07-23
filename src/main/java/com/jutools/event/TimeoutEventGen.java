package com.jutools.event;

import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

/**
 * 
 * 
 * @author jmsohn
 */
public class TimeoutEventGen {
	
	/** */
	private boolean stop;
	
	/** */
	private long timeout;
	
	/** */
	private volatile long lastTouchedTimestamp;
	
	/** */
	private Thread timeoutThread;
	
	/** */
	private List<Consumer<TimeoutEvent>> listenerList;

	/**
	 * 
	 * 
	 * @param timeout
	 */
	public TimeoutEventGen(long timeout) throws Exception {
		
		if(timeout < 1) {
			throw new IllegalArgumentException("timeout value must be greater than 1:" + timeout);
		}
		
		this.timeout = timeout;
		
		this.listenerList = new Vector<>();
	}
	
	/**
	 * 
	 * 
	 * @param listener
	 * @return
	 */
	public TimeoutEventGen add(Consumer<TimeoutEvent> listener) throws Exception {
		
		if(listener == null) {
			throw new IllegalArgumentException("listener is null.");
		}
		
		this.listenerList.add(listener);
		
		return this;
	}
	
	/**
	 * 
	 */
	public void touch() {
		this.lastTouchedTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public TimeoutEventGen start() throws Exception {
		
		if(this.stop == false) {
			throw new IllegalStateException("thread is aleady started.");
		}
		
		this.stop = false;
		
		//
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
						if(cur - lastTouchedTimestamp < timeout) {
							
							waitTime = lastTouchedTimestamp + timeout - cur;
							continue;
						}
						
						// 이벤트 생성
						TimeoutEvent event = new TimeoutEvent();
						event.setTimestamp(cur);
						event.setLastTouchedTimestamp(lastTouchedTimestamp);
						
						// 리스너에게 전달
						for(Consumer<TimeoutEvent> listener: listenerList) {
							listener.accept(event);
						}
						
					} catch(InterruptedException iex) {
						stop = true;
					}
				}
			}
		});
		
		//
		this.timeoutThread.start();
		
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimeoutEventGen stop() throws Exception {
		
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
