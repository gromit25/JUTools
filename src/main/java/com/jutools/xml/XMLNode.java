package com.jutools.xml;

import java.util.HashMap;
import java.util.Map;

import com.jutools.StringUtil;
import com.jutools.xml.typeshift.TypeShift;
import com.jutools.xml.typeshift.TypeShiftManager;

/**
 * XML 노드 클래스<br>
 * 테그명, 속성명, 속성값, 노드 텍스트, 테일 텍스트로 구성됨<br>
 * 노드 텍스트는 현재 노드의 텍스트이며,<br>
 * 테일 텍스트는 노드 종료이후 텍스트임<br>
 * <br>
 * 예를 들어, &lt;text&gt; hi &lt;p&gt;name&lt;/p&gt;!&lt;/text&gt;일 경우<br>
 * p 노드의 노드 텍스트는 "name"이 되고 테일 텍스트는 "!" 이 됨<br>
 * text 노드의 노드 텍스트는 "hi " 이 되고 테일 텍스트는 ""이 됨
 * 
 * @author jmsohn
 */
public class XMLNode {
	
	/** 노드 테그명 */
	private String tagName;
	/** 노드 속성 */
	private Map<String, String> attrMap = new HashMap<>();
	/** 노드 텍스트 */
	private String text;
	/** 테일 텍스트 */
	private String tail;
	
	/** 부모 노드 */
	private XMLNode parent;
	/** 자식 노드 목록 */
	private XMLArray childs = new XMLArray();
	
	/**
	 * 생성자 - 패키지 외부에서 생성못하게함
	 */
	XMLNode() {		
	}
	
	/**
	 * 현재 노드의 하위 노드를 쿼리로 조회하여 결과 반환
	 * 
	 * @param query 조회 쿼리
	 * @return 조회 결과 
	 */
	public XMLArray select(String query) throws Exception  {
		return NodeMatcher.create(query).match(this);
	}
	
