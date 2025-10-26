package com.jutools.filetracker;

import java.util.function.Consumer;

/**
 * 끊어 읽기 인터페이스
 * 
 * @author jmsohn
 */
public interface SplitReader<T> {

	/**
	 * 끊어 읽기 수행
	 * 
	 * @param buffer 끊어 읽을 데이터 
	 * @param action 읽은 데이터 처리를 위한 Consumer
	 */
	public void read(byte[] buffer, Consumer<T> action) throws Exception;
}
