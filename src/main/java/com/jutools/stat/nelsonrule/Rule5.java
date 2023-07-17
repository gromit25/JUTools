package com.jutools.stat.nelsonrule;

/**
 * 넬슨룰-5<br>
 * 연속적으로 관측된 3개의 값 중 2개 이상이 2시그마 이상
 * 
 * @author jmsohn
 */
class Rule5 extends Rule {
	
	/** 위측 2시그마 값 */
	private double upperTwoSigma;
	
	/** 아래측 2시그마 값 */
	private double lowerTwoSigma;
	
	/** 위측 2시그마 이상인 데이터의 큐 */
	private int upperCounterQueue;
	
	/** 아래측 2시그마 이하인 데이터의 큐 */
	private int lowerCounterQueue;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule5(NelsonRule nrule) {
		
		super(nrule);
		
		this.upperCounterQueue = 0;
		this.lowerCounterQueue = 0;
		
		this.upperTwoSigma = this.getNrule().getMean() + (2 * this.getNrule().getStd());
		this.lowerTwoSigma = this.getNrule().getMean() - (2 * this.getNrule().getStd());
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE5;
	}

	@Override
	boolean isViolated(double value) {
		
		// 데이터를 왼쪽으로 이동시켜, 새로운 자리를 만듦
		this.upperCounterQueue <<= 1;
		this.lowerCounterQueue <<= 1;
		
		// 만일, 2시그마 이상 또는 이하 이면
		// 큐의 새로운 자리를 1로 만듦
		if(value > this.upperTwoSigma) {
			this.upperCounterQueue |= 1;
		}
		
		if(value < this.lowerTwoSigma) {
			this.lowerCounterQueue |= 1;
		}
		
		// 3개 중 2개에 대해 2시그마 이상 또는 이하 여부를 확인
		if(RuleUtil.hasOnlyOneZeroInRange(this.upperCounterQueue, 0b111) == 0) {
			this.upperCounterQueue = 0;  // queue clear
			return true;
		}
		
		if(RuleUtil.hasOnlyOneZeroInRange(this.lowerCounterQueue, 0b111) == 0) {
			this.lowerCounterQueue = 0;  // queue clear
			return true;
		}
		
		return false;
	}

}
