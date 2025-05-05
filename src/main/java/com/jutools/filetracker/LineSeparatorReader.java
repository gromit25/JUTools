package com.jutools.filetracker;

import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

import com.jutools.BytesUtil;
import com.jutools.StringUtil;

/**
 * line 구분자로 끊어 읽기 Reader
 * 
 * @author jmsohn
 */
public class LineSeparatorReader implements PauseReader {
	
	/** line 구분자 */
	private byte[] lineSeparator;
	
	/** 파일을 읽을 때, 문자 인코딩 방식 */
	private Charset charset = Charset.defaultCharset();
	
	/** 끝나지 않은 데이터 임시 저장 변수 */
	byte[] temp = null;
	
	/**
	 * 생성자
	 * 
	 * @param lineSeparator line 구분자
	 * @param charset character set
	 */
	public LineSeparatorReader(String lineSeparator, Charset charset) throws Exception {
		
		if(StringUtil.isEmpty(lineSeparator) == true) {
			throw new IllegalArgumentException("line separator is blank.");
		}
		
		if(charset != null) {
			this.charset = charset;
		}
		
		this.lineSeparator = lineSeparator.getBytes();
	}
	
	/**
	 * 생성자
	 * 
	 * @param lineSeparator line 구분자
	 */
	public LineSeparatorReader(String lineSeparator) throws Exception {
		this(lineSeparator, null);
	}

	@Override
	public void read(Consumer<String> action, byte[] buffer) throws Exception {
		
		// 데이터 끝에 lineSeparator가 있는지 확인
		boolean isEndsWithLineSeparator = BytesUtil.endsWith(buffer, this.lineSeparator);

		// lineSeparator로 데이터를 한 문장씩 split함
		List<byte[]> messages = BytesUtil.split(buffer, this.lineSeparator);
		for(int index = 0; index < messages.size(); index++) {

			byte[] message = messages.get(index);

			// 임시 저장된 데이터가 있을 경우
			// 데이터 합친 뒤 임시 저장 초기화
			if(index == 0 && this.temp != null) {
				
				message = BytesUtil.concat(this.temp, message);
				this.temp = null;
			}

			// lineSeparator 를 통해 잘린 데이터는
			// 날짜 포맷 후 logMessageArr 에 추가
			if(index != messages.size() - 1 || isEndsWithLineSeparator == true) {
				
				// 람다 함수에서 데이터를 처리함
				action.accept(new String(message, this.charset));
				
			} else {
				
				// 데이터가 끝나지 않았을 경우 임시 저장
				this.temp = message;
			}
		}
	}
}
