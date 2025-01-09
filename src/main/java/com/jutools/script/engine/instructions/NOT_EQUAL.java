package com.jutools.script.engine.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 주어진 두 객체의 동일성 여부를 스택에 추가
 * 
 * @author jmsohn
 */
public class NOT_EQUAL extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		Object p2 = stack.pop();
		Object p1 = stack.pop();
		
		if(p1 == null || p2 == null) {
			stack.push(p1 != p2);
		} else {
			stack.push(!p1.equals(p2));
		}
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
}
