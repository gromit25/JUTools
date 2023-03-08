package com.jutools.mathexp.instructions;

import java.util.HashMap;
import java.util.Stack;

/**
 * 
 * 
 * @author jmsohn
 */
public class NOP extends Instruction {

	@Override
	public void execute(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		// Do nothing
	}

}
