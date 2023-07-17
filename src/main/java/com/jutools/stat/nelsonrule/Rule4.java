package com.jutools.stat.nelsonrule;

import com.jutools.MathUtil;

/**
 * 넬슨룰-4(진동)<br>
 * 연속적인 14개 이상의 관측값 방향이 번갈아 나타나는 경우
 * 
 * @author jmsohn
 */
class Rule4 extends Rule {
	
	/** 현재까지 진동한 횟수 */
	private int counter;
	
	/** 직전 값 */
	private double preValue;
	
	/**
	 * 직전 이동 방향<br>
	 * +1 : 증가 방향<br>
	 * 0 : 동일<br>
	 * -1 : 감소 방향<br>
	 */
	private int preDirection;

	/**
	 * 생성자
	 * 
	 * @param nrule 넬슨룰 객체
	 */
	Rule4(NelsonRule nrule) {
		
		super(nrule);
		
		this.counter = 0;
		this.preValue = Double.NaN;
		this.preDirection = Integer.MIN_VALUE; // 이전 방향이 설정되지 않음
	}

	@Override
	int bitmap() {
		return NelsonRule.RULE4;
	}

	@Override
	boolean isViolated(double value) {
		
		// 첫번째 값이 들어왔을 경우
		if(Double.isNaN(this.preValue) == true) {
			
			this.counter = 1;
			this.preValue = value;
			this.preDirection = Integer.MIN_VALUE; // 이전 방향이 설정되지 않음
			
			return false;
		}
		
		// 두번째 값이 들어왔을 경우
		if(this.preDirection == Integer.MIN_VALUE) {
			
			this.preDirection = MathUtil.sign(value - this.preValue);
			
			this.counter = 2;
			this.preValue = value;
			
			return false;
		}
		
		// 세번째 이상의 값이 들어왔을 경우
		int direction = MathUtil.sign(value - this.preValue);
		int discriminator = this.preDirection * direction;
		
		// 다른 방향 discriminator가 음수 이면,
		// 개수(this.counter)를 1을 추가함
		if(discriminator < 0) {
			this.counter++;
		} else {
			this.counter = 1;
		}
		
		this.preValue = value;
		this.preDirection = direction;
		
		// 14개 이상이면, 룰 위반
		if(this.counter >= 14) {
			this.counter = 0;
			return true;
		}
		
		return false;
	}

}
