package com.jutools.mathexp;

import lombok.Data;

/**
 * 수식 계산 결과
 * 
 * @author jmsohn
 */
@Data
public class MathResult {
	
	/** 수식 계산 결과 값 */
	private Object value;
	/** 단위(없을 경우 "") */
	private String baseUnit;
	
	/**
	 * 설정된 타입(returnType)에 따라 수식 계산 결과 반환
	 * 
	 * @param returnType 반환할 타입
	 * @return 결과 값
	 */
	public <T> T getValue(Class<T> returnType) {
		return returnType.cast(this.value);
	}
	
}
