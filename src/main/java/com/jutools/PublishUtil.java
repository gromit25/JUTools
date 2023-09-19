package com.jutools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
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
	 */
	public static void toConsole(File formatFile) throws Exception {
		toConsole(formatFile, Charset.defaultCharset(), new HashMap<String, Object>());
	}
	
	/**
	 * 콘솔로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param values 출력시 사용할 변수-값 목록
	 */
	public static void toConsole(File formatFile, Map<String, Object> values) throws Exception {
		toConsole(formatFile, Charset.defaultCharset(), values);
	}
	
	/**
	 * 콘솔로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param cs 출력 Character set
	 */
	public static void toConsole(File formatFile, Charset cs) throws Exception {
		toConsole(formatFile, cs, new HashMap<String, Object>());
	}

	/**
	 * 콘솔로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param cs 출력 Character set
	 * @param values 출력시 사용할 변수-값 목록
	 */
	public static void toConsole(File formatFile, Charset cs, Map<String, Object> values) throws Exception {
		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatFile);
		publisher.publish(null, cs, values);
	}
	
	// 텍스트 파일로 출력 --------------------------------

	/**
	 * 텍스트 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 */
	public static void toTxt(File formatFile, File out) throws Exception {
		toTxt(formatFile, out, Charset.defaultCharset(), new HashMap<String, Object>());
	}
	
	/**
	 * 텍스트 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param values 출력시 사용할 변수-값 목록
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
	 */
	public static void toTxt(File formatFile, File out, Charset cs) throws Exception {
		toTxt(formatFile, out, cs, new HashMap<String, Object>());
	}

	/**
	 * 텍스트 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param cs 출력 Character set
	 * @param values 출력시 사용할 변수-값 목록
	 */
	public static void toTxt(File formatFile, File out, Charset cs, Map<String, Object> values) throws Exception {
		
		try(OutputStream outTxt = new FileOutputStream(out)) {
			Publisher publisher = PublisherFactory.create(PublisherType.TEXT_FILE, formatFile);
			publisher.publish(outTxt, cs, values);
		}
	}
	
	// 엑셀 파일 출력 -----------------------------------

	/**
	 * 엑셀 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 */
	public static void toExcel(File formatFile, File out) throws Exception {
		toExcel(formatFile, out, Charset.defaultCharset(), new HashMap<String, Object>());
	}

	/**
	 * 엑셀 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param values 출력시 사용할 변수-값 목록
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
	 */
	public static void toExcel(File formatFile, File out, Charset cs) throws Exception {
		toExcel(formatFile, out, cs, new HashMap<String, Object>());
	}

	/**
	 * 엑셀 파일로 출력
	 * 
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param out 출력 파일
	 * @param cs 출력 Character set
	 * @param values 출력시 사용할 변수-값 목록
	 */
	public static void toExcel(File formatFile, File out, Charset cs,  Map<String, Object> values) throws Exception {

		try(OutputStream outExcel = new FileOutputStream(out)) {
			Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
			publisher.publish(outExcel, cs, values);
		}
	}
}
