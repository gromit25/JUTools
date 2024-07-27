package com.jutools.xml;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.jutools.StringUtil;
import com.jutools.TypeUtil;

/**
 * XML 테그의 속성 Matcher 클래스<br>
 * 속성 명/값이 query에 적합한지 검사하여 반환 
 * 
 * @author jmsohn
 */
class AttrMatcher {
	
	/**
	 * 속성값 검사 방식(Match Type) 클래스
	 * 
	 * @author jmsohn
	 */
	enum MatchType {

		/** 검사하지 않음, 항상 true를 반환 */
		NONE {
			@Override
			boolean match(String target, String pattern) throws Exception {
				return true;
			}
		},
		/** 모든 문자열 일치 */
		EQUAL {
			@Override
			boolean match(String target, String pattern) throws Exception {
				pattern = StringUtil.escape(pattern);
				return pattern.equals(target);
			}
		},
		/** wildcard 문자열 일치 */
		WILDCARD {
			@Override
			boolean match(String target, String pattern) throws Exception {
				return StringUtil.matchWildcard(pattern, target);
			}
		},
		/** 정규 표현식 문자열 일치 */
		REGEXP {
			@Override
			boolean match(String target, String pattern) throws Exception {
				return target.matches(pattern);
			}
		};
		
		/**
		 * 주어진 문자열이 속성값과 match 여부 반환<br>
		 * 일치시 true 반환
		 * 
		 * @param target 검사할 문자열
		 * @param pattern 문자열 패턴
		 * @return match 여부
		 */
		abstract boolean match(String target, String pattern) throws Exception;
	}
	
	/** 속성 쿼리의 패턴 */
	static String ATTR_P = "[a-zA-Z_\\*\\?\\#][a-zA-Z0-9_\\-\\*\\?]*"
			+ "(\\s*\\=\\s*[wp]?'[^'\\\\]*(\\\\.[^'\\\\]*)*')?";
	
	/** 속성 쿼리의 패턴 - 이름 설정 */
	private static String ATTR_P_NAMED = "(?<attr>[a-zA-Z_\\*\\?\\#][a-zA-Z0-9_\\-\\*\\?]*)"
			+ "(\\s*\\=\\s*(?<matchtype>[wp])?'(?<value>[^'\\\\]*(\\\\.[^'\\\\]*)*)')?";
	
	/** 테그의 텍스트 속성 명 */
	private static String TEXT_ATTR_NAME = "#text";
	
	/** 속성명 쿼리 */
	private String attrQuery;
	/** 값 쿼리 */
	private String valueQuery;
	/** 값 검사 방식 */
	private MatchType valueMatchType;
	
	/**
	 * 생성자
	 * 
	 * @param attrQuery 속성명 쿼리
	 * @param valueQuery 값 쿼리
	 * @param valueMatchType 값 검사 방식
	 */
	private AttrMatcher(String attrQuery, String valueQuery, MatchType valueMatchType) throws Exception {
		
		this.attrQuery = attrQuery;
		this.valueQuery = valueQuery;
		this.valueMatchType = valueMatchType;
		
	}
	
	/**
	 * 쿼리로 부터 AttrMatcher 목록을 생성<br>
	 * <pre>
	 * ex) attrQuery : attr1='test', attr2=w't?st' 일 경우,
	 *     -> {attr1 용 AttrMatcher, attr2 용 AttrMatcher}
	 * </pre> 
	 * 
	 * @param attrQuery 속성 쿼리
	 * @return AttrMatcher 목록
	 */
	static AttrMatcher[] create(String attrQuery) throws Exception {
		
		// AttrMatcher 목록 변수
		ArrayList<AttrMatcher> attrMatchers = new ArrayList<>();
		
		// 만일 쿼리가 없을 경우, 빈 목록 반환
		if(StringUtil.isEmpty(attrQuery) == true) {
			return TypeUtil.toArray(attrMatchers, AttrMatcher.class);
		}

		// 쿼리에서 AttrMatcher를 만들기 위한 패턴
		Pattern attrP = Pattern.compile(ATTR_P_NAMED);
		Matcher attrM = attrP.matcher(attrQuery);
		
		int index = 0;
		
		while(attrM.find(index) == true) {
			
			// 쿼리에서 속성명 쿼리와 값 쿼리를 추출 
			String attr = attrM.group("attr");
			String value = attrM.group("value");
			
			// 쿼리에서 검사 방식을 추출
			String matchTypeStr = attrM.group("matchtype");
			MatchType matchType = MatchType.NONE;
			
			if(matchTypeStr != null) {
				if(matchTypeStr.equals("w") == true) {
					matchType = MatchType.WILDCARD;
				} else if(matchTypeStr.equals("p") == true) {
					matchType = MatchType.REGEXP;
				}
			} else {
				if(value != null) {
					matchType = MatchType.EQUAL;
				}
			}
			
			// 텍스트 속성에 대한 검사일 경우, 값이 있어야 함
			// ex) #text = '홍길동'(O), #text (X)
			if(TEXT_ATTR_NAME.equals(attr) == true) {
				if(value == null) {
					throw new Exception("#text attribute must have value.");
				}
			}
			
			// 추출한 쿼리로 AttrMatcher를 만들고 목록에 추가함
			attrMatchers.add(new AttrMatcher(attr, value, matchType));
			
			// 다음 속성 쿼리 검색을 위해, 현재 속성 쿼리의 마지막으로 이동
			index = attrM.end();
		}

		return TypeUtil.toArray(attrMatchers, AttrMatcher.class);
	}
	
	/**
	 * 노드와 쿼리가 일치 여부 반환<br>
	 * 일치시 true 반환
	 * 
	 * @param node 검사할 노드
	 * @return 일치 여부
	 */
	boolean match(Element node) throws Exception {
		
		// 입력값 검증
		if(node == null) {
			throw new NullPointerException("node is null.");
		}
		
		// 주어진 테그의 속성이 설정된 query와 일치하는지 여부 검사 후 반환
		if(this.attrQuery.equals(TEXT_ATTR_NAME) == true) {
			
			// 만일 주어진 테그의 속성이 테그의 텍스트 일 경우 처리
			return this.valueMatchType.match(node.getTextContent(), this.valueQuery);
			
		} else {
			
			// 만일 주어진 테그의 속성이 테그의 일반 속성일 경우 처리
		
			// 노드의 속성 목록을 가져옴
			NamedNodeMap attrMap = node.getAttributes();
			
			for(int index = 0; index < attrMap.getLength(); index++) {
				
				// 속성을 가져옴
				Node attrNode = attrMap.item(index);
				if (attrNode.getNodeType() != Node.ATTRIBUTE_NODE) {
					continue;
				}
				
				// 속성의 이름과 값을 가져옴
				String attrName = attrNode.getNodeName();
				String attrValue = attrNode.getNodeValue();
				
				// 속성명 일치 여부 검사
				boolean isAttrNameMatch = StringUtil.matchWildcard(attrName, this.attrQuery);
				// 속성값 일치 여부 검사
				boolean isAttrValueMatch = this.valueMatchType.match(attrValue, this.valueQuery);
				
				// 속정명과 속성값이 일치할 경우 true 반환
				if(isAttrNameMatch == true && isAttrValueMatch == true) {
					return true;
				}
			}
			
			// match 되는 것이 없으면 false
			return false;
		}
	}
}
