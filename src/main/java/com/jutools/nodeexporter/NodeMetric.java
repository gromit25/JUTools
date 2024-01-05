package com.jutools.nodeexporter;

import java.util.HashMap;
import java.util.Map;

import com.jutools.StringUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * Node Exporter의 Metric 정보 객체
 * 
 * @author jmsohn
 */
public class NodeMetric {
	
	/** node exporter의 type */
	@Getter
	@Setter
	private String type;
	
	/** node exporter의 속성 값 목록 */
	private Map<String, String> attrs;
	
	/** node exporter의 value */
	@Getter
	@Setter
	private double value;
	
	/**
	 * 생성자
	 * 
	 * @param type node exporter의 type
	 */
	public NodeMetric(String type) throws Exception {
		if(StringUtil.isBlank(type) == true) {
			throw new Exception("type is blank or null.");
		}
		
		this.setType(type);
		this.attrs = new HashMap<>();
	}
	
	/**
	 * 속성 추가
	 * 
	 * @param attrName type의 속성명
	 * @param attrValue type의 속성값
	 * @return 현재 객체
	 */
	public NodeMetric putAttr(String attrName, String attrValue) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isBlank(attrName) == true) {
			throw new Exception("attribute name is blank or null.");
		}
		
		// 속성 추가
		this.attrs.put(attrName, attrValue);
		
		// 현재 객체 반환
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param attrName
	 * @return
	 */
	public String getAttr(String attrName) throws Exception {
		
		if(attrName == null) {
			throw new IllegalArgumentException("attribute name is null.");
		}
		
		if(this.containsAttr(attrName) == false) {
			throw new IllegalArgumentException("attribute name is not found:" + attrName);
		}
		
		return this.attrs.get(attrName);
	}
	
	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public boolean containsAttr(String attrName) {
		
		if(this.attrs == null) {
			return false;
		}
		
		return this.attrs.containsKey(attrName);
	}
}
