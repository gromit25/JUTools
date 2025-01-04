package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 스택에 숫자(double 형) 추가
 * 
 * @author jmsohn
 */
public class LOAD_NUMBER extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 스택에 숫자 추가
		double value = Double.parseDouble(this.getParam(0));
		stack.push(value);
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}

}
