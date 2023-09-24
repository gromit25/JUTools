package com.jutools.stat.ma;

public class ExponentialMovingAverage extends AbstractMovingAverage {
	
	private double n;

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
