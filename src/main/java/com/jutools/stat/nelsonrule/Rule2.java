package com.jutools.stat.nelsonrule;

/**
 * 넬슨룰-2(쏠림)<br>
 * 연속적으로 관측된 9개 이상의 점이 평균선 위 혹은 아래에만 존재할 경우
 * 
 * @author jmsohn
 */
class Rule2 extends Rule {
	
	/** 평균선 위 혹은 아래의 연속된 관측값 개수 */
	private int counter;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule2(NelsonRule nrule) {
		super(nrule);
		this.counter = 0;
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE2;
	}

	@Override
	boolean isViolated(double value) {

		// 평균선 위 혹은 아래의 개수 설정
		this.counter = RuleUtil.countWithDirection(value, this.getNrule().getMean(), this.counter);
		
		// 평균선 위 혹은 아래의 개수가 9개 이상이면 룰 위반
		if(Math.abs(this.counter) >= 9) {
			this.counter = 0;
			return true;
		}
		
		return false;
	}

}