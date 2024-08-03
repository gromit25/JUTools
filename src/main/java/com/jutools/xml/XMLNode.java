package com.jutools.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jutools.StringUtil;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * XML 노드 클래스
 * 
 * @author jmsohn
 */
public class XMLNode {
	
	/** DOM의 node 객체 */
	@Getter(AccessLevel.PACKAGE)
	private Element node;
	
	/**
	 * 생성자
	 * 
	 * @param node
	 */
	public XMLNode(Node node) throws Exception {
		
		if(node == null) {
			throw new NullPointerException("node is null.");
		}
		
		if (node.getNodeType() != Node.ELEMENT_NODE) {
			throw new IllegalArgumentException("node type is not element node:" + node.getNodeName());
		}
		
		this.node = (Element)node;
	}
	
	/**
	 * 현재 노드의 하위 노드를 쿼리로 조회하여 결과 반환
	 * 
	 * @param query 조회 쿼리
	 * @return 조회 결과 
	 */
	public XMLArray select(String query) throws Exception  {

		// 입력값 검증
		if(query == null) {
			throw new IllegalArgumentException("query is null.");
		}
		
		// 조회 결과를 담는 XML 목록 변수
		XMLArray nodes = new XMLArray();
		
		// ------------- 1. query의 node matcher 객체 생성 ----------------
		
		// 쿼리를 나눔
		// ex) query : "test1 > test2 > test3(attr1='what')"
		//     -> "test1", "test2 > test3(attr1='what')"
		//
		// 첫번째 "test1" 은 현재 노드의 하위 노드 중 일치하는 것을 찾기 위함
		// "test2 > test3(attr1='what')"는 다시 하위 노드에 select 메소드에 호출함
		//
		String[] splited = StringUtil.splitFirst(query, "\\s*>\\s*");
		
		// 노드가 query에 적합한지 검사하는 객체 생성
		NodeMatcher matcher = new NodeMatcher(splited[0]);
		
		// ------------- 2. 목록에서 검색할 범위의 시작점과 종료점 추출 ----------------
		
		// child node의 목록을 가져옴
		NodeList childs = this.node.getChildNodes();
		
		// 범위 시작점 - query에 설정된 시작점을 가져옴
		int start = matcher.getStart();
		
		// 범위 종료점 - query에 설정된 종료점을 가져옴
		// 종료점이 현재 배열 보다 클 경우 배열의 끝을 종료점으로 설정함
		int end = matcher.getEnd();
		
		if(end > childs.getLength()) {
			end = childs.getLength();
		}
		
		// ------------- 3. 주어진 query에 따라 노드 추출 ----------------
		
		// 현재 노드의 하위 노드 중 query에 일치하는 노드를 검색
		int elementIndex = 0;
		for(int index = 0; index < childs.getLength(); index++) {
			
			// 하위 노드를 가져옴
			Node childNode = childs.item(index);
			if(childNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			// element 노드의 index가 범위 내에 있는지 검사
			if(elementIndex < start) {
				
				// element 노드 index를 하나 올림
				elementIndex++;

				// 범위가 시작 되지 않으면 다음 child 노드를 검색
				continue;
			}
			
			if(elementIndex > end) {
				// 범위 바깥으로 나가면 for 문 종료함
				break;
			}
			
			// element 노드 index를 하나 올림
			elementIndex++;
			
			// query와 일치하는지 확인
			// 일치하지 않으면 다음 하위노드 검색
			if(matcher.match(childNode) == false) {
				continue;
			}
			
			// 하위 노드의 XML 노드를 생성 
			XMLNode xmlChildNode = new XMLNode(childNode);
			
			// 만일 query가 마지막 query(splited.length ==1) 이면,
			// -> 현재 노드를 결과에 추가함
			// query가 마지막이 아니면,
			// -> 하위 노드에 query를 수행 후
			//    수행 결과를 XML 결과 목록에 모두 추가
			if(splited.length == 1) {
				nodes.add(xmlChildNode);
			} else {
				nodes.addAll(xmlChildNode.select(splited[1]));
			}
		}
		
		// query에 적합한 node 목록 반환
		return nodes;
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
	 * 노드의 테그명 반환
	 * 
	 * @return 노드의 테그명
	 */
	public String getTagName() throws Exception {
		return this.node.getNodeName();
	}
	
	/**
	 * 노드의 속성 값 반환
	 * 
	 * @param attrName 속성 명
	 * @return 속성 값
	 */
	public String getAttributeValue(String attrName) throws Exception {
		return this.node.getAttribute(attrName);
	}
	
	/**
	 * 노드의 속성 이름 목록 반환
	 * 
	 * @return 노드의 속성 이름 목록
	 */
	public String[] getAttributeNames() throws Exception {
		
		// 속성 목록을 가져옴
		NamedNodeMap attrMap = this.node.getAttributes();
		
		// 속성 목록에서 속성명을 가져와
		// 목록에 추가함
		String[] attrNames = new String[attrMap.getLength()];
		
		for(int index = 0; index < attrMap.getLength(); index++) {
			
			Node attr = attrMap.item(index);
			attrNames[index] = attr.getNodeName(); 
		}
		
		return attrNames;
	}
	
	/**
	 * 노드의 텍스트를 반환
	 * 
	 * @return 노드의 텍스트
	 */
	public String getText() throws Exception {
		return this.node.getTextContent();
	}
	
	/**
	 * 노드의 상위 XML 노드 반환
	 * 
	 * @return 상위 XML 노드
	 */
	public XMLNode getParent() throws Exception {
		
		Node parent = this.node.getParentNode();
		
		if(parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
			return new XMLNode(parent);
		} else {
			return null;
		}
	}
	
	/**
	 * 노드의 하위 XML 노드 목록 반환
	 * 
	 * @return 하위 XML 노드 목록
	 */
	public XMLArray getChilds() throws Exception {
		return this.select("*");
	}
	
	/**
	 * XML 노드 객체의 정보를 문자열로 변환 
	 * 
	 * @return XML 노드 객체 문자열
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
	 * "tagname.attr~mappingname@type=default"
	 * 
	 * @param mappingSpecs
	 * @return
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
			String[] specAndMappingName = StringUtil.splitLast(specAndType[0], "\\~");
			String mappingName = (specAndMappingName.length == 2)?specAndMappingName[1].trim():null;
			
			//
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
