package com.jutools.stat.ma;

/**
 * 단순이동평균(Simple Moving Average) 계산 클래스
 * 
 * @author jmsohn
 */
public class SimpleMovingAverage extends AbstractMovingAverage {
	
	/** 구간 합계 */
	private double sum;

	/**
	 * 생성자 
	 * 
	 * @param windowSize
	 */
	public SimpleMovingAverage(int windowSize) throws Exception {
		super(windowSize);
	}

	@Override
	protected double calculate(double in, double out) {
		
		sum += in - out;
		
		if(this.getQueue().size() < this.getWindowSize()) {
			return sum / (double)this.getQueue().size();
		} else {
			return sum / (double)this.getWindowSize();
		}
	}
}
