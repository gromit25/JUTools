package com.jutools.mathexp.parser;

import java.util.ArrayList;

/**
 * 전이함수 생성 클래스
 * 
 * @author jmsohn
 */
public class TransferBuilder {
	
	/** */
	private ArrayList<Object[]> specs;
	
	/**
	 * 생성자
	 */
	public TransferBuilder() {
		this.specs = new ArrayList<Object[]>();
	}
	
	/**
	 * 
	 * @param pattern
	 * @param nextStatus
	 * @return
	 */
	public TransferBuilder add(String pattern, String nextStatus, boolean pushback) throws Exception {
		
		Object[] spec = new Object[3];
		spec[0] = pattern;
		spec[1] = nextStatus;
		spec[2] = pushback;
		
		specs.add(spec);
		
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param pattern
	 * @param nextStatus
	 * @return
	 */
	public TransferBuilder add(String pattern, String nextStatus) throws Exception {
		return this.add(pattern, nextStatus, false);
	}

	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Transfer> build() throws Exception {
		
		ArrayList<Transfer> transferFunctions = new ArrayList<Transfer>();
		
		for(Object[] spec: this.specs) {
			Transfer transfer = new Transfer(spec[0].toString(), spec[1].toString(), (boolean)spec[2]);
			transferFunctions.add(transfer);
		}
		
		return transferFunctions;
	}
}