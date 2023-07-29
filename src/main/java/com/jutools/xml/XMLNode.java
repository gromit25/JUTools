package com.jutools.xml;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jutools.StringUtil;
import com.jutools.TypeUtil;

/**
 * XML 노드 클래스
 * 
 * @author jmsohn
 */
public class XMLNode {
	
	/** DOM의 node 객체 */
	private Element node;
	
	/**
	 * 생성자
	 * 
	 * @param node
	 */
	public XMLNode(Node node) throws Exception {
		
		if(node == null) {
			throw new NullPointerException("node is null");
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
		
		// 현재 노드의 하위 노드 중 query에 일치하는 노드를 검색
		TagMatcher matcher = new TagMatcher(splited[0]);
		
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
		
		return nodes;
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
		return new XMLNode(this.node.getParentNode());
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
	 * XML Tag Matcher 클래스
	 * 
	 * @author jmsohn
	 */
	static class TagMatcher {
		
		/** 테그명 쿼리의 패턴 문자열 */
		private static String TAG_P = "[a-zA-Z_\\*\\?][a-zA-Z0-9_\\-\\*\\?]*";

		/** 테그 쿼리 전체 패턴 문자열 */
		private static String QUERY_P = "(?<tag>" + TAG_P + ")"
				+ "\\s*(?<attrs>\\(\\s*" + AttrMatcher.ATTR_P + "\\s*(\\,\\s*" + AttrMatcher.ATTR_P + ")*\\))?\\s*";
		
		/** 테그명 query */
		private String tagNameQuery;
		
		/** 속성 Matcher 목록 */
		private AttrMatcher[] attrMatchers;
		
		/**
		 * 생성자
		 * 
		 * @param tagQuery
		 */
		TagMatcher(String tagQuery) throws Exception {
			
			// 입력값 검증
			if(tagQuery == null) {
				throw new NullPointerException("tag query is null.");
			}
			
			// 주어진 쿼리(tagQuery)가 쿼리형식에 맞는지 검증
			Pattern tagQueryP = Pattern.compile(QUERY_P);
			Matcher tagQueryM = tagQueryP.matcher(tagQuery);
			
			if(tagQueryM.matches() == false) {
				throw new IllegalArgumentException("tag query is not valid:" + tagQuery);
			}
			
			// 쿼리에서 테그명 Matcher와 속성 Matcher 객체 생성
			this.tagNameQuery = tagQueryM.group("tag");
			this.attrMatchers = AttrMatcher.create(tagQueryM.group("attrs"));
			
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
		 * 주어진 DOM의 Element 객체가 매치 되는지 검사
		 * 매칭 시, true 반환
		 * 
		 * @param node 검사할 DOM의 Element 객체
		 * @return 매치 여부
		 */
		boolean match(Element node) throws Exception {
			
			// 테그명이 매치되는지 검사
			String tagName = node.getNodeName();
			boolean isTagMatched = StringUtil.matchWildcard(tagName, this.tagNameQuery);
			
			if(isTagMatched == false) {
				return false;
			}
			
			// 속성이 매치되는지 검사
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
		
	} // End of TagMatcher class
	
	/**
	 * XML Tag Attribute Matcher 클래스 
	 * 
	 * @author jmsohn
	 */
	static class AttrMatcher {
		
		/**
		 * 속성값 검사 방식(Match Type) 클래스
		 * 
		 * @author jmsohn
		 */
		enum MatchType {
			
			/** 모든 문자열 일치 */
			EQUAL {
				@Override
				boolean match(String target, String pattern) throws Exception {
					return pattern.equals(target);
				}
			},
			/** wildcard 문자열 일치 */
			WILDCARD {
				@Override
				boolean match(String target, String pattern) throws Exception {
					return StringUtil.matchWildcard(target, pattern);
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
		private static String ATTR_P = "[a-zA-Z_\\*\\?][a-zA-Z0-9_\\-\\*\\?]*"
				+ "\\s*\\=\\s*"
				+ "[wp]?\\'[^\\']*\\'";
		
		/** 속성 쿼리의 패턴 - 이름 설정 */
		private static String ATTR_P_NAMED = "(?<attr>[a-zA-Z_\\*\\?][a-zA-Z0-9_\\-\\*\\?]*)"
				+ "\\s*\\=\\s*"
				+ "(?<matchtype>[wp])?\\'(?<value>[^\\']*)\\'";
		
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
		 * 쿼리로 부터 AttrMatcher 목록을 생성
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
				MatchType matchType = MatchType.EQUAL;
				
				if(matchTypeStr != null) {
					if(matchTypeStr == "w") {
						matchType = MatchType.WILDCARD;
					} else if(matchTypeStr == "p") {
						matchType = MatchType.REGEXP;
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
		
	} // End of AttrMatcher

}
