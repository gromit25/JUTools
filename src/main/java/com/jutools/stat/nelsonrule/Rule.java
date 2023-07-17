package com.jutools.stat.nelsonrule;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 넬슨룰의 추상 클래스
 * 
 * @author jmsohn
 */
abstract class Rule {
	
	/** */
	@Getter(AccessLevel.PROTECTED)
	private NelsonRule nrule;
	
	/**
	 * rule의 bitmap 값 반환
	 * 
	 * @return rule의 bitmap 값
	 */
	abstract int bitmap();
	
	/**
	 * 룰에 대한 위반 검사
	 * 
	 * @param value 현재 입력값
	 * @return 룰 위반 여부(위반시 true)
	 */
	abstract boolean isViolated(double value);
	
	/**
	 * 생성자
	 * 
	 * @param nrule
	 */
	Rule(NelsonRule nrule) {
		this.nrule = nrule;
	}
}
