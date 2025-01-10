package com.jutools.script.engine.instructions;

/**
 * xOR 명령어 클래스
 * 
 * @author jmsohn
 */
public class XOR extends BiBooleanInstruction {

	@Override
	protected Object process(Boolean p1, Boolean p2) throws Exception {
		return p1 ^ p2;
	}
}
