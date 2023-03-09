package com.jutools.mathexp.instructions;

import java.util.HashMap;
import java.util.Stack;

public class INVOKE extends Instruction {

	@Override
	public void execute(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		System.out.println("INVOKE " + this.getParam(0) + " " + this.getParam(1));
	}

}
