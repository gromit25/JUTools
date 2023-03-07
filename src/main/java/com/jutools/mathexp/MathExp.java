package com.jutools.mathexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.parser.TreeNode;
import com.jutools.mathexp.parser.script.ArithmaticParser;

import lombok.Getter;

public class MathExp {
	
	@Getter
	private Stack<Object> stack;
	@Getter
	private HashMap<String, Object> values;
	
	/**
	 * 
	 * @param stack
	 * @param values
	 */
	private MathExp(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		
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
	private MathExp() throws Exception {
		this(new Stack<Object>(), new HashMap<String, Object>());
	}
	
	/**
	 * 
	 * 
	 * @param exp
	 * @return
	 */
	public static double calculate(String exp, HashMap<String, Object> values) throws Exception {
		
		ArithmaticParser parser = new ArithmaticParser();
		TreeNode<Instruction> insts = parser.parse(exp);
		
		return (double)MathExp.create(new Stack<Object>(), values).execute(insts).getResult(); 
	}

	/**
	 * 
	 * 
	 * @param exp
	 * @return
	 */
	public static double calculate(String exp) throws Exception {
		return calculate(exp, new HashMap<String, Object>()); 
	}
	
	/**
	 *
	 * 
	 * @return
	 */
	public static MathExp create() throws Exception {
		return new MathExp();
	}
	
	/**
	 * 
	 * @param stack
	 * @param values
	 * @return
	 */
	public static MathExp create(Stack<Object> stack, HashMap<String, Object> values) throws Exception {
		return new MathExp(stack, values);
	}
	
	/**
	 * 
	 * 
	 * @param insts
	 */
	public MathExp execute(ArrayList<Instruction> insts) throws Exception {
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
	public MathExp execute(TreeNode<Instruction> nodes) throws Exception {
		
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
