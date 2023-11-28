package com.jutools.stat.ma;

/**
 * 지수이동평균(Exponential Moving Average:EMA) 계산 클래스
 * 
 * @author jmsohn
 */
public class ExponentialMovingAverage extends AbstractMovingAverage {
	
	/** 데이터 수 */
	private double n;

	/**
	 * 생성자
	 * 
	 * @param windowSize Moving Average 구간
	 */
	public ExponentialMovingAverage(int windowSize) throws Exception {
		super(windowSize);
		this.n = 0.0;
	}

	@Override
	protected double calculate(double in, double out) {
		
		this.n++;
		
		// EMA(n) = (2 / (n + 1)) * 현재 값 + ((n - 1) / (n + 1)) * EMA(n - 1)
		if(this.n > this.getWindowSize()) { // window size를 넘은 경우 
			this.n = 1.0;
			return (2 / (n + 1)) * in;
		} else {                            // window size 이내인 경우
			return (2 / (n + 1)) * in + ((n - 1) / (n + 1)) * this.getAverage();
		}
	}
}
