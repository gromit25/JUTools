package com.jutools.filetracker;

import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

import com.jutools.BytesUtil;

/**
 * 
 * 2025-05-05T17:44:50.276+09:00  INFO 84760 --- [app] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
 * 
 * @author jmsohn
 */
public class LogReader implements PauseReader {
	
	/** 파일을 읽을 때, 문자 인코딩 방식 */
	private Charset charset = Charset.defaultCharset();
	
	/** 끝나지 않은 데이터 임시 저장 변수 */
	private byte[] temp = null;
	
	/**
	 * 생성자
	 * 
	 * @param charset character set
	 */
	public LogReader(Charset charset) throws Exception {
		
		if(charset != null) {
			this.charset = charset;
		}
	}
	
	/**
	 * 생성자
	 */
	public LogReader() throws Exception {
		this(null);
	}

	@Override
	public synchronized void read(byte[] buffer, Consumer<String> action) throws Exception {
		
		// 데이터 끝에 개행이 있는지 확인
		boolean isEndsWithNewLine = BytesUtil.endsWith(buffer, "\n".getBytes());

		// 개행으로 데이터를 한 문장씩 split함
		List<byte[]> messages = BytesUtil.split(buffer, "\n".getBytes());
		for(int index = 0; index < messages.size(); index++) {
			
			// 한문장을 가져옴
			byte[] message = messages.get(index);
			
			// 새로운 메시지 여부 반환
			boolean isNewMessage = isNewMessage(message);
			
			// 새로운 메시지(즉, 시간으로 메시지가 시작할 경우) 여부에 따라 분기
			if(isNewMessage == true) {
				
				// 새로운 메시지 시작
				// 이전 저장된 메시지를 action에서 처리함
				if(this.temp != null) {
					action.accept(new String(this.temp, this.charset));
				}
				
				// 현재 메시지는 저장
				this.temp = message;
				
			} else {

				// 새로운 메시지가 아닐 경우
				// 기존 메시지에 추가
				this.temp = BytesUtil.concat(this.temp, message);
				
				// 만일 읽은 버퍼의 끝이 '\n' 으로 끝나거나 마지막라인이 아닌 경우 '\n'을 추가함
				if(isEndsWithNewLine == true || index - 1 != messages.size()) { 
					this.temp = BytesUtil.concat(this.temp, "\n".getBytes());
				}
			}
			
		} // end of for index
	}

	/**
	 * 새로운 메시지 여부 반환
	 * 
	 * @param message 메시지
	 * @return 새로운 메시지 여부
	 */
	private static boolean isNewMessage(byte[] message) throws Exception {
		
		// 새로운 메시지 여부는 메시지의 처음 시작이 시간인지 확인하여 반환
		// ex)
		// 2025-05-05T17:44:50.276+09:00
		//
		// 0 : 년도-1
		// 1 : 년도-2
		// 2 : 년도-3
		// 3 : 년도-4
		// 4 : 년도와 월 사이의 -
		// 5 : 월-1
		// 6 : 월-2
		// 7 : 월과 일 사이의 -
		// 8 : 일-1
		// 9 : 일-2
		// 10 : 월과 시 사이의 T
		// 11 : 시-1
		// 12 : 시-2
		// 13 : 시와 분 사이의 :
		// 14 : 분-1
		// 15 : 분-2
		// 16 : 분과 초 사이의 :
		// 17 : 초-1
		// 18 : 초-2
		// 19 : 초와 밀리세컨드 사이의 .
		// 20 : 밀리세컨드-1
		// 21 : 밀리세컨드-2
		// 22 : 밀리세컨드-3
		// 23 : 성공
		//
		// 0,1,2,3,5,6,8,9,11,12,14,15,17,18,20,21,22 -> [0-9](0x30-0x39)
		// 4,7 -> '-'(0x2D)
		// 10 -> 'T'(0x54)
		// 13, 16 -> ':'(0x3A)
		// 19 -> '.'(0x2E)
		
		if(message.length < 23) {
			
			return false;
			
		} else {
		
			for(int byteIndex = 0; byteIndex < message.length; byteIndex++) {
				
				// 현재 위치에 따라 나타나야할 문자를 확인
				switch(byteIndex) {
				
				case 4:
				case 7:
					// '-'(0x2D)
					if(message[byteIndex] != 0x2D) {
						return false;
					}
					break;
					
				case 10:
					// 'T'(0x54)
					if(message[byteIndex] != 0x54) {
						return false;
					}
					break;
					
				case 13:
				case 16:
					// ':'(0x3A)
					if(message[byteIndex] != 0x3A) {
						return false;
					}
					break;
					
				case 19:
					// '.'(0x2E)
					if(message[byteIndex] != 0x2E) {
						return false;
					}
					break;
					
				default:
					// [0-9](0x30-0x39)
					if(message[byteIndex] < 0x30 || message[byteIndex] > 0x39) {
						return false;
					}
					break;
				}
			} // end of for byteIndex
			
			return true;
		} // end of if
	}
}
