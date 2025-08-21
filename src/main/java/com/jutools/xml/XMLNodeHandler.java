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
	
	/** XML에서 생성된 root 노드 */
	@Getter
	private XMLNode rootNode;
	
	/** 현재 생성 중인 노드 */
	private XMLNode curNode;
	
	/** 가장 마지막 자식 노드 */
	private XMLNode childNode;
	
	/**
	 * 텍스트 상태 여부<br>
	 * 파싱 수행시, 테그의 첫번째 텍스트 영역인지 여부임<br>
	 * 텍스트와 테일 텍스트를 나누기 위한 용도
	 */
	private boolean isTextStatus;
	
	/** XML의 텍스트 만들기 위한 임시 객체 */
	private StringBuilder textBuilder;

	
	/**
	 * 생성자
	 */
	public XMLNodeHandler() {
		
		this.rootNode = null;
		this.curNode = null;
		this.childNode = null;
		
		this.isTextStatus = true;
		this.textBuilder = new StringBuilder("");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		try {
			
			// 노드에 텍스트와 테일 텍스트 설정 --------------
			this.setTextAndTail();
			
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
			
			// 부모, 현재, 자식 노드 및 첫번째 상태 설정----------------------
			newNode.setParent(this.curNode); // 부모 노드
			this.curNode = newNode; // 현재 노드
			this.childNode = null; // 자식 노드
			this.isTextStatus = true;
			
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
		
		// 노드에 텍스트와 테일 텍스트 설정
		this.setTextAndTail();
		
		// 노드 텍스트 상태를 false(테일 텍스트) 상태로 변경
		// 노드가 종료 되면 이전 자식 노드의 테일 텍스트가 됨
		this.isTextStatus = false;
		
		// 자식 노드 설정
		this.childNode = this.curNode;
		
		// 테그가 종료될때는 현재 노드를 현재 노드의 부모 노드로 설정
		// ex) parentNode > childNode 에서 
		//     curNode = childNode 일 경우, childNode가 종료되었기 때문에
		//     curNode = parentNode로 설정함
		this.curNode = this.curNode.getParent();
	}
	
	/**
	 * 노드에 텍스트와 테일 텍스트 설정
	 */
	private void setTextAndTail() {
		
		// 노드의 첫번째 텍스트이면, 현재 노드 텍스트 설정
		if(this.curNode != null && this.isTextStatus == true) {
			this.curNode.setText(this.textBuilder.toString());
			this.textBuilder.setLength(0);
		}
		
		// 노드의 첫번째 텍스트가 아니면(즉 중간에 다른 자식 노드가 있었다면), 이전 자식 노드에 테일 텍스트로 설정
		if(this.childNode != null && this.isTextStatus == false) {
			this.childNode.setTail(this.textBuilder.toString());
			this.textBuilder.setLength(0);
		}
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		
		this.textBuilder.append(new String(ch, start, length));
	}
}
