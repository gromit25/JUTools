package com.jutools.filetracker.trimmer;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Consumer;

import com.jutools.filetracker.Trimmer;

/**
 * 
 * 
 * @author jmsohn
 */
public class LogfmtReader implements Trimmer<Map<String, Object>>{
	
	/** 파일을 읽을 때, 문자 인코딩 방식 */
	private Charset charset;
	
	/** 끝나지 않은 데이터 임시 저장 변수 */
	private byte[] temp = null;
	
	
	/**
	 * 생성자
	 * 
	 * @param charset character set
	 */
	public LogfmtReader(Charset charset) throws Exception {
		
		if(charset != null) {
			this.charset = charset;
		} else {
			this.charset = Charset.defaultCharset();
		}
	}
	
	/**
	 * 생성자
	 */
	public LogfmtReader() throws Exception {
		this(Charset.defaultCharset());
	}

	@Override
	public void trim(byte[] buffer, Consumer<Map<String, Object>> action) throws Exception {
	}
}
