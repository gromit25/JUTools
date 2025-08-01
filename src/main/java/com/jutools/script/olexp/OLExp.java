package com.jutools.script.olexp;

import java.util.HashMap;

import com.jutools.script.engine.AbstractEngine;
import com.jutools.script.engine.instructions.Instruction;
import com.jutools.script.olexp.parser.StoreParser;
import com.jutools.script.parser.AbstractParser;

/**
 * One Line Expression 클래스
 * 
 * @author jmsohn
 */
public class OLExp extends AbstractEngine {
	
	/**
	 * 생성자
	 * 
	 * @param exp 스크립트 문자열
	 * @param methodClsAry 커스텀 메소드 클래스 목록
	 */
	protected OLExp(String exp, Class<?>... methodClsAry) throws Exception {
		super(exp, methodClsAry);
	}

	@Override
	protected AbstractParser<Instruction> getRootParser() throws Exception {
		return new StoreParser();
	}
	
	/**
	 * 생성 메소드
	 * 
	 * @param exp 스크립트 문자열
	 * @param methodClsAry 커스텀 메소드 클래스 목록
	 * @return 생성된 명령어 처리 클래스
	 */
	public static OLExp compile(String exp, Class<?>... methodClsAry) throws Exception {
		return new OLExp(exp, methodClsAry);
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
	
	/**
	 * 명령어 수행(디버그 모드)
	 * 
	 * @return 현재 객체(fluent 코딩용)
	 */
	public OLExp executeForDebug() throws Exception {
		this.executeForDebug(new HashMap<String, Object>());
		return this;
	}
}
