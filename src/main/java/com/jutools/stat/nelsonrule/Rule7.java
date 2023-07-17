package com.jutools.stat.nelsonrule;

/**
 * 넬슨룰-7<br>
 * 연속된 15개의 값이 1 시그마 이내에 존재 
 * 
 * @author jmsohn
 */
class Rule7 extends Rule {
	
	/** 위측 1시그마 값 */
	private double upperOneSigma;
	
	/** 아래측 1시그마 값 */
	private double lowerOneSigma;
	
	/** 위측 1시그마, 아래측 1시그마 사이의 데이터의 큐*/
	private int counterQueue;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule7(NelsonRule nrule) {
		
		super(nrule);
		
		this.counterQueue = 0;
		
		this.upperOneSigma = this.getNrule().getMean() + this.getNrule().getStd();
		this.lowerOneSigma = this.getNrule().getMean() - this.getNrule().getStd();
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE7;
	}

	@Override
	boolean isViolated(double value) {
		
		// 데이터를 왼쪽으로 이동시켜, 새로운 자리를 만듦
		this.counterQueue <<= 1;
		
		// 만일, 평균값을 중심으로 1시그마 이내일 경우 
		// 큐의 새로운 자리를 1로 만듦
		if(value < this.upperOneSigma && value > this.lowerOneSigma) {
			this.counterQueue |= 1;
		}
		
		// 15개의 값이 연속적으로 1인지 여부 확인 
		int discriminator = this.counterQueue & 0b111111111111111;
		if(discriminator == 0b111111111111111) {
			this.counterQueue = 0;
			return true;
		}
		
		return false;
	}

}
