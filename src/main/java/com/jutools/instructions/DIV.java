package com.jutools.instructions;

/**
 * 나눗셈 명령어 클래스
 * 
 * @author jmsohn
 */
public class DIV extends BiNumInstruction {

	@Override
	public Object process(Double p1, Double p2) throws Exception {
		return p1 / p2;
	}

}
