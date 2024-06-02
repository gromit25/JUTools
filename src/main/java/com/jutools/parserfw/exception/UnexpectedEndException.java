package com.jutools.parserfw.exception;

import lombok.Getter;

/**
 * 
 * @author jmsohn
 */
public class UnexpectedEndException extends Exception {

	/** */
	private static final long serialVersionUID = 9217861306987755508L;
	
	/** 예외 발생 위치 */
	@Getter
	private int pos;
	
	/** 예외 발생시 상태 */
	@Getter
	private String status;
	
	/**
	 * 
	 * @param pos
	 * @param ch
	 * @param status
	 */
	public UnexpectedEndException(int pos, String status) {
		
		super(toString(pos, status));
		
		this.pos = pos;
		this.status = status;
	}
	
	/**
	 * 
	 * @param pos
	 * @param status
	 * @return
	 */
	private static String toString(int pos, String status) {
		return "Unexpected end status: " + status + " at " + pos;
	}
	
	@Override
	public String toString() {
		return toString(this.pos, this.status);
	}
}
