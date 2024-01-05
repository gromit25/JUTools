package com.jutools.nodeexporter.instructions;

import com.jutools.StringUtil;
import com.jutools.nodeexporter.NodeMetric;

import lombok.Getter;
import lombok.Setter;

/**
 * Node Export Metric에 value 항목을 설정하는 명령어
 * 
 * @author jmsohn
 */
public class SET_VALUE extends NodeMetricInstruction {
	
	/** Node Export Metric의 value 값 */
	@Getter
	@Setter
	private String value;
	
	/**
	 * 생성자 
	 * 
	 * @param value Node Export Metric의 value 값
	 */
	public SET_VALUE(String value) throws Exception {
		
		if(StringUtil.isBlank(value) == true) {
			throw new Exception("value is blank or null.");
		}
		
		this.value = value;
	}

	@Override
	public NodeMetric execute(NodeMetric nodeMetric) throws Exception {
		
		// 주어진 Node Export Metric에 value 값 설정
		nodeMetric.setValue(Double.parseDouble(this.value));
		return nodeMetric;
	}
}
