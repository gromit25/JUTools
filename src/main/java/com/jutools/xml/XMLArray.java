package com.jutools.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jutools.StringUtil;

/**
 * XML 노드 목록 클래스
 * 
 * @author jmsohn
 */
public class XMLArray implements Iterable<XMLNode> {
	
	/** XML 노드 목록 객체 */
	private List<XMLNode> nodes;
	
	/**
	 * 생성자
	 * 
	 * @param nodes 노드 배열 객체
	 */
	XMLArray(XMLNode[] nodes) throws Exception {
		
		if(nodes == null) {
			throw new NullPointerException("nodes is null.");
		}
		
		this.nodes = new ArrayList<>(nodes.length);
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
	 * XML 목록에서 주어진 조회 query 에 적합한 결과를 반환
	 * 
	 * @param query 조회 query
	 * @return query에 적합한 노드 목록 
	 */
	public XMLArray select(String query) throws Exception {
		
		// 입력값 검증
		if(query == null) {
			throw new IllegalArgumentException("query is null.");
		}
		
		// 만일, 노드 목록 객체가 null 이거나 크기가 0이면, 빈 결과를 반환함
		if(this.nodes == null || this.nodes.size() == 0) {
			return new XMLArray();
		}
		
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
		
		// 범위 시작점 - query에 설정된 시작점을 가져옴

		int start = matcher.getStart();
		
		// 범위 종료점 - query에 설정된 종료점을 가져옴
		// 종료점이 현재 배열 보다 클 경우 배열의 끝을 종료점으로 설정함
		int end = matcher.getEnd();
		
		if(end > this.nodes.size()) {
			end = this.nodes.size();
		}
		
		// ------------- 3. 주어진 query에 따라 노드 추출 ----------------
		
		// query에 의해 선택된 노드 목록
		XMLArray selectedNodes = new XMLArray();
		
		// 범위 추출된 노드 목록 별로 query에 적합한 노드를 추가
		for(int index = start; index < end; index++) {
			
			// 특정 인덱스의 노드 정보를 획득
			XMLNode node = this.nodes.get(index);
			
			// query와 일치하는지 확인
			// 일치하지 않으면 다음 하위노드 검색
			if(matcher.match(node) == false) {
				continue;
			}
			
			// 만일 query가 마지막 query(splited.length ==1) 이면,
			// -> 현재 노드를 결과에 추가함
			// query가 마지막이 아니면,
			// -> 하위 노드에 query를 수행 후
			//    수행 결과를 XML 결과 목록에 모두 추가
			if(splited.length == 1) {
				selectedNodes.add(node);
			} else {
				selectedNodes.addAll(node.select(splited[1]));
			}
		}
		
		// 선택된 노드를 반환
		return selectedNodes;
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
