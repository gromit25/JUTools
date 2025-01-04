package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 스택의 값에 따라 실행 위치 변경<br>
 * 값이 True 이면, 파라미터 1 만큼 이동,<br>
 * False 이면, 파라미터 2 만큼 이동
 * 
 * @author jmsohn
 */
public class IF_TRUE extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
}
