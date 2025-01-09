package com.jutools.script.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 스택의 가장 위쪽 값을 하나 복사하여 추가함<br>
 * 
 * @author jmsohn
 */
public class DUP extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 가장 위쪽 값 획득
		Object value = stack.pop();
		
		// 가장 위쪽 값을 복사하여 넣는 것이기 때문에 획득한 값을 두번 추가
		stack.push(value);
		stack.push(value);
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}

}
