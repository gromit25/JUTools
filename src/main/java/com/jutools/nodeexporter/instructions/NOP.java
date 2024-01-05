package com.jutools.nodeexporter.instructions;

import com.jutools.nodeexporter.NodeMetric;

/**
 * No operation 명령어
 * 
 * @author jmsohn
 */
public class NOP extends NodeMetricInstruction {

	@Override
	public NodeMetric execute(NodeMetric nodeMetric) throws Exception {
		// do nothing
		return nodeMetric;
	}

}
