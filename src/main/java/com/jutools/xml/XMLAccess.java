package com.jutools.xml;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jutools.StringUtil;
import com.jutools.TypeUtil;

/**
 * 
 * @author jmsohn
 */
public class XMLAccess {
	
	private Document xmlDoc;
	
	/**
	 * 
	 * @param xmlDoc
	 */
	public XMLAccess(Document xmlDoc) throws Exception {
		
		if(xmlDoc == null) {
			throw new NullPointerException("xml doc is null.");
		}
		
		this.xmlDoc = xmlDoc;
	}
	
	/**
	 * 
	 * @param xmlFile
	 */
	public XMLAccess(File xmlFile) throws Exception {
		
		DocumentBuilderFactory configFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder configBuilder = configFactory.newDocumentBuilder();
		
		this.xmlDoc = configBuilder.parse(xmlFile);
	}

	public XMLAccess(Path xmlPath) throws Exception {
		this(xmlPath.toFile());
	}
	
	public XMLAccess(String fileName) throws Exception {
		this(new File(fileName));
	}
	
	public XMLArray select(String query) throws Exception {
		
		NodeList nodes = this.xmlDoc.getChildNodes();
		
//		NodeList dburlList = doc.getElementsByTagName("db.url"); // db.url인 tag노드목록을 찾음
//		for (int index = 0; index < dburlList.getLength(); index++) {
//
//		    Node node = dburlList.item(index);
//		    if (node.getNodeType() != Node.ELEMENT_NODE) {
//		        continue;
//		    }
//		    
//		    Element dburlElement = (Element)node;
//		    System.out.println("id=" + dburlElement.getAttribute("id")); // node의 Id 정보를 가져옴
//		    System.out.println("db.url=" + dburlElement.getTextContent()); // node의 text를 가져옴
//		}
		
		return null;
	}
	
	/**
	 * 
	 * @author jmsohn
	 */
	static class TagMatcher {
		
		private static String TAG_P = "[a-zA-Z_\\*\\?][a-zA-Z0-9_-\\*\\?]*";

		private static String QUERY_P = "(?<tag>" + TAG_P + ")"
				+ "\\s*(?<attrs>\\(\\s*" + AttrMatcher.ATTR_P + "\\s*(\\,\\s*" + AttrMatcher.ATTR_P + ")*\\))?\\s*";
		
		/**
		 * 생성자
		 * 
		 * @param tagQuery
		 */
		TagMatcher(String tagQuery) throws Exception {
			
		}
		
	} // End of TagMatcher class
	
	/**
	 * 
	 * @author jmsohn
	 */
	static class AttrMatcher {
		
		/**
		 * 
		 * @author jmsohn
		 */
		enum MatchType {
			
			EQUAL {
				@Override
				boolean match(String target, String pattern) throws Exception {
					return pattern.equals(target);
				}
			},
			WILDCARD {
				@Override
				boolean match(String target, String pattern) throws Exception {
					return StringUtil.matchWildcard(target, pattern);
				}
			},
			REGEXP {
				@Override
				boolean match(String target, String pattern) throws Exception {
					return target.matches(pattern);
				}
			};
			
			abstract boolean match(String target, String pattern) throws Exception;
		}
		
		/** */
		private static String ATTR_P = "[a-zA-Z_\\*\\?][a-zA-Z0-9_-\\*\\?]*"
				+ "\\s*\\=\\s*"
				+ "[wp]?\\'[^\\']*\\'";
		
		/** */
		private static String ATTR_P_NAMED = "(?<attr>[a-zA-Z_\\*\\?][a-zA-Z0-9_-\\*\\?]*)"
				+ "\\s*\\=\\s*"
				+ "(?<matchtype>[wp])?\\'(?<value>[^\\']*)\\'";
		
		/** */
		private String attrQuery;
		/** */
		private String valueQuery;
		/** */
		private MatchType valueMatchType;
		
		/**
		 * 생성자
		 * 
		 * @param attrQuery
		 * @param valueQuery
		 * @param valueMatchType
		 */
		private AttrMatcher(String attrQuery, String valueQuery, MatchType valueMatchType) throws Exception {
			
			this.attrQuery = attrQuery;
			this.valueQuery = valueQuery;
			this.valueMatchType = valueMatchType;
			
		}
		
		/**
		 * 
		 * @param query
		 * @return
		 */
		static AttrMatcher[] create(String query) throws Exception {
			
			ArrayList<AttrMatcher> attrMatchers = new ArrayList<>();

			Pattern attrP = Pattern.compile(ATTR_P_NAMED);
			Matcher attrM = attrP.matcher(query);
			
			int index = 0;
			
			while(attrM.find(index) == true) {
				
				//
				String attr = attrM.group("attr");
				String value = attrM.group("value");
				
				//
				String matchTypeStr = attrM.group("matchtype");
				MatchType matchType = MatchType.EQUAL;
				
				if(matchTypeStr != null) {
					if(matchTypeStr == "w") {
						matchType = MatchType.WILDCARD;
					} else if(matchTypeStr == "p") {
						matchType = MatchType.REGEXP;
					}
				}
				
				//
				attrMatchers.add(new AttrMatcher(attr, value, matchType));
				
				//
				index = attrM.end();
			}

			return TypeUtil.toArray(attrMatchers, AttrMatcher.class);
		}
		
		/**
		 * 
		 * 
		 * @param node
		 * @return
		 */
		boolean match(Element node) throws Exception {
			
			// 입력값 검증
			if(node == null) {
				throw new NullPointerException("node is null.");
			}
			
			//
			NamedNodeMap attrMap = node.getAttributes();
			
			for(int index = 0; index < attrMap.getLength(); index++) {
				
				//
				Node attrNode = attrMap.item(index);
				if (attrNode.getNodeType() != Node.ATTRIBUTE_NODE) {
					continue;
				}
				
				//
				String attrName = attrNode.getNodeName();
				String attrValue = attrNode.getNodeValue();
				
				boolean isAttrNameMatch = StringUtil.matchWildcard(attrName, this.attrQuery);
				boolean isAttrValueMatch = this.valueMatchType.match(attrValue, this.valueQuery);
				
				if(isAttrNameMatch == true && isAttrValueMatch == true) {
					return true;
				}
			}
			
			// match 되는 것이 없으면 false
			return false;
		}
		
	} // End of AttrMatcher
}
