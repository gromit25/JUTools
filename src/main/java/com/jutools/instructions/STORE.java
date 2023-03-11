package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

public class STORE extends Instruction {

	@Override
	public void execute(Stack<Object> stack, Map<String, Object> values) throws Exception {
		Object p1 = stack.pop();
		if(p1 == null) {
			throw new NullPointerException("stack is empty");
		}
	}

}
