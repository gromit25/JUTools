package com.jutools.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jutools.StringUtil;

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
	private int start = 0;
	
	/** 노드 목록의 종료점 - NodeMatcher 클래스 주의 설명 참조 */
	private int end = Integer.MAX_VALUE;
	
	/** 속성 Matcher 목록 */
	private AttrMatcher[] attrMatchers;
	
	/** 자식 노드 NodeMatcher */
	private NodeMatcher childNodeMatcher;
	
	/**
	 * 생성자 - 외부에서 직접 생성 금지
	 * 
	 * @param query
	 */
	private NodeMatcher(String query) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isBlank(query) == true) {
			throw new NullPointerException("query is null or blank.");
		}
		
		// ---- 쿼리를 나눔 ---------
		// ex) query : "test1 > test2 > test3(attr1='what')"
		//     -> "test1", "test2 > test3(attr1='what')"
		//
		// 첫번째 "test1" 은 현재 노드의 하위 노드 중 일치하는 것을 찾기 위함
		// "test2 > test3(attr1='what')"는 다시 하위 노드에 select 메소드에 호출함
		//
		String[] splitedQuery = StringUtil.splitFirst(query, "\\s*>\\s*");
		
		// ---- 테그명 Matcher 객체 생성 -----
		
		// 주어진 쿼리(tagQuery)가 쿼리형식에 맞는지 검증
		Pattern queryP = Pattern.compile(QUERY_P);
		Matcher queryM = queryP.matcher(splitedQuery[0]);
		
		if(queryM.matches() == false) {
			throw new IllegalArgumentException("query is invalid:" + query);
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
			if(this.start > this.end) {
				throw new IllegalArgumentException("end index must be greater equal than start index(start, end):(" + this.start + "," + this.end + ")");
			}
		}
		
		// ---- 속성 Matcher 생성 -----
		this.attrMatchers = AttrMatcher.create(queryM.group("attrs"));
		
		// ---- 하위 테그를 검색할 NodeMatcher 생성 ----
		if(splitedQuery.length > 1) {
			this.childNodeMatcher = NodeMatcher.create(splitedQuery[1]);
		} else {
			this.childNodeMatcher = null;
		}
	}
	
	/**
	 * 주어진 쿼리를 처리할 수 있는 NodeMatcher 객체를 생성하여 반환
	 * 
	 * @param query 쿼리
	 * @return 생성된 NodeMatcher 객체
	 */
	static NodeMatcher create(String query) throws Exception {
		return new NodeMatcher(query);
	}
	
	/**
	 * 대상 노드 목록(nodes)에서 쿼리에 적합한 노드 목록을 추출하여 반환
	 * 
	 * @param nodes 대상 노드 목록
	 * @return 쿼리에 적합한 노드 목록
	 */
	XMLArray match(XMLArray nodes) throws Exception {
		
		// 매치된 노드의 담을 노드 목록 변수
		XMLArray matchedNodes = new XMLArray();
		
		// 시작점(start) 부터 종료점(end) 이하이고 전체 노드의 크기 보다 작을때까지 수행
		for(int index = this.start; index < nodes.size() && index <= this.end; index++) {
			
			// 현재 검사할 노드 목록을 가져옴
			XMLNode node = nodes.get(index);
			
			// 노드 가 현재 표현식에 매치되고
			// 하위 NodeMatcher(subNodeMatcher) 없으면, 매치된 목록에 노드 추가함
			// 하위 NodeMatcher가 있으면, 하위 NodeMatcher에서 매치되는 모든 노드 목록을 추가함
			if(this.matchNode(node) == true) {
				
				if(this.childNodeMatcher == null) {
					matchedNodes.add(node);
				} else {
					matchedNodes.addAll(this.childNodeMatcher.match(node));
				}
			}
		}
		
		// 매치된 노드 목록을 반환
		return matchedNodes;
	}
	
	/**
	 * 대상 노드(node)의 자식 노드들 중 쿼리에 적합한 노드 목록을 추출하여 반환
	 * 
	 * @param node 대상 노드
	 * @return 자식 노드들 중 쿼리에 적합한 노드 목록
	 */
	XMLArray match(XMLNode node) throws Exception {
		return this.match(node.getChilds());
	}
	
	/**
	 * 주어진 DOM의 Element 객체가 매치 되는지 검사
	 * 매칭 시, true 반환
	 * 
	 * @param node 검사할 DOM의 Element 객체
	 * @return 매치 여부
	 */
	private boolean matchNode(XMLNode node) throws Exception {
		
		// ------ 테그명 매치 검사 ---------
		String tagName = node.getTagName();
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
} 
