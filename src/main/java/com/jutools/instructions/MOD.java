package com.jutools.instructions;

/**
 * Modular 연산 
 * 
 * @author jmsohn
 */
public class MOD extends BiNumInstruction {

	@Override
	public Object process(Double p1, Double p2) throws Exception {
		return p1 % p2;
	}

}
