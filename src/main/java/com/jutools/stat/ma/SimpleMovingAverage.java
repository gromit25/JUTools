package com.jutools.stat.ma;

public class SimpleMovingAverage extends AbstractMovingAverage {
	
	private double sum;

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
