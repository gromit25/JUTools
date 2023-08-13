package com.jutools.stat;

import lombok.Getter;

/**
 * 실시간 통계량(statistic) 계산 클래스
 * 
 * @author jmsohn
 */
public class RTParameter {
	
	/** 데이터의 개수 */
	@Getter
	private int count;
	
	/** 합계 */
	@Getter
	private double sum;
	/** 제곱합(표준편차 계산용) */
	private double squaredSum;
	/** 세제곱 합(왜도 계산용) */
	private double cubedSum;
	/** 네제곱 합(첨도 계산용) */
	private double fourthPoweredSum;
	
	/** 평균 값 */
	@Getter
	private double mean;
	/** 분산 */
	@Getter
	private double variance;
	/** 왜도 */
	@Getter
	private double skewness;
	/** 첨도 */
	@Getter
	private double kurtosis;
	
	/** 최소 값 */
	@Getter
	private double min;
	/** 최대 값 */
	@Getter
	private double max;
	
	/**
	 * 생성자
	 */
	public RTParameter() {
		this.reset();
	}

	/**
	 * 
	 */
	public void reset() {

		this.count = 0;
		
		this.sum = 0.0;
		this.squaredSum = 0.0;
		this.cubedSum = 0.0;
		this.fourthPoweredSum = 0.0;
		
		this.mean = 0.0;
		this.variance = 0.0;
		this.skewness = 0.0;
		this.kurtosis = 0.0;
		
		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
	}
	
	/**
	 * 데이터 추가
	 * 
	 * @param value 추가할 데이터
	 * @param squaredValue 추가할 데이터의 제곱값
	 */
	public synchronized void add(double value) {
		
		double squaredValue = value * value;
		double cubedValue = squaredValue * value;
		double fourthPoweredValue = cubedValue * value;
		
		this.count++;
		
		//----
		this.sum += value;
		this.squaredSum += squaredValue;
		this.cubedSum += cubedValue;
		this.fourthPoweredSum += fourthPoweredValue;
		
		//----
		this.mean = this.sum/this.count;
		double squaredMean = this.mean * this.mean;
		double cubedMean = squaredMean * this.mean;
		double fourthPoweredMean = cubedMean * this.mean;
		
		this.variance = (this.squaredSum/this.count) - squaredMean;
		this.skewness =
				(
					(this.cubedSum/this.count)
					- cubedMean
					- (3 * this.mean * this.variance)
				)
				/ Math.pow(this.variance, 3/2);
		this.kurtosis =
				(
					(this.fourthPoweredSum/this.count)
					- (4 * this.mean * this.cubedSum / this.count)
					+ (6 * squaredMean * this.variance)
					+ (3 * fourthPoweredMean)
				)
				/ (this.variance * this.variance);
		
		//----
		if(this.min > value) {
			this.min = value;
		}
		
		if(this.max < value) {
			this.max = value;
		}
	}
	
	/**
	 * 표준 편차 반환
	 * 
	 * @return 표준 편차
	 */
	public double getStd() {
		return Math.sqrt(this.variance);
	}
	
	/**
	 * 현재 객체를 json 문자열 형태로 반환
	 */
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder("");
		
		builder
			.append("{").append("\r\n")
			.append("\"count\":").append(this.count).append("\r\n")
			.append("\"sum\":").append(this.sum).append("\r\n")
			.append("\"mean\":").append(this.mean).append("\r\n")
			.append("\"variance\":").append(this.variance).append("\r\n")
			.append("\"std\":").append(this.getStd()).append("\r\n")
			.append("\"skewness\":").append(this.skewness).append("\r\n")
			.append("\"kurtosis\":").append(this.kurtosis).append("\r\n")
			.append("}");
		
		return builder.toString();
	}
}