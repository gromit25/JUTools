package com.jutools.stat;

import com.jutools.StatUtil;

import lombok.Data;
import lombok.Getter;

/**
 * 통계량(statistic) 클래스
 * 
 * @author jmsohn
 */
@Data
public class StatisticRT {
	
	/** 합계 */
	private double sum;
	/** 제곱합(표준편차 계산용) */
	private double squaredSum;
	/** 데이터의 개수 */
	@Getter
	private int count;
	/** 최소 값 */
	@Getter
	private double min;
	/** 최대 값 */
	@Getter
	private double max;
	
	/**
	 * 생성자
	 */
	public StatisticRT() {
		
		this.sum = 0.0;
		this.squaredSum = 0.0;
		this.count = 0;
		
		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
	}
	
	/**
	 * 데이터 추가
	 * 
	 * @param value 추가할 데이터
	 * @param squaredValue 추가할 데이터의 제곱값
	 */
	void add(double value) {
		
		double squaredValue = value * value;
		
		synchronized(this) {
			
			this.sum += value;
			this.squaredSum += squaredValue;
			this.count++;
			
			if(this.min > value) {
				this.min = value;
			}
			
			if(this.max < value) {
				this.max = value;
			}
		}
	}
	
	/**
	 * 실시간 표준 편차 반환
	 * 
	 * @return 실시간 표준 편차
	 */
	public double std() {
		return StatUtil.std(this.sum, this.squaredSum, this.count); 
	}
	
	/**
	 * 실시간 분산 값 반환
	 * 
	 * @return 실시간 분산 값
	 */
	public double variance() {
		return StatUtil.variance(this.sum, this.squaredSum, this.count);
	}
	
	/**
	 * 현재 객체를 json 문자열 형태로 반환
	 */
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder("");
		
		builder
			.append("{").append("\r\n")
			.append("\"sum\":").append(this.sum).append("\r\n")
			.append("\"squaredSum\":").append(this.squaredSum).append("\r\n")
			.append("\"count\":").append(this.count).append("\r\n")
			.append("}");
		
		return builder.toString();
	}
}