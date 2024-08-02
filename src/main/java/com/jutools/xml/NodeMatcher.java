package com.jutools.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.jutools.StringUtil;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * XML Node Matcher 클래스<br>
 * XMLNode가 주어진 query에 적합한지 검사하여 반환<br>
 * 테그 명과 속성 명/값을 검사하여 반환함<br>
 * 주의) 시작점(start)과 종료점(end)는 여기에서 사용하지 않고<br>
 *      단순 파싱만을 수행하고 실제 사용은 XMLArray와 XMLNode에서 사용함
 * 
 * @author jmsohn
 */
class NodeMatcher {
	
	// --------- 상수 선언 ---------------
	
	/** 테그명 쿼리의 패턴 문자열 */
	private static String TAG_P = "[a-zA-Z_\\:\\*\\?][a-zA-Z0-9_\\:\\-\\*\\?]*";
	
	/** 배열 패턴 문자열 */
	private static String ARRAY_P = "\\[(?<start>[0-9]+)(\\-(?<end>[0-9]+))?\\]";

	/** 테그 쿼리 전체 패턴 문자열 */
	private static String QUERY_P = "(?<tag>" + TAG_P + ")"
			+ "(" + ARRAY_P + ")?"
			+ "\\s*(?<attrs>\\(\\s*" + AttrMatcher.ATTR_P + "\\s*(\\,\\s*" + AttrMatcher.ATTR_P + ")*\\))?\\s*";
	
	// --------- 멤버 변수 선언 ---------------
	
	/** 테그명 query */
	private String tagNameQuery;
	
	/** 노드 목록의 시작점 - NodeMatcher 클래스 주의 설명 참조 */
	@Getter(AccessLevel.PACKAGE)
	private int start = 0;
	
	/** 노드 목록의 종료점 - NodeMatcher 클래스 주의 설명 참조 */
	@Getter(AccessLevel.PACKAGE)
	private int end = Integer.MAX_VALUE;
	
	/** 속성 Matcher 목록 */
	private AttrMatcher[] attrMatchers;
	
	/**
	 * 생성자
	 * 
	 * @param query
	 */
	NodeMatcher(String query) throws Exception {
		
		// 입력값 검증
		if(query == null) {
			throw new NullPointerException("query is null.");
		}
		
		// ---- 테그명 Matcher 객체 생성 -----
		
		// 주어진 쿼리(tagQuery)가 쿼리형식에 맞는지 검증
		Pattern queryP = Pattern.compile(QUERY_P);
		Matcher queryM = queryP.matcher(query);
		
		if(queryM.matches() == false) {
			throw new IllegalArgumentException("query is not valid:" + query);
		}
		
		this.tagNameQuery = queryM.group("tag");
		
		// ---- 노드 배열 대상 시작점과 종료점 설정  -----
		
		String startStr = queryM.group("start");
		if(startStr != null) {
			
			this.start = Integer.parseInt(startStr);

			// 종료점 설정
			// 만일, 종료점이 설정되어 있지 않으면 시작점과 동일하게 설정
			String endStr = queryM.group("end");
			if(endStr != null) {
				this.end = Integer.parseInt(endStr); 
			} else {
				this.end = this.start + 1;
			}
			
			// 종료점은 시작점 보다 같거나 커야함
			if(start > end) {
				throw new IllegalArgumentException("end index must be greater equal than start index(start, end):(" + start + "," + end + ")");
			}
		}
		
		// ---- 속성 Matcher 객체 생성 -----
		
		this.attrMatchers = AttrMatcher.create(queryM.group("attrs"));
	}
	
	/**
	 * 주어진 DOM의 Element 객체가 매치 되는지 검사
	 * 매칭 시, true 반환
	 * 
	 * @param node 검사할 DOM의 Element 객체
	 * @return 매치 여부
	 */
	boolean match(Element node) throws Exception {
		
		// ------ 테그명 매치 검사 ---------
		String tagName = node.getNodeName();
		boolean isTagMatched = StringUtil.matchWildcard(tagName, this.tagNameQuery);
		
		if(isTagMatched == false) {
			return false;
		}
		
		// ------ 속성 매치 검사 ---------
		// 각 속성 별 매치 여부 검사 - 전체 속성이 매치되어야 함
		boolean isAttrMatched = true;
		for(AttrMatcher attrMatcher: this.attrMatchers) {
			
			if(attrMatcher.match(node) == false) {
				isAttrMatched = false;
				break;
			}
		}
		
		// 테그 매치 여부 반환
		return isAttrMatched;
	}
	
	/**
	 * 주어진 DOM의 노드 객체가 매치 되는지 검사
	 * 매칭 시, true 반환
	 * 
	 * @param node 검사할 DOM의 노드 객체
	 * @return 매치 여부
	 */
	boolean match(Node node) throws Exception {
		
		if(node.getNodeType() != Node.ELEMENT_NODE) {
			throw new IllegalArgumentException("node is not element type:" + node.getNodeName());
		}
		
		return this.match((Element)node);
	}
	
	/**
	 * 주어진 DOM의 노드 객체가 매치 되는지 검사
	 * 매칭 시, true 반환
	 * 
	 * @param node 검사할 DOM의 노드 객체
	 * @return 매치 여부
	 */
	boolean match(XMLNode node) throws Exception {
		
		// 입력값 검증
		if(node == null) {
			throw new IllegalArgumentException("node is null.");
		}
		
		return this.match(node.getNode());
	}
} 
