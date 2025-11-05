package com.jutools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.jutools.xml.XMLArray;
import com.jutools.xml.XMLNode;
import com.jutools.xml.XMLNodeHandler;
import com.jutools.xml.typeshift.TypeShift;
import com.jutools.xml.typeshift.TypeShiftManager;

/**
 * xml 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class XMLUtil {
	
	/**
	 * xml 문자열에서 root node를 읽어옴
	 * 
	 * @param xmlStr xml 문자열
	 * @param charSet xml 문자열의 character set
	 * @return root node 객체
	 */
	public static XMLNode fromString(String xmlStr, Charset charSet) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isBlank(xmlStr) == true) {
			throw new IllegalArgumentException("xml string is blank.");
		}
		
		if(charSet == null) {
			throw new IllegalArgumentException("character set is null.");
		}
		
		// xml 문자열을 InputStream으로 변환 및 파싱하여 root node를 반환 
		return fromInputStream(new ByteArrayInputStream(xmlStr.getBytes(charSet)));
	}
	
	/**
	 * xml 문자열에서 root node를 읽어옴
	 * 
	 * @param xmlStr xml 문자열
	 * @return root node 객체
	 */
	public static XMLNode fromString(String xmlStr) throws Exception {
		
		return fromString(xmlStr, Charset.defaultCharset());
	}
	
	/**
	 * xml 파일에서 root node를 읽어옴
	 * 
	 * @param fileName xml 파일명
	 * @return root node 객체
	 */
	public static XMLNode fromFile(String fileName) throws Exception {
		
		if(StringUtil.isBlank(fileName) == true) {
			throw new IllegalArgumentException("file name is blank.");
		}
		
		return fromFile(new File(fileName));
	}
	
	/**
	 * xml 파일에서 root node를 읽어옴
	 * 
	 * @param file xml 파일 객체
	 * @return root node 객체
	 */
	public static XMLNode fromFile(File file) throws Exception {
		
		if(file == null) {
			throw new NullPointerException("file is null.");
		}
		
		if(file.canRead() == false) {
			throw new Exception("can't read file:" + file.getAbsolutePath());
		}
		
		return fromInputStream(new FileInputStream(file));
	}
	
	/**
	 * HTTPS로 가져온 XML 파일에서 root node를 읽어옴
	 * 
	 * @param conn HTTPS 연결
	 * @return root node 객체
	 */
	public static XMLNode fromHTTPS(HttpsURLConnection conn) throws Exception {
		return fromHTTP((HttpURLConnection)conn);
	}
	
	/**
	 * HTTP로 가져온 XML 파일에서 root node를 읽어옴
	 * 
	 * @param conn HTTP 연결
	 * @return root node 객체
	 */
	public static XMLNode fromHTTP(HttpURLConnection conn) throws Exception {
		
		if(conn == null) {
			throw new NullPointerException("http connection is null");
		}
		
		int responseCode = conn.getResponseCode();
		if(responseCode < 200 || responseCode > 299) {
			throw new Exception("http response code is not valid: " + responseCode);
		}
		
		return fromInputStream(conn.getInputStream());
	}
	
	/**
	 * xml 입력스트림에서 root node를 읽어옴
	 * 
	 * @param is xml 입력스트림
	 * @return root node 객체
	 */
	public static XMLNode fromInputStream(InputStream is) throws Exception {
		
		if(is == null) {
			throw new NullPointerException("input stream is null.");
		}
		
		XMLNodeHandler handler = new XMLNodeHandler();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(is, handler);
		
		return handler.getRootNode();
	}
	
	/**
	 * xml 파일에서 query 조회하여 결과 반환
	 * 
	 * @param fileName xml 파일명
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(String fileName, String query) throws Exception {
		return fromFile(new File(fileName)).select(query);  
	}
	
	/**
	 * xml 파일에서 query 조회하여 결과 반환
	 * 
	 * @param file xml 파일 객체
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(File file, String query) throws Exception {
		return fromFile(file).select(query);
	}
	
	/**
	 * HTTPS로 가져온 XML에서 query 조회하여 결과 반환
	 * 
	 * @param conn HTTPS 연결
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(HttpsURLConnection conn, String query) throws Exception {
		return fromHTTPS(conn).select(query);
	}
	
	/**
	 * HTTP로 가져온 XML에서 query 조회하여 결과 반환
	 * 
	 * @param conn HTTP 연결
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(HttpURLConnection conn, String query) throws Exception {
		return fromHTTP(conn).select(query);
	}
	
	/**
	 * xml 입력스트림에서 query 조회하여 결과 반환
	 * 
	 * @param is xml 입력스트림
	 * @param query 조회문
	 * @return query 조회 결과
	 */
	public static XMLArray select(InputStream is, String query) throws Exception {
		return fromInputStream(is).select(query);
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
	
	/**
	 * 타입 변환자를 타입 변환 관리자에 등록
	 * 
	 * @param name 타입 변환자 구분 이름
	 * @param typeShift 타입 변환자
	 */
	public static void registTypeShift(String name, TypeShift typeShift) throws Exception {
		TypeShiftManager.registTypeShift(name, typeShift);
	}
}
