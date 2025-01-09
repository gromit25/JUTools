package com.jutools.script.mathexp;

import lombok.Data;

/**
 * 수식 계산 결과
 * 
 * @author jmsohn
 */
@Data
public class MathResult {
	
	/** 수식 계산 결과 값 */
	private double value;
	/** 단위(없을 경우 "") */
	private String baseUnit;
	
}
