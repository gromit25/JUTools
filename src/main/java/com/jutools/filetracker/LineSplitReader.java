package com.jutools.filetracker;

import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

import com.jutools.BytesUtil;

/**
 * line 구분자로 끊어 읽기 Reader
 * 
 * @author jmsohn
 */
public class LineSplitReader implements SplitReader<String> {
	
	/** \n 의 byte array */
	private static byte[] LINE_FEED = "\n".getBytes();
	
	/** \r 의 byte array */
	private static byte[] CARRAGE_RETURN = "\r".getBytes();
	
	/** 파일을 읽을 때, 문자 인코딩 방식 */
	private Charset charset;
	
	/** 끝나지 않은 데이터 임시 저장 변수 */
	byte[] temp = null;
	
	/**
	 * 생성자
	 * 
	 * @param charset character set
	 */
	public LineSplitReader(Charset charset) throws Exception {
		
		if(charset != null) {
			this.charset = charset;
		} else {
			this.charset = Charset.defaultCharset();
		}
	}
	
	/**
	 * 생성자
	 */
	public LineSplitReader() throws Exception {
		this(null);
	}

	@Override
	public synchronized void read(byte[] buffer, Consumer<String> action) throws Exception {
		
		// 데이터 끝에 lineSeparator가 있는지 확인
		boolean isEndsWithLineFeed = BytesUtil.endsWith(buffer, LINE_FEED);

		// lineSeparator로 데이터를 한 문장씩 split함
		List<byte[]> messages = BytesUtil.split(buffer, LINE_FEED);
		for(int index = 0; index < messages.size(); index++) {

			byte[] message = messages.get(index);

			// 임시 저장된 데이터가 있을 경우
			// 데이터 합친 뒤 임시 저장 초기화
			if(index == 0 && this.temp != null) {
				
				message = BytesUtil.concat(this.temp, message);
				this.temp = null;
			}

			// "\n" 를 통해 잘린 데이터는
			// 날짜 포맷 후 logMessageArr 에 추가
			if(index != messages.size() - 1 || isEndsWithLineFeed == true) {
				
				// 만일 마지막 문자가 "\r" 일 경우 제외 시킴
				if(BytesUtil.endsWith(message, CARRAGE_RETURN) == true) {
					message = BytesUtil.cut(message, 0, message.length - 1);
				}
				
				// 사용자 처리 메소드에서 데이터를 처리함
				action.accept(new String(message, this.charset));
				
			} else {
				
				// 데이터가 끝나지 않았을 경우 임시 저장
				this.temp = message;
			}
		}
	}
}
