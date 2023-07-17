package com.jutools.xml;

import java.io.File;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	class TagQueryMatcher {
		
		TagQueryMatcher(String tagQuery) {
			
		}
		
	} // End of TagQueryMatcher class
}
