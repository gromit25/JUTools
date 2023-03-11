package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

public abstract class BiNumInstruction extends Instruction {
	
	public abstract Object process(Double p1, Double p2) throws Exception;

	@Override
	public void execute(Stack<Object> stack, Map<String, Object> values) throws Exception {
		
		Object p2 = stack.pop();
		Object p1 = stack.pop();
		
		if(p2 == null) {
			throw new NullPointerException("p2 value is null");
		}
		
		if(p1 == null) {
			throw new NullPointerException("p1 value is null");
		}
		
		if((p2 instanceof Double) == false) {
			throw new Exception("Unexpected type:" + p2.getClass());
		}
		
		if((p1 instanceof Double) == false) {
			throw new Exception("Unexpected type:" + p1.getClass());
		}
		
		Object result = this.process((Double)p1, (Double)p2);
		
		if((result instanceof Double) == false &&
			(result instanceof Boolean) == false) {
			throw new Exception("result value is not expected type:" + result.getClass());
		}
		
		stack.push(result);

	}

}
