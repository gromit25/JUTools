package com.jutools.mathexp.instructions;

import java.util.HashMap;
import java.util.Stack;

/**
 * 
 * 
 * @author jmsohn
 */
public class LOAD_STRING extends Instruction {

	@Override
	public void execute(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		stack.push(this.getParam(0).toString());
	}

}
