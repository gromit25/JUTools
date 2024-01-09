package com.jutools.nodeexporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Node Exporter의 Metrics 클래스
 * 
 * @author jmsohn
 */
public class NodeMetrics implements Iterable<NodeMetric>{
	
	/** metrics 목록 */
	private List<NodeMetric> metrics;
	
	/**
	 * 생성자
	 */
	NodeMetrics() {
		this.metrics = new ArrayList<>();
	}
	
	/**
	 * metric 추가
	 * 
	 * @param metric 추가할 metric
	 * @return 현재 객체
	 */
	NodeMetrics add(NodeMetric metric) throws Exception {
		
		if(metric == null) {
			throw new IllegalArgumentException("metric is null.");
		}
		
		this.metrics.add(metric);
		
		return this;
	}
	
	/**
	 * metrics의 개수 반환
	 * 
	 * @return metrics의 개수
	 */
	public int size() {
		return this.metrics.size();
	}
	
	/**
	 * 특정 인덱스의 metric 반환
	 * 
	 * @param index 인덱스
	 * @return 특정 인덱스의 metric
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
	 * metrics의 value 합 반환
	 * 
	 * @return metrics의 value 합
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
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	public NodeMetrics find(String condition) throws Exception {
		return null;
	}
}
