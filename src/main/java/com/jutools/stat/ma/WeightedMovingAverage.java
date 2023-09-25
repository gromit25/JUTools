package com.jutools.stat.ma;

/**
 * 
 * @author jmsohn
 */
public class WeightedMovingAverage extends AbstractMovingAverage {
	
	private double[] weights;
	private double sumOfWeights;

	/**
	 * 
	 * @param weights
	 */
	public WeightedMovingAverage(double[] weights) throws Exception {
		
		super(weights.length);
		
		this.weights = weights;
		
		this.sumOfWeights = 0.0;
		for(double weight: this.weights) {
			this.sumOfWeights += weight;
		}
		
		if(this.sumOfWeights == 0) {
			throw new IllegalArgumentException("sum of weights must be none zero");
		}
	}

	@Override
	protected double calculate(double in, double out) {
		
		double sum = 0.0;
		int index = this.weights.length - this.getQueue().size();
		
		for(double value: this.getQueue()) {
			sum += value * this.weights[index];
		}
		
		return sum / this.sumOfWeights;
	}

}
