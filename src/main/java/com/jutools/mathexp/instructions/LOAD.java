package com.jutools.mathexp.instructions;

import java.util.HashMap;
import java.util.Stack;

/**
 * 
 * 
 * @author jmsohn
 */
public class LOAD extends Instruction {

	@Override
	public void execute(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		double value = Double.parseDouble(this.getParam(0));
		stack.push(value);
	}

}
