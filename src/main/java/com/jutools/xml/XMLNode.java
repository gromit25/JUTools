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
 * 
 * 
 * @author jmsohn
 */
public class XMLNode {
	
	private Element node;
	
	/**
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
	 * 
	 * @param query
	 * @return
	 */
	public XMLArray select(String query) throws Exception  {
		
		//
		XMLArray nodes = new XMLArray();
		
		//
		String[] splited = StringUtil.splitFirst(query, "\\s*>\\s*");
		
		//
		TagMatcher matcher = new TagMatcher(splited[0]);
		
		NodeList childs = this.node.getChildNodes();
		for(int index = 0; index < childs.getLength(); index++) {
			
			//
			Node childNode = childs.item(index);
			if(childNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			//
			if(matcher.match(childNode) == false) {
				continue;
			}
			
			//
			XMLNode xmlChildNode = new XMLNode(childNode);
			if(splited.length == 1) {
				nodes.add(xmlChildNode);
			} else {
				nodes.addAll(xmlChildNode.select(splited[1]));
			}
			
		}
		
		return nodes;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTagName() throws Exception {
		return this.node.getNodeName();
	}
	
	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public String getAttribute(String attrName) throws Exception {
		return this.node.getAttribute(attrName);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getAttributeNames() throws Exception {
		
		//
		NamedNodeMap attrMap = this.node.getAttributes();
		
		//
		String[] attrNames = new String[attrMap.getLength()];
		
		for(int index = 0; index < attrMap.getLength(); index++) {
			
			Node attr = attrMap.item(index);
			attrNames[index] = attr.getNodeName(); 
		}
		
		return attrNames;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getText() throws Exception {
		return this.node.getTextContent();
	}
	
	/**
	 * 
	 * @return
	 */
	public XMLNode getParent() throws Exception {
		return new XMLNode(this.node.getParentNode());
	}
	
	/**
	 * 
	 * @return
	 */
	public XMLArray getChilds() throws Exception {
		return this.select("*");
	}
	
	/**
	 * 
	 * @author jmsohn
	 */
	static class TagMatcher {
		
		/** */
		private static String TAG_P = "[a-zA-Z_\\*\\?][a-zA-Z0-9_\\-\\*\\?]*";

		/** */
		private static String QUERY_P = "(?<tag>" + TAG_P + ")"
				+ "\\s*(?<attrs>\\(\\s*" + AttrMatcher.ATTR_P + "\\s*(\\,\\s*" + AttrMatcher.ATTR_P + ")*\\))?\\s*";
		
		/** */
		private String tagNameQuery;
		
		/** */
		private AttrMatcher[] attrMatchers;
		
		/**
		 * 생성자
		 * 
		 * @param tagQuery
		 */
		TagMatcher(String tagQuery) throws Exception {
			
			if(tagQuery == null) {
				throw new NullPointerException("tag query is null.");
			}
			
			Pattern tagQueryP = Pattern.compile(QUERY_P);
			Matcher tagQueryM = tagQueryP.matcher(tagQuery);
			
			if(tagQueryM.matches() == false) {
				throw new IllegalArgumentException("tag query is not valid:" + tagQuery);
			}
			
			this.tagNameQuery = tagQueryM.group("tag");
			this.attrMatchers = AttrMatcher.create(tagQueryM.group("attrs"));
			
		}
		
		/**
		 * 
		 * 
		 * @param node
		 * @return
		 */
		boolean match(Node node) throws Exception {
			
			if(node.getNodeType() != Node.ELEMENT_NODE) {
				throw new IllegalArgumentException("node is not element type:" + node.getNodeName());
			}
			
			return this.match((Element)node);
		}
		
		/**
		 * 
		 * 
		 * @param node
		 * @return
		 */
		boolean match(Element node) throws Exception {
			
			//
			String tagName = node.getNodeName();
			boolean isTagMatched = StringUtil.matchWildcard(tagName, this.tagNameQuery);
			
			if(isTagMatched == false) {
				return false;
			}
			
			//
			boolean isAttrMatched = true;
			for(AttrMatcher attrMatcher: this.attrMatchers) {
				
				if(attrMatcher.match(node) == false) {
					isAttrMatched = false;
					break;
				}
			}
			
			//
			return isAttrMatched;
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
		private static String ATTR_P = "[a-zA-Z_\\*\\?][a-zA-Z0-9_\\-\\*\\?]*"
				+ "\\s*\\=\\s*"
				+ "[wp]?\\'[^\\']*\\'";
		
		/** */
		private static String ATTR_P_NAMED = "(?<attr>[a-zA-Z_\\*\\?][a-zA-Z0-9_\\-\\*\\?]*)"
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
		static AttrMatcher[] create(String attrQuery) throws Exception {
			
			ArrayList<AttrMatcher> attrMatchers = new ArrayList<>();
			
			if(StringUtil.isEmpty(attrQuery) == true) {
				return TypeUtil.toArray(attrMatchers, AttrMatcher.class);
			}

			Pattern attrP = Pattern.compile(ATTR_P_NAMED);
			Matcher attrM = attrP.matcher(attrQuery);
			
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
