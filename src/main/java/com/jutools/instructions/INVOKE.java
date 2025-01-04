package com.jutools.instructions;

import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.Stack;

import lombok.Getter;
import lombok.Setter;

/**
 * 메소드 호출 명령어 클래스
 * 
 * @author jmsohn
 */
public class INVOKE extends Instruction {
	
	/** 호출할 메소드 핸들 */
	@Setter
	@Getter
	private MethodHandle method;
	
	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 메소드 핸들이 있는지 확인
		if(this.method == null) {
			throw new NullPointerException("method is null(check link process)");
		}
		
		// 메소드에 넘겨줄 parameter를 만듦
		Object[] params = new Object[Integer.parseInt(this.getParam(1))]; // 파라미터의 개수 만큼 배열 생성
		for(int index = params.length - 1 ; index >= 0; index--) {
			params[index] = stack.pop();
		}

		// 메소드 호출 및 결과를 stack에 추가
		try {
			Object result = this.method.invokeWithArguments(params);
			stack.push(result);
		} catch (Throwable t) {
			throw (Exception)t;
		}
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
}
