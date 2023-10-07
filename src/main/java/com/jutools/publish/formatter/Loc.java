package com.jutools.publish.formatter;

import lombok.Data;

/**
 * XML 파싱 위치 정보 클래스
 * 
 * @author jmsohn
 */
@Data
public class Loc implements Comparable<Loc>{
	
	/** XML 라인 번호 */
	private int lineNum;
	
	/** XML 컬럼 번호 */
	private int columnNum;
	
	/**
	 * 생성자
	 * 
	 * @param lineNum XML 라인 번호 초기값
	 * @param columnNum XML 컬럼 번호 초기값
	 */
	public Loc(int lineNum, int columnNum) {
		this.setLineNum(lineNum);
		this.setColumnNum(columnNum);
	}

	@Override
	public int compareTo(Loc o) {

		// 다른 Loc 객체와 비교
		
		// 다른 Loc 객체가 null 이면, 1을 반환
		if(null == o) {
			return 1;
		}
		
		// 라인 비교
		// 현재 라인이 더 크면, 1을 비교
		if(this.getLineNum() > o.getLineNum()) {
			return 1;
		}
		
		// 컬럼 비교
		// 라인이 같고, 컬럼이 현재 컬럼이 크면 1을 반환 
		if(this.getLineNum() == o.getLineNum()) {
			if(this.getColumnNum() > o.getColumnNum()) {
				return 1;
			} else if(this.getColumnNum() == o.getColumnNum()) {
				return 0;
			}
		}
		
		return -1;
	}
}