package com.jutools.olexp;

import java.util.HashMap;

import com.jutools.instructions.AbstractExp;
import com.jutools.instructions.Instruction;
import com.jutools.olexp.parser.EqualityParser;
import com.jutools.parserfw.AbstractParser;

/**
 * 
 * 
 * @author jmsohn
 */
public class OLExp extends AbstractExp {
	
	/**
	 * 
	 */
	protected OLExp(String exp) throws Exception {
		super(exp);
	}

	@Override
	protected AbstractParser<Instruction> getRootParser() throws Exception {
		return new EqualityParser();
	}
	
	/**
	 * 생성 메소드
	 * 
	 * @param exp 명령어 문자열
	 * @return 생성된 명령어 처리 클래스
	 */
	public static OLExp compile(String exp) throws Exception {
		return new OLExp(exp);
	}
	
	/**
	 * 명령어 수행
	 * 
	 * @return 현재 객체(fluent 코딩용)
	 */
	public OLExp execute() throws Exception {
		this.execute(new HashMap<String, Object>());
		return this;
	}
}
