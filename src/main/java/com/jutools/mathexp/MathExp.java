package com.jutools.mathexp;

import java.util.HashMap;
import java.util.Map;

import com.jutools.mathexp.parser.UnitParser;
import com.jutools.parserfw.AbstractParser;
import com.jutools.scriptengine.AbstractEngine;
import com.jutools.scriptengine.Instruction;

/**
 * 수식 처리 클래스
 * 
 * @author jmsohn
 */
public class MathExp extends AbstractEngine {
	
	/**
	 * 생성자(외부 생성 불가)
	 * 
	 * @parma exp 수식 
	 */
	private MathExp(String exp) throws Exception {
		super(exp);
	}
	
	@Override
	protected AbstractParser<Instruction> getRootParser() throws Exception {
		return new UnitParser();
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
	 * 수식 수행
	 * 
	 * @return 현재 객체(fluent 코딩용)
	 */
	public MathExp execute() throws Exception {
		this.execute(new HashMap<String, Object>());
		return this;
	}
	
	/**
	 * 수식 수행 결과 반환
	 * 
	 * @return 수식 수행 결과
	 */
	public MathResult getResult() throws Exception {
		
		MathResult result = new MathResult();
		
		result.setValue(this.pop(Double.class));
		result.setBaseUnit(this.pop(String.class));
		
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
		MathExp mathExp = (MathExp)MathExp.compile(exp).execute(values);
		return mathExp.getResult();
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
		return calculateWithUnit(exp, values).getValue(); 
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
