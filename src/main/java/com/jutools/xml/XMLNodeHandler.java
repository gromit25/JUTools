package com.jutools.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.Getter;

/**
 * 
 * 
 * @author jmsohn
 */
public class XMLNodeHandler extends DefaultHandler {
	
	@Getter
	private XMLNode rootNode;
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
			
			// ----------------
			//
			XMLNode newNode = new XMLNode();
			
			//
			newNode.setTagName(qName);
			
			//
			for(int index = 0; index < attributes.getLength(); index++) {
				
				//
				String attrName = attributes.getQName(index);
				String attrValue = attributes.getValue(index);
				
				//
				newNode.setAttributeValue(attrName, attrValue);
			}
			
			// ----------------------
			// 
			newNode.setParent(this.curNode);
			this.curNode = newNode;
			
			// ----------------
			if(this.rootNode == null) {
				this.rootNode = newNode;
			}
			
		} catch(Exception ex) {
			throw new SAXException(ex);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		//
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
