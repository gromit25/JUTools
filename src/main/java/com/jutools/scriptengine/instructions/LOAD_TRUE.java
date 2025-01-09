package com.jutools.scriptengine.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 스택에 True 값 추가
 * 
 * @author jmsohn
 */
public class LOAD_TRUE extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 스택에 True 값 추가
		stack.push(Boolean.TRUE);

		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
}
