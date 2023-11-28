package com.jutools.stat.ma;

import java.util.LinkedList;
import java.util.Queue;

import lombok.Getter;

/**
 * Moving Average 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractMovingAverage {
	
	/** Moving Average 구간 */
	@Getter
	private int windowSize;
	
	/** Moving Average */
	@Getter
	private double average;
	
	/** 구간 데이터 저장소 */
	@Getter
	private Queue<Double> queue = new LinkedList<Double>();
	
	/**
	 * 생성자
	 * 
	 * @param windowSize Moving Average 구간
	 */
	public AbstractMovingAverage(int windowSize) throws Exception {
		
		if(windowSize < 1) {
			throw new IllegalArgumentException("window size must be greater than 0:" + windowSize);
		}
		
		this.windowSize = windowSize;
	}
	
	/**
	 * 데이터 추가 및 Moving Average 실시간 계산
	 * 
	 * @param value 추가할 데이터
	 */
	public void add(double value) {
		
		this.queue.add(value);
		
		double out = Double.NaN;
		if(this.queue.size() > this.windowSize) {
			out = this.queue.poll();
		}

		this.average = this.calculate(value, out);
	}
	
	/**
	 * Moving Average 계산
	 * 
	 * @param in 구간 데이터에 신규 입력되는 값
	 * @param out 구간 데이터에서 삭제되는 값
	 * @return Moving Average 값
	 */
	protected abstract double calculate(double in, double out);
	
	/**
	 * 구간 데이터 삭제 
	 */
	public void clear() {
		if(this.queue != null) {
			this.queue.clear();
		}
	}

}
