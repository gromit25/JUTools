package com.jutools;

import java.lang.reflect.Method;
import java.nio.Buffer;

/**
 * java.nio.Buffer관련 utility 클래스<br>
 * -> Buffer의 메소드 중 일부가 1.8 버전에 없는 경우가 있어<br>
 *    버전에 상관없이 지원하기 위한 용도로 만듦
 * 
 * @author jmsohn
 */
public class NIOBufferUtil {
	
	// JDK 1.8에서 지원하지 않는 메소드
	// 만일 1.8초과이면 null이 아니고 1.8이면 null로 설정
	/** flip 메소드 */
	private static Method flipMethod;
	/** clear 메소드 */
	private static Method clearMethod;
	
	static {
		
		// Method 초기화 수행
		Class<?> bufferClass = Buffer.class;
		
		// flip method 설정
		try {
			flipMethod = bufferClass.getMethod("flip");
		} catch(Exception ex) {
			flipMethod = null;
		}
		
		// clear method 설정
		try {
			clearMethod = bufferClass.getMethod("clear");
		} catch(Exception ex) {
			clearMethod = null;
		}
	}
	
	/**
	 * java.nio.Buffer의 flip 기능 구현<br>
	 * -> JDK 1.8에서는 flip이 구현되어 있지 않음
	 * 
	 * @param buffer flip을 수행할 buffer
	 * @return buffer 객체 - fluent 코딩용
	 */
	public static Buffer flip(Buffer buffer) throws Exception {
		
		if(buffer == null) {
			throw new NullPointerException("buffer is null");
		}
		
		if(NIOBufferUtil.flipMethod == null) {
			buffer.limit(buffer.position()); // limit = position;
			buffer.position(0);              // position = 0; mark = -1; 주의) mark가 0으로 설정되어 있으면 -1이 되지 않음
		} else {
			flipMethod.invoke(buffer);
		}
		
		return buffer;
	}
	
	/**
	 * java.nio.Buffer의 clear 기능 구현<br>
	 * -> JDK 1.8에서는 clear가 구현되어 있지 않음
	 * 
	 * @param buffer clear를 수행할 buffer
	 * @return buffer 객체 - fluent 코딩용
	 */
	public static Buffer clear(Buffer buffer) throws Exception {
		
		if(buffer == null) {
			throw new NullPointerException("buffer is null");
		}
		
		if(NIOBufferUtil.clearMethod == null) {
		    buffer.position(0);              // position = 0; mark = -1; 주의) mark가 0으로 설정되어 있으면 -1이 되지 않음
		    buffer.limit(buffer.capacity()); // limit = capacity;
		} else {
			clearMethod.invoke(buffer);
		}
		
		return buffer;
	}
}
