package com.jutools.stat.nelsonrule;

/**
 * 넬슨룰-1<br>
 * 측정된 값이 UCL과 LCL을 벗어난 경우
 * 
 * @author jmsohn
 */
class Rule1 extends Rule {
	
	/** UCL(Upper Control Limit) */
	private double ucl;
	
	/** LCL(Lower Control Limit) */
	private double lcl;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule1(NelsonRule nrule) {
		
		super(nrule);
		
		// UCL 및 LCL 계산하여 설정
		this.ucl = this.getNrule().getMean() + (3 * this.getNrule().getStd());
		this.lcl = this.getNrule().getMean() - (3 * this.getNrule().getStd());
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE1;
	}

	@Override
	boolean isViolated(double value) {
		
		// UCL을 넘거나 LCL 보다 작을 경우 룰 위반
		if(value > this.ucl) {
			return true;
		} else if(value < this.lcl) {
			return true;
		}
		
		return false;
	}

}
