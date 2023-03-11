package com.jutools.mathexp;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.jutools.mathexp.instructions.INVOKE;
import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.parser.script.UnitParser;

import lombok.Getter;

/**
 * 수식 처리 클래스
 * 
 * @author jmsohn
 */
public class MathExp {
	
	/** 수식 문자열 */
	@Getter
	private String exp;
	/** 수식 명령어 목록 */
	private ArrayList<Instruction> insts;
	/** 수식 처리시 사용할 Stack */
	@Getter
	private Stack<Object> stack = new Stack<Object>();
	/** 수식 내의 alias 메소드의 실제 메소드 - K: 메소드 alias 명, V: 실제 수행 메소드 */
	private Map<String, Method> methods = new HashMap<String, Method>();
	
	/**
	 * 생성자(외부 생성 불가)
	 * 
	 * @parma exp 수식 
	 */
	private MathExp(String exp) throws Exception {
		
		if(exp == null) {
			throw new NullPointerException("math expression(exp) is null");
		}
		
		this.exp = exp;
		this.setMethod(BuiltInMethods.class);
		
		// 수식 파싱
		UnitParser parser = new UnitParser();
		this.insts = parser.parse(exp).travelPostOrder();
		
		// 메소드 링킹
		this.linkMethod();
		
	}
	
	/**
	 * 생성 메소드
	 * 
	 * @param exp 산술식 문자열
	 * @return 생성된 산술식 처리 클래스
	 */
	public static MathExp compile(String exp) throws Exception {
		return new MathExp(exp);
	}
	
	/**
	 * 실제 실행할 메소드 설정
	 * 
	 * @param methodClass 실제 실행할 메소드를 가지는 클래스(MethodAlias Annotation 사용)
	 * @return 현재 객체(fluent 코딩용)
	 */
	public MathExp setMethod(Class<?> methodClass) throws Exception {

		// 입력값 검증
		if(methodClass == null) {
			throw new NullPointerException("method class is null");
		}
		
		// 각 메소드 별로 검사하여 메소드 추가
		for(Method method: methodClass.getMethods()) {
			
			// 메소드에 MethodAlias Annotation을 가져옴
			MethodAlias methodMap  = method.getAnnotation(MethodAlias.class);
			if(methodMap == null) {
				continue;
			}
			
			// 메소드는 public static 이어야 함
			int modifier = method.getModifiers();
			
			if(Modifier.isStatic(modifier) == false) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method is not static");
			}
			
			if(Modifier.isPublic(modifier) == false) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method is not public");
			}
			
			// 메소드의 return type은 double/Double 형이어야 함
			Class<?> returnType = method.getReturnType();
			if(returnType != double.class && returnType != Double.class) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method return type is not double or Double");
			}
			
			// 메소드 추가
			this.methods.put(methodMap.alias(), method);
		}
		
		return this;
	}
	
	/**
	 * alias에 해당하는 메소드를 반환하는 메소드
	 * 
	 * @param alias 검색할 alias
	 * @return 메소드
	 */
	private Method getMethod(String alias) throws Exception {
		
		if(this.methods.containsKey(alias) == false) {
			throw new Exception("method is not found");
		}
		
		return this.methods.get(alias);
	}
	
	/**
	 * 메소드 호출(INVOKE) 명령어의 alias에 실제 메소드를 연결(linking)
	 * 
	 * @return 현재 객체(fluent 코딩용)
	 */
	private MathExp linkMethod() throws Exception {
		
		if(this.insts == null) {
			throw new NullPointerException("instruction list is null");
		}
		
		for(Instruction inst: this.insts) {
			
			if(inst instanceof INVOKE == false) {
				continue;
			}
			
			// method link 작업 수행
			INVOKE invokeInst = (INVOKE)inst;
			
			String alias = invokeInst.getParam(0); // method alias
			Method method = this.getMethod(alias); // get aliased method
			
			invokeInst.setMethod(method);
		}
		
		return this;
	}
	
	/**
	 * 수식 수행
	 * 
	 * @param values 
	 * @return 현재 객체(fluent 코딩용)
	 */
	public MathExp execute(Map<String, Object> values) throws Exception {
		
		for(Instruction inst: insts) {
			inst.execute(this.stack, values);
		}
		
		return this;
	}

	/**
	 * 수식 수행
	 * 
	 * @return 현재 객체(fluent 코딩용)
	 */
	public MathExp execute() throws Exception {
		return this.execute(new HashMap<String, Object>());
	}
	
	/**
	 * 수식 수행 결과 반환
	 * 
	 * @return 수식 수행 결과
	 */
	public MathResult getResult() throws Exception {
		
		MathResult result = new MathResult();
		
		result.setValue((double)this.stack.pop());
		result.setBaseUnit(this.stack.pop().toString());
		
		return result;
	}
	
	/**
	 * 수식 수행 후 결과 반환(단위 포함)
	 * 
	 * @param exp 수식
	 * @param values 산술식 처리시 변수 목록
	 * @return 수식 수행 결과(단위 포함)
	 */
	public static MathResult calculateWithUnit(String exp, Map<String, Object> values) throws Exception {
		return MathExp.compile(exp).execute(values).getResult();
	}
	
	/**
	 * 수식 수행 후 결과 반환(단위 포함)
	 * 
	 * @param exp 수식
	 * @return 수식 수행 결과(단위 포함)
	 */
	public static MathResult calculateWithUnit(String exp) throws Exception {
		return calculateWithUnit(exp, new HashMap<String, Object>());
	}

	
	/**
	 * 수식 수행 후 결과 반환
	 * 
	 * @param exp 수식
	 * @return 수식 수행 결과
	 */
	public static double calculate(String exp, Map<String, Object> values) throws Exception {
		return calculateWithUnit(exp, values).getValue(Double.class); 
	}

	/**
	 * 수식 수행 후 결과 반환
	 * 
	 * @param exp 수식
	 * @return 수식 수행 결과
	 */
	public static double calculate(String exp) throws Exception {
		return calculate(exp, new HashMap<String, Object>()); 
	}
}
