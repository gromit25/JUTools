package com.jutools.stat;

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
	
	/** 최소 값 */
	@Getter
	private double min;
	/** 최대 값 */
	@Getter
	private double max;
	
	/**
	 * 생성자
	 */
	public RTStatistic() {
		this.reset();
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
		double n = (double)this.count;
		
		//---- 표본 통계치 계산을 위한 값 설정
		this.sum += value;
		this.squaredSum += squaredValue;
		this.cubedSum += cubedValue;
		this.fourthPoweredSum += fourthPoweredValue;
		
		//---- 모수 계산 수행
		this.mean = this.sum/n;
		double squaredMean = this.mean * this.mean;
		double cubedMean = squaredMean * this.mean;
		double fourthPoweredMean = cubedMean * this.mean;

		//---- 분산 계산
		if(this.count < 2) {
			
			this.variance = 0.0;
			
		} else {
			
			this.variance = (this.squaredSum - (squaredMean * n)) / (n - 1);
			
		}
		
		//---- 왜도 계산
		if(this.count < 3) {
			
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
		if(this.count < 4) {
			
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
		
		//---- 최대값, 최소값 계산
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
