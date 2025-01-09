package com.jutools.scriptengine;

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
	
	/** 메소드 별칭 */
	@Getter
	@Setter
	private String methodAlias;
	
	/** 메소드 파라미터 개수 */
	@Setter
	@Getter
	private int paramCount;
	
	/**
	 * 호출할 메소드 핸들<br>
	 * AbstractEnging 클래스의 link 과정에서 설정됨
	 */
	@Setter
	@Getter
	private MethodHandle method;

	/**
	 * 생성자
	 * 
	 * @param methodAlias 메소드 별칭
	 * @param paramCount 파라미터 개수
	 */
	public INVOKE(String methodAlias, int paramCount) {
		this.methodAlias = methodAlias;
		this.paramCount = paramCount;
	}
	
	
	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 메소드 핸들이 있는지 확인
		if(this.method == null) {
			throw new NullPointerException("method is null(check link process)");
		}
		
		// 메소드에 넘겨줄 parameter를 만듦
		Object[] params = new Object[this.paramCount]; // 파라미터의 개수 만큼 배열 생성
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
	
	@Override
	protected String getParamString() {
		
		StringBuilder paramString = new StringBuilder("");
		
		paramString
			.append(this.methodAlias);
		
		return paramString.toString();
	}
}
