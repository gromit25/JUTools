package com.jutools.publish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
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
	 * 
	 * 
	 * @param type
	 * @param outFile
	 * @param formatFile
	 * @param charset
	 * @param values
	 */
	public static void publish(PublisherType type, File formatFile, File outFile, Charset charset, Map<String, Object> values) throws Exception {
    	try (OutputStream outExcel = new FileOutputStream(outFile)) {
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
    		publisher.publish(outExcel, charset, new HashMap<String, Object>());
    	}
	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @param formatFile
	 * @param outFile
	 * @param values
	 */
	public static void publish(PublisherType type, File formatFile, File outFile, Map<String, Object> values) throws Exception {
		Publisher.publish(type, formatFile, outFile, Charset.defaultCharset(), values);
	}
	
}
