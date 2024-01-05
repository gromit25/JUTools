package com.jutools.nodeexporter.instructions;

import com.jutools.nodeexporter.NodeMetric;

/**
 * Node Exporter의 Metric 객체를 생성하기 위한 명령어의 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class NodeMetricInstruction {

	/**
	 * 명령어 실행 메소드
	 * 
	 * @param nodeMetric Node Exporter의 Metric 객체
	 * @return 처리된 Node Exporter의 Metric 객체
	 */
	public abstract NodeMetric execute(NodeMetric nodeMetric) throws Exception;
}
