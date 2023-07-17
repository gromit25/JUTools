package com.jutools.stat.nelsonrule;

/**
 * 넬슨룰-6<br>
 * 연속적으로 관측된 5개의 관측치 중 4개 이상의 관측치가 1 시그마 기준선 바깥에 존재
 * 
 * @author jmsohn
 */
class Rule6 extends Rule {
	
	/** 위측 1시그마 값 */
	private double upperOneSigma;
	
	/** 아래측 1시그마 값 */
	private double lowerOneSigma;
	
	/** 위측 1시그마 이상인 데이터의 큐 */
	private int upperCounterQueue;
	
	/** 아래측 1시그마 이상인 데이터의 큐 */
	private int lowerCounterQueue;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule6(NelsonRule nrule) {
		
		super(nrule);
		
		this.upperCounterQueue = 0;
		this.lowerCounterQueue = 0;
		
		this.upperOneSigma = this.getNrule().getMean() + this.getNrule().getStd();
		this.lowerOneSigma = this.getNrule().getMean() - this.getNrule().getStd();
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE6;
	}

	@Override
	boolean isViolated(double value) {
		
		// 데이터를 왼쪽으로 이동시켜, 새로운 자리를 만듦
		this.upperCounterQueue <<= 1;
		this.lowerCounterQueue <<= 1;
		
		// 만일, 1시그마 이상 또는 이하 이면
		// 큐의 새로운 자리를 1로 만듦
		if(value > this.upperOneSigma) {
			this.upperCounterQueue |= 1;
		}
		
		if(value < this.lowerOneSigma) {
			this.lowerCounterQueue |= 1;
		}
		
		// 5개 중 4개에 대해 2시그마 이상 또는 이하 여부를 확인
		if(RuleUtil.hasOnlyOneZeroInRange(this.upperCounterQueue, 0b11111) == 0) {
			this.upperCounterQueue = 0;  // queue clear
			return true;
		}
		
		if(RuleUtil.hasOnlyOneZeroInRange(this.lowerCounterQueue, 0b11111) == 0) {
			this.lowerCounterQueue = 0;  // queue clear
			return true;
		}
		
		return false;
	}

}
