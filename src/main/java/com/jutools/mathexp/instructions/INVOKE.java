package com.jutools.mathexp.instructions;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Stack;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author jmsohn
 */
public class INVOKE extends Instruction {
	
	@Setter
	@Getter
	private Method method;

	@Override
	public void execute(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		
		if(this.method == null) {
			throw new NullPointerException("method is null(check link process)");
		}
		
		// 메소드에 넘겨줄 parameter를 만듦
		Object[] params = new Object[Integer.parseInt(this.getParam(1))]; // 파라미터의 개수 만큼 배열 생성
		for(int index = 0; index < params.length; index++) {
			params[index] = stack.pop();
		}
		
		// 메소드 호출 및 결과를 stack에 추가
		Object result = this.method.invoke(null, params);
		stack.push(result);
		
	}

}
