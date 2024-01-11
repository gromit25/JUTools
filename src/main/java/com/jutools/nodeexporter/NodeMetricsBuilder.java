package com.jutools.nodeexporter;

import java.io.LineNumberReader;
import java.util.List;

import com.jutools.StringUtil;
import com.jutools.nodeexporter.instructions.NodeMetricInstruction;
import com.jutools.nodeexporter.parser.TypeParser;

/**
 * Node Exporter의 Metrics을 생성하는 Builder 클래스
 * 
 * @author jmsohn
 */
public class NodeMetricsBuilder {
	
	/**
	 * 주어진 Node Exporter의 metrics 문자열로 metric 목록 생성 및 반환
	 * 
	 * @param metricsStr Node Exporter의 metrics 문자열
	 * @return 생성된 metric 목록
	 */
	public static NodeMetrics build(String metricsStr) throws Exception {
		
		// metrics 문자열 reader 객체 생성
		LineNumberReader reader = new LineNumberReader(StringUtil.newReader(metricsStr));
		
		// 파싱된 metric 정보 목록 변수
		NodeMetrics metrics = new NodeMetrics();
		
		// 한줄씩 읽어 파싱하여 metric 객체를 생성 및 metrics에 추가
		String read = null;
		while((read = reader.readLine()) != null) {
			
			// 만일 "#"으로 문자열이 시작할 경우 주석이므로 다음 라인을 읽음
			if(StringUtil.isBlank(read) == true || read.startsWith("#") == true) {
				continue;
			}
			
			// 문자열을 파싱하여 metric 객체 생성 및 추가
			metrics.add(makeMetric(read));
		}
		
		return metrics;
	}
	
	/**
	 * metric 정보 문자열을 파싱하여 metric 정보 객체 생성 및 반환하는 메소드
	 * 
	 * @param metricStr metric 정보 문자열
	 * @return 생성된 metric 정보 객체
	 */
	private static NodeMetric makeMetric(String metricStr) throws Exception {
		
		NodeMetric metric = null;
		
		// metric 정보 문자열을 파싱하여 metric 정보 객체 생성 명령어 목록 반환
		List<NodeMetricInstruction> insts = new TypeParser().parse(metricStr).travelPostOrder();
		
		// metric 정보 객체 생성 명령어를 하나씩 수행
		for(NodeMetricInstruction inst: insts) {
			metric = inst.execute(metric);
		}
		
		// 생성된 metric 정보 객체 반환
		return metric;
	}
}
