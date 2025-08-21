package com.jutools.xml;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * XML 노드 목록 클래스
 * 
 * @author jmsohn
 */
public class XMLArray implements Iterable<XMLNode> {
	
	/** XML 노드 목록 객체 */
	private List<XMLNode> nodes = new Vector<>();
	
	
	/**
	 * 생성자
	 */
	XMLArray() {
	}

	@Override
	public Iterator<XMLNode> iterator() {
		return this.nodes.iterator();
	}
	
	/**
	 * XML 목록에서 주어진 조회 query 에 적합한 결과를 반환
	 * 
	 * @param query 조회 query
	 * @return query에 적합한 노드 목록 
	 */
	public XMLArray select(String query) throws Exception {
		return NodeMatcher.create(query).match(this);
	}
	
	/**
	 * 하위 노드를 Stream 으로 반환
	 *
	 * @return 하위 노드 Stream
	 */
	public Stream<XMLNode> stream() {
        return this.nodes.stream();
    }
	
	/**
	 * 하위 노드를 병렬 Stream 으로 반환
	 * 
	 * @return 하위 노드 병렬 Stream
	 */
	public Stream<XMLNode> parallelStream() {
        return this.nodes.parallelStream();
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
	void add(XMLNode node) {
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
			return this.nodes.get(0);
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
