package com.jutools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.jutools.xml.XMLArray;
import com.jutools.xml.XMLNode;

/**
 * xml 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class XMLUtil {
	
	/**
	 * xml 파일에서 root node를 읽어옴
	 * 
	 * @param fileName xml 파일명
	 * @return root node 객체
	 */
	public static XMLNode getRootNode(String fileName) throws Exception {
		
		if(StringUtil.isEmpty(fileName) == true) {
			throw new IllegalArgumentException("file name is empty.");
		}
		
		return getRootNode(new File(fileName));
	}
	
	/**
	 * xml 파일에서 root node를 읽어옴
	 * 
	 * @param file xml 파일 객체
	 * @return root node 객체
	 */
	public static XMLNode getRootNode(File file) throws Exception {
		
		if(file == null) {
			throw new NullPointerException("file is null.");
		}
		
		if(file.canRead() == false) {
			throw new Exception("can't read file:" + file.getAbsolutePath());
		}
		
		return getRootNode(new FileInputStream(file));
	}
	
	/**
	 * xml 입력스트림에서 root node를 읽어옴
	 * 
	 * @param is xml 입력스트림
	 * @return root node 객체
	 */
	public static XMLNode getRootNode(InputStream is) throws Exception {
		
		if(is == null) {
			throw new NullPointerException("input stream is null.");
		}
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		
		return getRootNode(doc);
	}
	
	/**
	 * xml dom의 document 객체에서 root node를 읽어옴
	 * 
	 * @param doc xml dom의 document 객체
	 * @return root node 객체
	 */
	public static XMLNode getRootNode(Document doc) throws Exception {
		
		if(doc == null) {
			throw new NullPointerException("document obj is null.");
		}
		
		Element root = doc.getDocumentElement();
		return new XMLNode(root);
	}
	
	/**
	 * xml 파일에서 query 조회하여 결과 반환
	 * 
	 * @param fileName xml 파일명
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(String fileName, String query) throws Exception {
		return getRootNode(new File(fileName)).select(query);  
	}
	
	/**
	 * xml 파일에서 query 조회하여 결과 반환
	 * 
	 * @param file xml 파일 객체
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(File file, String query) throws Exception {
		return getRootNode(file).select(query);
	}
	
	/**
	 * xml 입력스트림에서 query 조회하여 결과 반환
	 * 
	 * @param is xml 입력스트림
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(InputStream is, String query) throws Exception {
		return getRootNode(is).select(query);
	}
	
	/**
	 * xml dom의 document에 query 조회하여 결과 반환
	 * 
	 * @param doc xml dom의 document 객체
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(Document doc, String query) throws Exception {
		return getRootNode(doc).select(query);
	}
	
	/**
	 * xml dom의 node에 query 조회하여 결과 반환
	 * 
	 * @param node xml dom의 node 객체
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(Node node, String query) throws Exception {
		return (new XMLNode(node)).select(query);
	}
	
	/**
	 * xml node에 query 조회하여 결과 반환
	 * 
	 * @param node node 객체
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(XMLNode node, String query) throws Exception {
		return node.select(query);
	}
}
