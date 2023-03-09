package com.jutools.mathexp;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.jutools.mathexp.instructions.INVOKE;
import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.parser.TreeNode;
import com.jutools.mathexp.parser.script.UnitParser;

import lombok.Getter;

/**
 * 
 * 
 * @author jmsohn
 */
public class MathExp {
	
	@Getter
	private Stack<Object> stack = new Stack<Object>();
	@Getter
	private HashMap<String, Object> values;
	
	private HashMap<String, Method> methods = new HashMap<String, Method>();
	
	/**
	 * 
	 * @param values
	 */
	private MathExp(HashMap<String, Object> values) throws Exception {
		
		if(values == null) {
			throw new NullPointerException("values is null");
		}
		
		this.values = values;
		this.setMethod(BuiltInMethods.class);
	}
	
	/**
	 * 
	 * 
	 */
	private MathExp() throws Exception {
		this(new HashMap<String, Object>());
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
	 * @param values
	 * @return
	 */
	public static MathExp create(HashMap<String, Object> values) throws Exception {
		return new MathExp(values);
	}
	
	/**
	 * 
	 * 
	 * @param methodClass
	 * @return
	 */
	public MathExp setMethod(Class<?> methodClass) throws Exception {

		//
		if(methodClass == null) {
			throw new NullPointerException("method class is null");
		}
		
		for(Method method: methodClass.getMethods()) {
			
			//
			MethodMap methodMap = method.getAnnotation(MethodMap.class);
			if(methodMap == null) {
				continue;
			}
			
			//
			int modifier = method.getModifiers();
			
			if(Modifier.isStatic(modifier) == false) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method is not static");
			}
			
			if(Modifier.isPublic(modifier) == false) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method is not public");
			}
			
			Class<?> returnType = method.getReturnType();
			if(returnType != double.class && returnType != Double.class) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method return type is not double or Double");
			}
			
			//
			this.methods.put(methodMap.alias(), method);
		}
		
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param alias
	 * @return
	 */
	public Method getMethod(String alias) throws Exception {
		
		if(this.methods.containsKey(alias) == false) {
			throw new Exception("method is not found");
		}
		
		return this.methods.get(alias);
	}
	
	/**
	 * 
	 * @param insts
	 * @return
	 */
	private MathExp linkMethod(ArrayList<Instruction> insts) throws Exception {
		
		for(Instruction inst: insts) {
			
			if(inst instanceof INVOKE == false) {
				continue;
			}
			
			// method link 작업 수행
			INVOKE invokeInst = (INVOKE)inst;
			
			String alias = invokeInst.getParam(0); // method alias
			Method method = this.methods.get(alias); // get aliased method
			
			invokeInst.setMethod(method);
		}
		
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param insts
	 */
	private MathExp execute(ArrayList<Instruction> insts) throws Exception {
		
		for(Instruction inst: insts) {
			inst.execute(this.stack, this.values);
		}
		
		return this;
	}

	/**
	 * 
	 * 
	 * @param insts
	 */
	public MathExp execute(String exp) throws Exception {
		
		UnitParser parser = new UnitParser();
		ArrayList<Instruction> insts = parser.parse(exp).travelPostOrder();
		
		return this.linkMethod(insts).execute(insts);
	}
	
	/**
	 * 
	 * 
	 * @param name
	 * @param value
	 */
	public MathExp putValue(String name, Object value) throws Exception {
		
		this.values.put(name, value);
		
		return this;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public MathResult getResult() throws Exception {
		
		MathResult result = new MathResult();
		
		result.setValue((double)this.stack.pop());
		result.setBaseUnit(this.stack.pop().toString());
		
		return result;
	}
	
	/**
	 * 
	 * @param exp
	 * @param values
	 * @return
	 */
	public static MathResult calculateWithUnit(String exp, HashMap<String, Object> values) throws Exception {
		return MathExp.create(values).execute(exp).getResult();
	}
	
	/**
	 * 
	 * @param exp
	 * @param values
	 * @return
	 */
	public static MathResult calculateWithUnit(String exp) throws Exception {
		return calculateWithUnit(exp, new HashMap<String, Object>());
	}

	
	/**
	 * 
	 * 
	 * @param exp
	 * @return
	 */
	public static double calculate(String exp, HashMap<String, Object> values) throws Exception {
		return calculateWithUnit(exp, values).getValue(); 
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
}
