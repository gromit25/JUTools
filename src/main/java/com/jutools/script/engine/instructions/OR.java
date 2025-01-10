package com.jutools.script.engine.instructions;

/**
 * or 실행 결과를 스택에 추가
 * 
 * @author jmsohn
 */
public class OR extends BiBooleanInstruction {

	@Override
	protected Object process(Boolean p1, Boolean p2) throws Exception {
		return p1 || p2;
	}
}
