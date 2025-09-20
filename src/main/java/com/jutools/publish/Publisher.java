package com.jutools.publish;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.publish.formatter.Formatter;
import com.jutools.publish.formatter.FormatterXmlHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Publisher 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class Publisher {
	
	/** root formatter */
	@Getter
	@Setter(value=AccessLevel.PACKAGE)
	private Formatter rootFormatter;
	
	/**
	 * format 파일을 파싱하기 위한 xml 파서 생성
	 *  
	 * @return xml 파서
	 */
	protected abstract FormatterXmlHandler createXmlHandler() throws Exception;
	
	/**
	 * output stream으로 publish 수행
	 *  
	 * @param out 출력 스트림
	 * @param charset charater set
	 * @param values value container
	 */
	public abstract void publish(OutputStream out, Charset charset, Map<String, Object> values) throws Exception;
	
	/**
	 * publish 결과를 문자열로 변환하여 반환
	 * 
	 * @param charset charater set
	 * @param values value container
	 * @return 출력 문자열
	 */
	public String publish(Charset charset, Map<String, Object> values) throws Exception {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.publish(out, Charset.forName("UTF-8"), values);
		
		return new String(out.toByteArray(), "UTF-8");
	}
	
	/**
	 * output stream으로 publish 수행
	 * 
	 * @param type publisher의 종류
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param outFile 출력 파일
	 * @param charset 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void publish(PublisherType type, File formatFile, File outFile, Charset charset, Map<String, Object> values) throws Exception {
    	try (OutputStream out = new FileOutputStream(outFile)) {
    		Publisher publisher = PublisherFactory.create(type, formatFile);
    		publisher.publish(out, charset, values);
    	}
	}
	
	/**
	 * output stream으로 publish 수행
	 * 
	 * @param type publisher의 종류
	 * @param formatFile 출력 포맷 파일(xml)
	 * @param outFile 출력 파일
	 * @param values 변수-값 목록
	 */
	public static void publish(PublisherType type, File formatFile, File outFile, Map<String, Object> values) throws Exception {
		Publisher.publish(type, formatFile, outFile, Charset.defaultCharset(), values);
	}
	
	/**
	 * output stream으로 publish 수행
	 * 
	 * @param type publisher의 종류
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param outFile 출력 파일
	 * @param charset 출력 Character set
	 * @param values 변수-값 목록
	 */
	public static void publish(PublisherType type, InputStream formatInputStream, File outFile, Charset charset, Map<String, Object> values) throws Exception {
    	try (OutputStream outExcel = new FileOutputStream(outFile)) {
    		Publisher publisher = PublisherFactory.create(type, formatInputStream);
    		publisher.publish(outExcel, charset, values);
    	}
	}

	/**
	 * output stream으로 publish 수행
	 * 
	 * @param type publisher의 종류
	 * @param formatInputStream 출력 포맷 InputStream
	 * @param outFile 출력 파일
	 * @param values 변수-값 목록
	 */
	public static void publish(PublisherType type, InputStream formatInputStream, File outFile, Map<String, Object> values) throws Exception {
		Publisher.publish(type, formatInputStream, outFile, Charset.defaultCharset(), values);
	}
}
