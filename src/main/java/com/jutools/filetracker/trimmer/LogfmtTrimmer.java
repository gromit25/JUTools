package com.jutools.filetracker.trimmer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
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
	
	/** 끝나지 않은 데이터 임시 저장 변수 */
	private byte[] buffer = null;

	
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
	public void trim(byte[] buffer, Consumer<Map<String, Object>> action) throws Exception {
		
		TrackingLogInputStream is = new TrackingLogInputStream();
		Reader reader = new InputStreamReader(is, this.charset);
		
	}
}

/**
 * 
 * 
 * @author jmsohn
 */
class TrackingLogInputStream extends InputStream {
	
	/** */
	private BlockingQueue<byte[]> logQueue = new LinkedBlockingQueue<byte[]>();
	
	/** */
	private byte[] buffer = new byte[0];
	
	/** */
	private int pos = Integer.MAX_VALUE;
	
	
	@Override
	public synchronized int read() throws IOException {
		
		//
		if(this.pos >= this.buffer.length) {
			
			try {
				
				this.buffer = null;
				
				while(this.buffer == null) {
					this.buffer = this.logQueue.poll(10, TimeUnit.SECONDS);
				}
				
				this.pos = 0;
				
			} catch(Exception ex) {
				throw new IOException(ex);
			}
		}

		//
		int read = (int)this.buffer[this.pos];
		
		//
		this.pos++;
		
		return read;
	}
	
	/**
	 * 
	 * 
	 * @param log
	 */
	public void put(byte[] log) throws Exception {
		
		if(log == null) {
			return;
		}
		
		this.logQueue.put(log);
	}
}
