package com.jutools.nodeexporter.instructions;

import com.jutools.StringUtil;
import com.jutools.nodeexporter.NodeMetric;

import lombok.Getter;
import lombok.Setter;

/**
 * Node Exporter Metric 클래스를 생성하는 명령어
 * 
 * @author jmsohn
 */
public class MAKE_METRIC extends NodeMetricInstruction {
	
	/** Node Exporter Metric의 Type값 */
	@Setter
	@Getter
	private String type;
	
	/**
	 * 생성자
	 * 
	 * @param type Node Exporter Metric의 Type값
	 */
	public MAKE_METRIC(String type) throws Exception {
		
		if(StringUtil.isBlank(type) == true) {
			throw new Exception("type is blank or null.");
		}
		
		this.type = type;
	}

	@Override
	public NodeMetric execute(NodeMetric nodeMetric) throws Exception {
		
		// 새로운 Node Exporter Metric 클래스 생성
		return new NodeMetric(this.type);
	}
}
