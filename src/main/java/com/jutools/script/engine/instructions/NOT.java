package com.jutools.script.engine.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * Not 연산 결과 스택에 추가
 * 
 * @author jmsohn
 */
public class NOT extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		Object p1 = stack.pop();
		
		if(p1 instanceof Boolean == false) {
			throw new Exception("Unexpected type: " + p1.getClass());
		}
		
		stack.push(!((Boolean)p1));
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
}
