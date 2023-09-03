package com.jutools.stat;

import java.util.List;

import lombok.Getter;

/**
 * 실시간 표본 통계량(Statistic) 계산 클래스
 * 
 * @author jmsohn
 */
public class RTStatistic {
	
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
	
	/**
	 * 생성자
	 */
	public RTStatistic() {
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
	public RTStatistic(double sum, double squaredSum, double cubedSum, double fourthPoweredSum, int count) throws Exception {
		
		// 입력값 검증
		if(count <= 0) {
			throw new IllegalArgumentException("count must be greater than 0:" + count); 
		}
		
		//---- 데이터 개수 설정
		this.count = count;
		
		//---- 통계값 계산을 위한 설정
		this.sum = sum;
		this.squaredSum = squaredSum;
		this.cubedSum = cubedSum;
		this.fourthPoweredSum = fourthPoweredSum;
		
		//---- 설정된 값으로 표본 통계량을 계산
		this.calStatistic();
	}

	/**
	 * 통계량 초기화
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
		
		//---- 표본 통계량 계산을 위한 값 설정
		this.sum += value;
		this.squaredSum += squaredValue;
		this.cubedSum += cubedValue;
		this.fourthPoweredSum += fourthPoweredValue;
		
		//---- 설정된 값으로 표본 통계량을 계산
		this.calStatistic();
	}
	
	/**
	 * 객체에 설정된 값으로 통계량 계산
	 */
	private void calStatistic() {
		
		//---- 데이터 초기화
		double n = (double)this.count;
		
		//---- 표본 통계량 계산 수행
		this.mean = this.sum/n;
		double squaredMean = this.mean * this.mean;
		double cubedMean = squaredMean * this.mean;
		double fourthPoweredMean = cubedMean * this.mean;

		//---- 분산 계산
		if(n < 2) {
			this.variance = 0.0;
		} else {
			this.variance = (this.squaredSum - (squaredMean * n)) / (n - 1);
		}
		
		//---- 왜도 계산
		if(n < 3) {
			this.skewness = 0.0;
		} else {
			
			this.skewness =
				(n / ((n - 1) * (n - 2)))
				* (
					this.cubedSum
					- (cubedMean * n)
					- (3 * this.mean * this.variance * (n - 1))
				)
				/ Math.pow(this.variance, 3.0/2.0);
		}
		
		//---- 첨도 계산
		if(n < 4) {
			this.kurtosis = 0.0;
		} else {
			
			this.kurtosis =
				(
					(n * (n + 1) / ((n - 1) * (n - 2) * (n - 3)))
					* (
						this.fourthPoweredSum
						- (4 * this.mean * this.cubedSum)
						+ (6 * squaredMean * this.variance * (n - 1))
						+ (3 * fourthPoweredMean * n)
					)
		  		)
				/ (this.variance * this.variance)
				- (3 * (n - 1) * (n - 1)) / ((n - 2) * (n - 3));
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
