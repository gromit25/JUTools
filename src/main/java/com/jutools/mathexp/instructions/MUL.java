package com.jutools.mathexp.instructions;

import java.util.HashMap;
import java.util.Stack;

public class MUL extends Instruction {

	@Override
	public void execute(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		double p2 = (double)stack.pop();
		double p1 = (double)stack.pop();
		
		stack.push(p1 * p2);
	}

}
