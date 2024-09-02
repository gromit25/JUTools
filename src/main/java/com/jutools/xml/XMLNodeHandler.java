package com.jutools.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.Getter;

/**
 * XML에서 XMLNode를 생성하는 SAX 파서 핸들러 클래스
 * 
 * @author jmsohn
 */
public class XMLNodeHandler extends DefaultHandler {
	
	/** XML에서 생성된 root 노드 객체 */
	@Getter
	private XMLNode rootNode;
	
	/** 현재 생성 중인 노드 객체 */
	private XMLNode curNode;
	
	/**
	 * 생성자
	 */
	public XMLNodeHandler() {
		
		this.rootNode = null;
		this.curNode = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		try {
			
			// 새로운 노드 생성 ----------------
			XMLNode newNode = new XMLNode();
			
			// 테그명 설정
			newNode.setTagName(qName);
			
			// 속성 설정
			for(int index = 0; index < attributes.getLength(); index++) {
				
				// 속성명과 속성값을 가져옴
				String attrName = attributes.getQName(index);
				String attrValue = attributes.getValue(index);
				
				// 속성명과 속성값 설정
				newNode.setAttributeValue(attrName, attrValue);
			}
			
			// 부모 노드 설정 및 현재 노드 설정----------------------
			newNode.setParent(this.curNode);
			this.curNode = newNode;
			
			// 만일 루트노드가 null 일 경우, 현재 생성된 노드를 root 노드로 설정----------------
			if(this.rootNode == null) {
				this.rootNode = newNode;
			}
			
		} catch(Exception ex) {
			throw new SAXException(ex);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		// 테그가 종료될때는 현재 노드를 현재 노드의 부모 노드로 설정
		// ex) parentNode > childNode 에서 
		//     curNode = childNode 일 경우, childNode가 종료되었기 때문에
		//     curNode = parentNode로 설정함
		this.curNode = this.curNode.getParent();
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		
		try {
			this.curNode.setText(new String(ch, start, length));
		} catch(Exception ex) {
			throw new SAXException(ex);
		}
	}
}
