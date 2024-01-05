package com.jutools.nodeexporter.instructions;

import com.jutools.StringUtil;
import com.jutools.nodeexporter.NodeMetric;

import lombok.Getter;
import lombok.Setter;

/**
 * Node Exporter Metric에 속성을 추가하는 명령어
 * 
 * @author jmsohn
 */
public class ADD_ATTR extends NodeMetricInstruction {
	
	/** Node Exporter Metric 추가할 속성명 */
	@Getter
	@Setter
	private String attrName;
	
	/** Node Exporter Metric 추가할 속성값 */
	@Getter
	@Setter
	private String attrValue;
	
	/**
	 * 생성자
	 * 
	 * @param attrName Node Exporter Metric 추가할 속성명
	 * @param attrValue Node Exporter Metric 추가할 속성값
	 */
	public ADD_ATTR(String attrName, String attrValue) throws Exception {
		
		if(StringUtil.isBlank(attrName) == true) {
			throw new Exception("attribute name is blank or null.");
		}
		
		this.attrName = attrName;
		this.attrValue = attrValue;
	}

	@Override
	public NodeMetric execute(NodeMetric nodeMetric) throws Exception {
		
		// Node Exporter Metric 속성 추가
		return nodeMetric.putAttr(attrName, attrValue);
	}
}
