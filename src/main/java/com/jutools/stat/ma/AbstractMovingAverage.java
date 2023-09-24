package com.jutools.stat.ma;

import java.util.LinkedList;
import java.util.Queue;

import lombok.Getter;

/**
 * 
 * 
 * @author jmsohn
 */
public abstract class AbstractMovingAverage {
	
	/** */
	@Getter
	private int windowSize;
	
	/** */
	@Getter
	private double average;
	
	/** */
	@Getter
	private Queue<Double> queue = new LinkedList<Double>();
	
	/**
	 * 
	 * @param windowSize
	 */
	public AbstractMovingAverage(int windowSize) throws Exception {
		
		if(windowSize < 1) {
			throw new IllegalArgumentException("window size must be greater than 0:" + windowSize);
		}
		
		this.windowSize = windowSize;
	}
	
	/**
	 * 
	 * @param value
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
	 * 
	 * @param out
	 * @return
	 */
	protected abstract double calculate(double in, double out);
	
	/**
	 * 
	 */
	public void clear() {
		if(this.queue != null) {
			this.queue.clear();
		}
	}

}
