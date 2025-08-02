package com.jutools.script.engine.instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import lombok.Getter;
import lombok.Setter;

/**
 * 목록 생성 명령어
 * 
 * @author jmsohn
 */
public class NEW_LIST extends Instruction {
	
	/** 목록의 개수 */
	@Getter
	@Setter
	private int count;

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		List<Object> list = new ArrayList<>();
		
		for(int index = 0; index < this.count; index++) {
			
			if(stack.size() == 0) {
				throw new Exception("stack is empty.");
			}
			
			list.add(0, stack.pop());
		}
		
		stack.push(list);
		
		return 1;
	}
	
	@Override
	protected String getParamString() {
		return Integer.toString(this.count);
	}
}
