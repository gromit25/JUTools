package com.jutools;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.publish.Publisher;
import com.jutools.publish.PublisherFactory;
import com.jutools.publish.PublisherType;

/**
 * 출력 Utility (콘솔, 텍스트 파일, 엑셀 파일 등)<br>    
 * XML에 정의에 형태로 출력 수행
 * 
 * @author jmsohn
 */
public class PublishUtil {
	
	// Console 출력 -------------------------
	
	/**
	 * 콘솔로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param values 변수-값 목록
	 */
	public static void toConsole(File formatFile, Map<String, Object> values) throws Exception {
		toConsole(formatFile, Charset.defaultCharset(), values);
	}

	/**
	 * 콘솔로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param cs 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void toConsole(File formatFile, Charset cs, Map<String, Object> values) throws Exception {
		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatFile);
		publisher.publish(null, cs, values);
	}
	
	/**
	 * 콘솔로 출력
	 * 
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param values 변수-값 목록
	 */
	public static void toConsole(InputStream formatInputStream, Map<String, Object> values) throws Exception {
		toConsole(formatInputStream, Charset.defaultCharset(), values);
	}
	
	/**
	 * 콘솔로 출력
	 * 
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param cs 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void toConsole(InputStream formatInputStream, Charset cs, Map<String, Object> values) throws Exception {
		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatInputStream);
		publisher.publish(null, cs, values);
	}
	
	// 텍스트 파일로 출력 --------------------------------

	/**
	 * 텍스트 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param values 변수-값 목록
	 */
	public static void toTxt(File formatFile, File out, Map<String, Object> values) throws Exception {
		toTxt(formatFile, out, Charset.defaultCharset(), values);
	}
	
	/**
	 * 텍스트 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param cs 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void toTxt(File formatFile, File out, Charset cs, Map<String, Object> values) throws Exception {
		Publisher.publish(PublisherType.TEXT_FILE, formatFile, out, cs, values);
	}
	
	/**
	 * 텍스트 파일로 출력
	 * 
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param out 출력 파일
	 * @param values 변수-값 목록
	 */
	public static void toTxt(InputStream formatInputStream, File out, Map<String, Object> values) throws Exception {
		toTxt(formatInputStream, out, Charset.defaultCharset(), values);
	}

	/**
	 * 텍스트 파일로 출력
	 * 
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param out 출력 파일
	 * @param cs 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void toTxt(InputStream formatInputStream, File out, Charset cs, Map<String, Object> values) throws Exception {
		Publisher.publish(PublisherType.TEXT_FILE, formatInputStream, out, cs, values);
	}

	
	// 엑셀 파일 출력 -----------------------------------

	/**
	 * 엑셀 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param values 변수-값 목록
	 */
	public static void toExcel(File formatFile, File out, Map<String, Object> values) throws Exception {
		toExcel(formatFile, out, Charset.defaultCharset(), values);
	}
	
	/**
	 * 엑셀 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param cs 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void toExcel(File formatFile, File out, Charset cs,  Map<String, Object> values) throws Exception {
		Publisher.publish(PublisherType.EXCEL_FILE, formatFile, out, cs, values);
	}
	
	/**
	 * 엑셀 파일로 출력
	 * 
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param out 출력 파일
	 * @param values 변수-값 목록
	 */
	public static void toExcel(InputStream formatInputStream, File out, Map<String, Object> values) throws Exception {
		toExcel(formatInputStream, out, Charset.defaultCharset(), values);
	}

	/**
	 * 엑셀 파일로 출력
	 * 
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param out 출력 파일
	 * @param cs 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void toExcel(InputStream formatInputStream, File out, Charset cs,  Map<String, Object> values) throws Exception {
		Publisher.publish(PublisherType.EXCEL_FILE, formatInputStream, out, cs, values);
	}
}
