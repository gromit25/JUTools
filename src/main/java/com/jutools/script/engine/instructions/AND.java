package com.jutools.script.engine.instructions;

/**
 * and 명령어 클래스
 * 
 * @author jmsohn
 */
public class AND extends BiBooleanInstruction {

	@Override
	protected Object process(Boolean p1, Boolean p2) throws Exception {
		return p1 && p2;
	}
}