	/**
	 * 현재 노드의 하위 노드를 쿼리로 조회 후 첫번째 결과 반환
	 * 
	 * @param query 조회 쿼리
	 * @return 조회된 XML 노드
	 */
	public XMLNode selectFirst(String query) throws Exception{
		
		XMLArray nodes = this.select(query);
		
		if(nodes.size() >= 1) {
			return nodes.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 노드 테그명 반환
	 * 
	 * @return 노드 테그명
	 */
	public String getTagName() {
		return this.tagName;
	}
	
	/**
	 * 노드 테그명 설정 - 패키지 외부에서는 설정하지 못하도록 함
	 * 
	 * @param tagName 설정할 노드 테그명
	 */
	void setTagName(String tagName) throws Exception {
		
		if(StringUtil.isBlank(tagName) == true) {
			throw new Exception("tag name is null or blank.");
		}
		
		this.tagName = tagName;
	}
	
	/**
	 * 노드의 속성 값 반환
	 * 
	 * @param attrName 속성 명
	 * @param attrName 속성이 없을 경우 default 값
	 * @return 속성 값
	 */
	public String getAttributeValue(String attrName, String defaultValue) {
		
		if(this.attrMap.containsKey(attrName) == true) {
			return this.attrMap.get(attrName);
		} else {
			return defaultValue;
		}
	}
	
	/**
	 * 노드의 속성 값 반환<br>
	 * 속성 명이 없을 경우 null 을 반환
	 * 
	 * @param attrName 속성 명
	 * @return 속성 값
	 */
	public String getAttributeValue(String attrName) {
		return this.getAttributeValue(attrName, null);
	}
	
	/**
	 * 노드 속성 값 설정 - 패키지 외부에서는 설정하지 못하도록 함
	 * 
	 * @param attrName 속성 명
	 * @param attrValue 속성 값
	 */
	void setAttributeValue(String attrName, String attrValue) throws Exception {
		
		if(StringUtil.isBlank(attrName) == true) {
			throw new Exception("attribute name is null or blank.");
		}
		
		if(attrValue == null) {
			throw new Exception("attribute value is null.");
		}
		
		this.attrMap.put(attrName, attrValue);
	}
	
	/**
	 * 노드의 속성 이름 목록 반환
	 * 
	 * @return 노드의 속성 이름 목록
	 */
	public String[] getAttributeNames() throws Exception {
		
		return this.attrMap.keySet().toArray(new String[0]);
	}
	
	/**
	 * 노드 텍스트 반환
	 * 
	 * @return 노드 텍스트
	 */
	public String getText() {
		
		if(this.text == null) {
			this.text = "";
		}
		
		return this.text;
	}
	
	/**
	 * 노드 텍스트 설정 - 패키지 외부에서는 설정하지 못하도록 함
	 * 
	 * @param text 설정할 노드 텍스트
	 */
	void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 테일 텍스트 반환
	 * 
	 * @return 테일 텍스트
	 */
	public String getTail() {
		
		if(this.tail == null) {
			this.tail = "";
		}
		
		return this.tail;
	}
	
	/**
	 * 테일 텍스트 설정 - 패키지 외부에서는 설정하지 못하도록 함
	 * 
	 * @param tail 설정할 테일 텍스트
	 */
	void setTail(String tail) {
		this.tail = tail;
	}
	
	/**
	 * 부모 노드 반환
	 * 
	 * @return 부모 노드
	 */
	public XMLNode getParent() {
		return this.parent;
	}
	
	/**
	 * 부모 노드 설정 - 패키지 외부에서는 설정하지 못하도록 함
	 * 
	 * @param parent 설정할 부모 노드
	 */
	void setParent(XMLNode parent) throws Exception {
		
		this.parent = parent;
		
		if(this.parent != null) {
			this.parent.addChild(this);
		}
	}
	
	/**
	 * 자식 노드 목록 반환
	 * 
	 * @return 자식 노드 목록
	 */
	public XMLArray getChilds() {
		return this.childs;
	}
	
	/**
	 * 자식 노드 추가
	 * 
	 * @param node 자식 노드
	 */
	void addChild(XMLNode node) throws Exception {
		
		if(node == null) {
			throw new Exception("node is null.");
		}
		
		// 자식 노드 목록에 추가
		this.childs.add(node);
		
		// 자식노드의 부모 노드를 현재 노드로 설정
		node.parent = this;
	}
	
	/**
	 * 노드 객체의 정보를 문자열로 변환 
	 * 
	 * @return 노드 객체 문자열
	 */
	@Override
	public String toString() {
		
		// 객체 정보를 문자열로 변환을 위한 임시 버퍼 변수
		StringBuilder builder = new StringBuilder();
		
		try {
			
			// 테그명 출력
			builder.append("TAG: ").append(this.getTagName());
			
			// 각 속성 출력
			for(String attrName: this.getAttributeNames()) {
				
				// 속성별 속성값 가져옴
				String attrValue = this.getAttributeValue(attrName);
				
				// 속성 출력
				builder.append("\t").append(attrName).append(": ").append(attrValue).append("\n");
			}
			
		} catch(Exception ex) {
			
			// 예외 발생시 오류 메시지 반환
			return "Exception Occured:" + ex.getMessage();
		}
		
		// 변환된 문자열 반환
		return builder.toString();
	}
	
	/**
	 * 맵핑 스펙에 따라 노드의 내용을 Map 객체로 생성 및 반환<br>
	 * 맵핑 스팩: "tagname.attr->mappingname@type=default"<br>
	 * 하위 테그 텍스트 또는 속성을 mappingname으로 type 변환하여 추가함<br>
	 * 만일, 해당 테그 텍스트나 속성이 없는 경우 default 값을 사용함
	 * 
	 * @param mappingSpecs 맵핑 스펙 목록
	 * @return 스펙에 따라 생성된 Map 객체
	 */
	public Map<String, Object> toMap(String... mappingSpecs) throws Exception {
		
		// mapping한 데이터가 들어갈 map 변수
		Map<String, Object> map = new HashMap<>();
		
		// 각 mapping spec 별로 처리 수행
		for(String mappingSpec: mappingSpecs) {
			
			// mapping spec 이 비어 있는 경우 다음 것을 처리함
			if(StringUtil.isBlank(mappingSpec) == true) {
				continue;
			}
			
			// -------- spec 파싱 --------
			// 뒤에서 부터 파싱을함
			
			// default 값 설정
			String[] specAndDefault = StringUtil.splitLast(mappingSpec, "\\=");
			String defaultValue = (specAndDefault.length == 2)?specAndDefault[1]:null;
			
			// type 설정
			String[] specAndType = StringUtil.splitLast(specAndDefault[0], "\\@");
			String typeName = (specAndType.length == 2)?specAndType[1].trim():"String";
			TypeShift typeShift = TypeShiftManager.getTypeShift(typeName);
			
			// mapping 명
			String[] specAndMappingName = StringUtil.splitLast(specAndType[0], "\\->");
			String mappingName = (specAndMappingName.length == 2)?specAndMappingName[1].trim():null;
			
			// 테그명과 속성 명 분리
			String[] tagAndAttr = StringUtil.splitLast(specAndMappingName[0], "\\.");
			String attrName = (tagAndAttr.length == 2)?tagAndAttr[1].trim():"#text";
			String tagName = tagAndAttr[0].trim();
			
			// mapping 명이 null 일 경우 디폴트 값 설정
			if(mappingName == null) {
				mappingName = tagName + "." + attrName;
			}
			
			// -------- map에 데이터 추가 --------
			
			// 데이터를 읽어옴
			String value = null;
			
			XMLNode selectedNode = this.selectFirst(tagName);
			if(selectedNode != null) {
				if(attrName.equalsIgnoreCase("#text") == true) {
					value = selectedNode.getText();
				} else {
					value = selectedNode.getAttributeValue(attrName);
				}
			}
			
			// 디폴트 값 설정
			if(value == null && defaultValue != null) {
				value = defaultValue;
			}
			
			// 데이터 변환 및 저장
			if(value != null) {
				typeShift.setValue(map, mappingName, value);
			} else {
				map.put(mappingName, null);
			}
			
		} // End of mappingSpecs
		
		return map;
	}
}
