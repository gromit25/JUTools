package com.jutools.mathexp.instructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.jutools.mathexp.parser.TreeNode;

import lombok.Getter;

/**
 * 
 * 
 * @author jmsohn
 */
public class Instructions {
	
	@Getter
	private Stack<Object> stack;
	@Getter
	private HashMap<String, Object> values;
	
	/**
	 * 
	 * @param stack
	 * @param values
	 */
	private Instructions(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		
		if(stack == null) {
			throw new NullPointerException("stack is null");
		}
		
		if(values == null) {
			throw new NullPointerException("values is null");
		}
		
		this.stack = stack;
		this.values = values;
	}
	
	/**
	 * 
	 * 
	 */
	private Instructions() throws Exception {
		this(new Stack<Object>(), new HashMap<String, Object>());
	}
	
	/**
	 *
	 * 
	 * @return
	 */
	public static Instructions create() throws Exception {
		return new Instructions();
	}
	
	/**
	 * 
	 * @param stack
	 * @param values
	 * @return
	 */
	public static Instructions create(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		return new Instructions(stack, values);
	}
	
	/**
	 * 
	 * 
	 * @param insts
	 */
	public Instructions execute(ArrayList<Instruction> insts) throws Exception {
		for(Instruction inst: insts) {
			inst.execute(this.stack, this.values);
		}
		
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param nodes
	 * @return
	 */
	public Instructions execute(TreeNode<Instruction> nodes) throws Exception {
		
		ArrayList<Instruction> insts = nodes.travelPostOrder();
		this.execute(insts);
		
		return this;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Object getResult() throws Exception {
		return this.stack.pop();
	}

}
