package com.jutools.parserfw.exception;

import lombok.Getter;

/**
 * 
 * @author jmsohn
 */
public class ParseException extends Exception {

	/** */
	private static final long serialVersionUID = -5962084713002262234L;
	
	/** 예외 발생 위치 */
	@Getter
	private int pos;
	
	/** 예외 발생시 문자 */
	@Getter
	private char ch;
	
	/** 예외 발생시 상태 */
	@Getter
	private String status;
	
	/**
	 * 
	 * @param pos
	 * @param ch
	 * @param status
	 */
	public ParseException(int pos, char ch, String status) {
		
		super(toString(pos, ch, status));
		
		this.pos = pos;
		this.ch = ch;
		this.status = status;
	}
	
	/**
	 * 
	 * @param pos
	 * @param ch
	 * @param status
	 * @return
	 */
	private static String toString(int pos, char ch, String status) {
		return "Unexpected char: " + ch + ", status:" + status + " at " + pos;
	}
	
	@Override
	public String toString() {
		return toString(this.pos, this.ch, this.status);
	}
}
