package com.jutools.stat.nelsonrule;

/**
 * 넬슨룰-3(경향성)<br>
 * 여섯개의 관측된 값이 연속적으로 증가 혹은 감소  
 * 
 * @author jmsohn
 */
class Rule3 extends Rule {
	
	/** 경향성이 발생한 개수 */
	private int counter;
	
	/** 직전 값 */
	private double preValue;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule3(NelsonRule nrule) {
		
		super(nrule);
		
		this.counter = 0;
		this.preValue = Double.NaN;
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE3;
	}

	@Override
	boolean isViolated(double value) {
		
		// 직전 값을 기준으로 증가 및 감소에 따른 개수 확인
		this.counter = RuleUtil.countWithDirection(value, this.preValue, this.counter);
		this.preValue = value;
		
		// this.counter는 점의 개수가 아니라 특정방향으로 이동한 횟수임
		if(Math.abs(this.counter) >= 5) {
			this.counter = 0;
			return true;
		}
		
		return false;
	}

}
