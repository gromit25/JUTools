package com.jutools.stat.nelsonrule;

/**
 * 넬슨룰-8<br>
 * 연속된 8개의 값이 1 시그마 이외에 존재 
 * 
 * @author jmsohn
 */
class Rule8 extends Rule {
	
	/** 위측 1시그마 값 */
	private double upperOneSigma;
	
	/** 아래측 1시그마 값 */
	private double lowerOneSigma;
	
	/** 위측 1시그마, 아래측 1시그마 바깥 여부 데이터의 큐*/
	private int counterQueue;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule8(NelsonRule nrule) {
		
		super(nrule);
		
		this.counterQueue = 0;
		
		this.upperOneSigma = this.getNrule().getMean() + this.getNrule().getStd();
		this.lowerOneSigma = this.getNrule().getMean() - this.getNrule().getStd();
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE8;
	}

	@Override
	boolean isViolated(double value) {
		
		// 데이터를 왼쪽으로 이동시켜, 새로운 자리를 만듦
		this.counterQueue <<= 1;
		
		// 만일, 평균값을 중심으로 1시그마 이외일 경우 
		// 큐의 새로운 자리를 1로 만듦
		if(value < this.upperOneSigma && value > this.lowerOneSigma) {
			this.counterQueue |= 1;
		}
		
		// 8개의 값이 연속적으로 1인지 여부 확인
		int discriminator = this.counterQueue & 0b11111111;
		if(discriminator == 0) {
			this.counterQueue = 0;
			return true;
		}
		
		return false;
	}

}
