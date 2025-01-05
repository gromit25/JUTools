package com.jutools.parserfw;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 파싱 트리의 노드 클래스
 * 
 * @author jmsohn
 */
public class TreeNode<T> {
	
	/** 현재 노드의 데이터 */
	@Getter
	@Setter
	private T data;
	
	/** 자식 노드 목록 */
	@Getter
	private List<TreeNode<T>> childs;
	
	/** 자식 노드의 개수 */
	@Getter
	private int childCount;
	
	/**
	 * 현재 노드가 상위 노드에 자식 노드로 등록되고 나면<br>
	 * 현재 노드의 자식 노드를 추가할 수 없음<br>
	 * childCount 가 상위 노드에 반영되지 않는 현상 방지
	 */
	@Getter
	private boolean lock;
	
	/**
	 * 생성자
	 */
	public TreeNode() {
		this(null);
	}
	
	/**
	 * 생성자
	 * 
	 * @param data 현재 노드의 데이터
	 */
	public TreeNode(T data) {
		this.data = data;
		this.childs = new ArrayList<TreeNode<T>>();
		this.childCount = 0;
		this.lock = false;
	}
	
	/**
	 * 자식 노드 추가 금지 lock 설정
	 */
	public synchronized void setLock() {
		this.lock = true;
	}
	
	/**
	 * 자식 노드 추가<br>
	 * 주의) 상위 노드에 등록 전에 하위 노드는 모두 추가되어야 함
	 * 
	 * @param node 추가할 자식 노드
	 */
	public synchronized void addChild(TreeNode<T> node) throws Exception {
		
		if(this.lock == true) {
			throw new Exception("can't add child node.");
		}
		
		this.childs.add(node);
		this.childCount += 1 + node.childCount; // 자기노드(1) 와 자식노드 수 추가
		
		node.setLock();
	}
	
	/**
	 * 주어진 위치에 자식 노드 추가<br>
	 * 주의) 상위 노드에 등록 전에 하위 노드는 모두 추가되어야 함
	 * 
	 * @param index 추가할 위치 
	 * @param node 추가할 자식 노드
	 */
	public synchronized void addChild(int index, TreeNode<T> node) throws Exception {
		
		if(this.lock == true) {
			throw new Exception("can't add child node.");
		}
		
		this.childs.add(index, node);
		this.childCount += 1 + node.childCount; // 자기노드(1) 와 자식노드 수 추가
		
		node.setLock();
	}
	
	/**
	 * post order로 방문
	 * 
	 * @return post order로 방문한 목록
	 */
	public List<T> travelPostOrder() {
		
		// post order로 방문한 목록 변수
		List<T> list = new ArrayList<T>();
		
		// 자식 노드 방문
		for(TreeNode<T> child: this.childs) {
			list.addAll(child.travelPostOrder());
		}
		
		// 현재 노드 데이터 추가
		list.add(this.data);
		
		// 방문 목록 반환
		return list;
	}
}
