package com.jutools.xml;

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
		
		// 현재 노드의 하위 노드 중 query에 일치하는 노드를 검색
		NodeList childs = this.node.getChildNodes();
		for(int index = 0; index < childs.getLength(); index++) {
			
			// 하위 노드를 가져옴
			Node childNode = childs.item(index);
			if(childNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
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
	public String getAttribute(String attrName) throws Exception {
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
}
