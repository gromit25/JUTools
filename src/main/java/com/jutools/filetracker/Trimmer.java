package com.jutools.filetracker;

import java.util.function.Consumer;

/**
 * 읽은 데이터를 처리(재단)하는 인터페이스
 * 
 * @author jmsohn
 */
public interface Trimmer<T> {
	
	/**
	 * 컨슈머 설정
	 * 
	 * @param consumer 재단된 데이터를 처리하기 위한 Consumer
	 */
	public void setConsumer(Consumer<T> consumer) throws Exception;

	/**
	 * 읽은 데이터 처리(재단)함
	 * 
	 * @param buffer 읽은 데이터 
	 */
	public void trim(byte[] buffer) throws Exception;
}
