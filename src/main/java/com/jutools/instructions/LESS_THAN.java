package com.jutools.instructions;

public class LESS_THAN extends BiNumInstruction {

	@Override
	public Object process(Double p1, Double p2) throws Exception {
		return p1 < p2;
	}

}
