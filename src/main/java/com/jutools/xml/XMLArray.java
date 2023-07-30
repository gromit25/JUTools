package com.jutools.xml;

import java.util.Iterator;
import java.util.Vector;

/**
 * XML 노드 목록 클래스
 * 
 * @author jmsohn
 */
public class XMLArray implements Iterable<XMLNode> {
	
	/** XML 노드 목록 객체 */
	private Vector<XMLNode> nodes;
	
	/**
	 * 생성자
	 * 
	 * @param nodes 노드 배열 객체
	 */
	XMLArray(XMLNode[] nodes) throws Exception {
		
		if(nodes == null) {
			throw new NullPointerException("nodes is null.");
		}
		
		this.nodes = new Vector<>(nodes.length);
		for(XMLNode node: nodes) {
			
			this.nodes.add(node);
		}
	}
	
	/**
	 * 생성자
	 */
	XMLArray() throws Exception {
		this(new XMLNode[] {});
	}

	@Override
	public Iterator<XMLNode> iterator() {
		return this.nodes.iterator();
	}
	
	/**
	 * XML 목록에 조회 query 결과를 반환
	 * 
	 * @param query 조회 query
	 * @return query 결과 반환
	 */
	public XMLArray select(String query) throws Exception {
		
		XMLArray nodes = new XMLArray();
		
		for(XMLNode node: this.nodes) {
			nodes.addAll(node.select(query));
		}
		
		return nodes;
	}
	
	/**
	 * 부모 XML 노드 객체 목록 반환
	 * 
	 * @return 부모 XML 노드 객체 목록
	 */
	public XMLArray getParents() throws Exception {
		
		XMLArray parents = new XMLArray();
		
		for(XMLNode node: this.nodes) {
			
			XMLNode parent = node.getParent();
			
			if(node.getParent() != null) {
				parents.add(parent);
			}
		}
		
		return parents;
	}
	
	/**
	 * XML 목록에 XML 노드를 하나 추가
	 * 
	 * @param node 추가할 XML 노드
	 */
	void add(XMLNode node) throws Exception {
		this.nodes.add(node);
	}
	
	/**
	 * 현재 XML목록에 다른 XML 목록을 추가
	 * 
	 * @param nodes 추가할 XML 목록
	 */
	void addAll(XMLArray nodes) throws Exception {
		this.nodes.addAll(nodes.nodes);
	}
	
	/**
	 * XML 목록에서 특정 인덱스의 XML 노드를 반환 
	 * 
	 * @param index 가져올 인덱스
	 * @return XML 노드 객체
	 */
	public XMLNode get(int index) throws Exception {
		return this.nodes.get(index);
	}
	
	/**
	 * XML 노드 목록의 첫번째 XML 노드를 반환 
	 * 
	 * @return 첫번째 XML 노드
	 */
	public XMLNode getFirst() {
		if(this.nodes == null || this.nodes.size() < 1) {
			return null;
		} else {
			return this.nodes.elementAt(0);
		}
	}
	
	/**
	 * XML 노드 목록 크기를 반환
	 * 
	 * @return XML 노드 모록 크기
	 */
	public int size() {
		if(this.nodes == null) {
			return 0;
		} else {
			return this.nodes.size();
		}
	}

}
