package com.jutools.scriptengine.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * No Operation
 * 
 * @author jmsohn
 */
public class NOP extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// Do nothing
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
}
