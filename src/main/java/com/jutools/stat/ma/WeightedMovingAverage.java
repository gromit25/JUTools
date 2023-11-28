package com.jutools.stat.ma;

/**
 * 가중이동평균(Weighed Moving Average) 계산 클래스
 * 
 * @author jmsohn
 */
public class WeightedMovingAverage extends AbstractMovingAverage {
	
	/** 가중치 목록 */
	private double[] weights;
	
	/** 가중치의 합계값 */
	private double sumOfWeights;

	/**
	 * 생성자
	 * 
	 * @param weights 가중치 목록
	 */
	public WeightedMovingAverage(double[] weights) throws Exception {
		
		super(weights.length);
		
		this.weights = weights;
		
		// 가중치의 합계값 계산
		this.sumOfWeights = 0.0;
		for(double weight: this.weights) {
			this.sumOfWeights += weight;
		}
		
		if(this.sumOfWeights == 0) {
			throw new IllegalArgumentException("sum of weights must be none zero.");
		}
	}
	
	/**
	 * 생성자
	 * 
	 * @param windowSize
	 */
	public WeightedMovingAverage(int windowSize) throws Exception {
		this(makeSeqArray(windowSize));
	}
	
	/**
	 * 주어진 크기만큼 순차 배열을 생성<br>
	 * ex) size = 3 일 경우, [1,2,3]을 생성
	 * 
	 * @param size 배열의 크기
	 * @return 생성된 배열
	 */
	private static double[] makeSeqArray(int size) throws Exception {
		
		if(size < 1) {
			throw new Exception("size must be greater than 1:" + size);
		}
		
		double[] weights = new double[size];
		for(int index = 0; index < size; index++) {
			weights[index] = index + 1;
		}
		
		return weights;
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
