package com.jutools;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.jutools.nodeexporter.NodeMetric;
import com.jutools.nodeexporter.NodeMetrics;
import com.jutools.nodeexporter.NodeMetricsBuilder;

public class NodeExporterTest {

	@Test
	public void test() throws Exception {
		
		String metricsStr = "go_gc_duration_seconds{quantile=\"1\"} 0";
		
		NodeMetrics metrics = NodeMetricsBuilder.build(metricsStr);
		
		for(NodeMetric metric: metrics) {
			System.out.println(metric.getType());
			System.out.println(metric.getAttr("quantile"));
			System.out.println(metric.getValue());
		}
	}
}
