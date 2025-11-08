package com.jutools.filetracker.trimmer;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Consumer;

import com.jutools.filetracker.Trimmer;
import com.jutools.logfmt.LogfmtParser;

/**
 * 
 * 
 * @author jmsohn
 */
public class LogfmtTrimmer implements Trimmer<Map<String, Object>>{
	
	
	/** 파일을 읽을 때, 문자 인코딩 방식 */
	private Charset charset;

	/** */
	private LogfmtParser parser;

	
	/**
	 * 생성자
	 * 
	 * @param charset character set
	 */
	public LogfmtTrimmer(Charset charset) throws Exception {
		
		if(charset != null) {
			this.charset = charset;
		} else {
			this.charset = Charset.defaultCharset();
		}
	}
	
	/**
	 * 생성자
	 */
	public LogfmtTrimmer() throws Exception {
		this(Charset.defaultCharset());
	}

	@Override
	public void trim(byte[] buffer) throws Exception {
		this.parser.feed(new String(buffer, this.charset));
	}

	@Override
	public void setConsumer(Consumer<Map<String, Object>> consumer) throws Exception {
		this.parser = new LogfmtParser(consumer);
	}
}
