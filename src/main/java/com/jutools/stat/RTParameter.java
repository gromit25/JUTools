package com.jutools.stat;

import java.util.List;

import lombok.Getter;

/**
 * 실시간 모수(Parameter) 계산 클래스
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
	 * 생성자
	 *  
	 * @param sum 합계
	 * @param squaredSum 제곱합(표준편차 계산용)
	 * @param cubedSum 세제곱 합(왜도 계산용)
	 * @param fourthPoweredSum 네제곱 합(첨도 계산용)
	 * @param count 데이터의 개수
	 */
	public RTParameter(double sum, double squaredSum, double cubedSum, double fourthPoweredSum, int count) throws Exception {
		
		// 입력값 검증
		if(count <= 0) {
			throw new IllegalArgumentException("count must be greater than 0:" + count); 
		}
		
		//---- 데이터 개수 설정
		this.count = count;
		
		//---- 모수 계산을 위한 설정
		this.sum = sum;
		this.squaredSum = squaredSum;
		this.cubedSum = cubedSum;
		this.fourthPoweredSum = fourthPoweredSum;
		
		//---- 설정된 값으로 모수를 계산
		this.calParameter();
	}

	/**
	 * 모수 값 초기화
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
	 * 데이터 배열 추가
	 * 
	 * @param values 추가할 데이터 배열
	 */
	public void addAll(double... values) {
		for(double value: values) {
			this.add(value);
		}
	}
	
	/**
	 * 데이터 배열 추가
	 * 
	 * @param values 추가할 데이터 배열
	 */
	public void addAll(List<Double> values) {
		for(Double value: values) {
			this.add(value);
		}
	}
	
	/**
	 * 데이터 추가
	 * 
	 * @param value 추가할 데이터
	 * @param squaredValue 추가할 데이터의 제곱값
	 */
	public synchronized void add(double value) {
		
		//----
		double squaredValue = value * value;
		double cubedValue = squaredValue * value;
		double fourthPoweredValue = cubedValue * value;
		
		//---- 데이터 초기화
		this.count++;
		
		//---- 모수 계산을 위한 값 설정
		this.sum += value;
		this.squaredSum += squaredValue;
		this.cubedSum += cubedValue;
		this.fourthPoweredSum += fourthPoweredValue;
		
		//---- 설정된 값으로 모수를 계산
		this.calParameter();
		
		//---- 최소값, 최대값 설정
		if(this.min > value) {
			this.min = value;
		}
		
		if(this.max < value) {
			this.max = value;
		}
	}
	
	/**
	 * 객체에 설정된 값으로 모수 계산
	 */
	private void calParameter() {
		
		//---- 데이터 개수 초기화
		double n = (double)this.count;
		
		//---- 모수 계산 수행
		this.mean = this.sum/n;
		double squaredMean = this.mean * this.mean;
		double cubedMean = squaredMean * this.mean;
		double fourthPoweredMean = cubedMean * this.mean;
		
		this.variance = (this.squaredSum/n) - squaredMean;
		this.skewness =
				(
					(this.cubedSum/n)
					- cubedMean
					- (3 * this.mean * this.variance)
				)
				/ Math.pow(this.variance, 3.0/2.0);
		this.kurtosis =
				(
					(this.fourthPoweredSum/n)
					- (4 * this.mean * this.cubedSum / n)
					+ (6 * squaredMean * this.variance)
					+ (3 * fourthPoweredMean)
				)
				/ (this.variance * this.variance) - 3;
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
			.append("\"min\":").append(this.min).append("\r\n")
			.append("\"max\":").append(this.max).append("\r\n")
			.append("}");
		
		return builder.toString();
	}
}