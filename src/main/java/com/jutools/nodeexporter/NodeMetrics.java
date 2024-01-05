package com.jutools.nodeexporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author jmsohn
 */
public class NodeMetrics implements Iterable<NodeMetric>{
	
	/** */
	private List<NodeMetric> metrics;
	
	/**
	 * 
	 */
	NodeMetrics() {
		this.metrics = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param metric
	 * @return
	 */
	NodeMetrics add(NodeMetric metric) throws Exception {
		
		if(metric == null) {
			throw new IllegalArgumentException("metric is null.");
		}
		
		this.metrics.add(metric);
		
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return this.metrics.size();
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public NodeMetric get(int index) throws Exception {
		
		if(index < 0) {
			throw new IllegalArgumentException("index must be greater than 0:" + index);
		}
		
		if(index >= this.size()) {
			throw new IllegalArgumentException("index must be less than " + this.size() + ":" + index);
		}
		
		return this.metrics.get(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public double sumValue() {
		
		double sum = 0.0;
		
		for(NodeMetric metric: this.metrics) {
			sum += metric.getValue();
		}
		
		return sum;
	}

	@Override
	public Iterator<NodeMetric> iterator() {
		return this.metrics.iterator();
	}
}
