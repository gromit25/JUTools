package com.jutools.logfmt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.jutools.StringUtil;

import lombok.Getter;

/**
 * Logfmt 파서 클래스
 * 
 * @author jmsohn
 */
public class LogfmtParser {
	
	/**
	 * 파싱 상태
	 * 
	 * @author jmsohn
	 */
	private static enum Status {
		
		START(true),
		KEY(false),
		SET(true),
		STRING(false),
		ESCAPE(false),
		MINUS(true),
		INTEGER(true),
		DOUBLE(true),
		VALUE(true),
		END(true),
		ERROR(true);
		
		// ----------------
		
		/** 종료 상태 여부 */
		@Getter
		private boolean endStatus;
		
		/**
		 * 생성자
		 * 
		 * @param endStatus 종료 상태 여부
		 */
		Status(boolean endStatus) {
			this.endStatus = endStatus;
		}
	}
	
	/** 파싱 결과 저장 맵 */
	private Map<String, Object> map = new HashMap<>();
	
	/** 파싱 상태 변수 */
	private Status status = Status.START;
	
	/**  임시 저장 변수 */
	private StringBuilder buffer = new StringBuilder("");
	
	/** 파싱 결과 처리를 위한 콜백 메소드 */
	private Consumer<Map<String, Object>> consumer;
	
	/** 맵에 저장할 키 변수 */
	String key = null;
	

	/**
	 * 생성자
	 * 
	 * @param consumer 파싱 결과 처리를 위한 콜백 메소드
	 */
	public LogfmtParser(Consumer<Map<String, Object>> consumer) throws Exception {
		
		if(consumer == null) {
			throw new IllegalArgumentException("'consumer' is null.");
		}
		
		this.consumer = consumer;
	}

	/**
	 * Logfmt 메시지 피드 수행
	 * 
	 * @param log 읽은 문자열
	 */
	public void feed(String log) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isEmpty(log) == true) {
			return;
		}
		
		// 리더에서 한문자씩 일어와서 파싱 수행
		for(int index = 0; index < log.length(); index++) {
			
			// 현재 문자를 읽음
			char ch = log.charAt(index);
			
			// 각 상태별 읽은 데이터 처리
			switch(this.status) {
			
			case START:
				
				if(ch == '\n') {
					
					this.status = Status.END;
					
				} else if(ch != ' ' && ch != '\t' && ch != '\r') {
					
					this.buffer.append(ch);
					this.status = Status.KEY;
				}
				
				break;
				
			case KEY:

				if(ch == '=') {
					
					key = this.buffer.toString();
					this.buffer.delete(0, buffer.length());
					
					this.status = Status.SET;
					
				} else if(ch == '\n') {
					
					this.status = Status.ERROR;
					
				} else {
					
					this.buffer.append(ch);
				}
				
				break;
				
			case SET:
				
				if(ch == '"') {
					
					this.status = Status.STRING;
					
				} else if(ch == '-') {
					
					this.buffer.append(ch);
					this.status = Status.MINUS;
					
				} else if(ch >= '0' && ch <= '9') {
					
					this.buffer.append(ch);
					this.status = Status.INTEGER;
					
				} else if(ch == '\n') {
					
					this.status = Status.END;
					
				} else if(ch == '\r' || ch == ' ' || ch == '\t'){

					this.map.put(key, buffer.toString());
					this.status = Status.START;
					
				} else {
					
					this.buffer.append(ch);
					this.status = Status.VALUE;
				}
				
				break;
				
			case STRING:
				
				if(ch == '"') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, this.buffer.toString());
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.START;
					
				} else if(ch == '\\'){
					
					this.status = Status.ESCAPE;
					
				} else {
					
					this.buffer.append(ch);
				}
				
				break;
			
			case ESCAPE:
				
				this.buffer.append(getEscapeChar(ch));
				this.status = Status.STRING;
				
				break;
				
			case MINUS:
				
				if(ch >= '0' && ch <= '9') {
					
					this.buffer.append(ch);
					this.status = Status.INTEGER;
					
				} else if(ch == ' ' || ch == '\t' || ch == '\r') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, this.buffer.toString());
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.START;
					
				} else if(ch == '\n') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, this.buffer.toString());
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.END;
					
				} else {
					
					this.buffer.append(ch);
					this.status = Status.VALUE;
				}
				
				break;
				
			case INTEGER:
				
				if(ch >= '0' && ch <= '9') {
					
					this.buffer.append(ch);
					
				} else if(ch == '.') {
					
					this.buffer.append(ch);
					this.status = Status.DOUBLE;
					
				} else if(ch == ' ' || ch == '\t' || ch == '\r') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, Integer.parseInt(this.buffer.toString()));
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.START;
					
				} else if(ch == '\n') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, Integer.parseInt(this.buffer.toString()));
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.END;
					
				} else {
					
					this.status = Status.VALUE;
				}
				
				break;
				
			case DOUBLE:
				
				if(ch >= '0' && ch <= '9') {
					
					buffer.append(ch);
					
				} else if(ch == ' ' || ch == '\t' || ch == '\r') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, Double.parseDouble(this.buffer.toString()));
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.START;
					
				} else if(ch == '\n') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, Double.parseDouble(this.buffer.toString()));
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.END;
					
				} else {
					
					this.status = Status.VALUE;
				}
				
				break;
				
			case VALUE:
				
				if(ch == ' ' || ch == '\t' || ch == '\r') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, this.buffer.toString());
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.START;
					
				} else if(ch == '\n') {
					
					// 맵 추가 및 버퍼 클리어
					this.map.put(key, this.buffer.toString());
					this.buffer.delete(0, this.buffer.length());
					
					this.status = Status.END;
					
				} else {
					
					this.buffer.append(ch);
				}
				
				break;
				
			default:
				throw new IllegalStateException("invalid status: " + this.status);
			}
			
			// 종료 상태이면 컨슈머 호출
			if(this.status == Status.END) {
				this.consumer.accept(this.map);
				this.status = Status.START;
			}
			
			// 오류 상태이면 중지
			if(this.status == Status.ERROR) {
				throw new IllegalStateException("invalid status: " + this.status);
			}
		} // End of for
	}
	
	/**
	 * 이스케이프 문자에 해당하는 문자 반환
	 * 
	 * @param ch 이스케이프 문자
	 * @return 치환 문자
	 */
	private char getEscapeChar(char ch) {
		
		switch(ch) {
		case 't':
			return '\t';
		case 'r':
			return '\r';
		case 'n':
			return '\n';
		case '0':
			return '\0';
		default:
			return ch;
		}
	}
	
	/**
	 * 최종 정리 작업 수행
	 */
	public void finalize() throws Exception {
		
		// buffer 에 데이터가 있다면(갑자기 종료된 경우)
		// map 에 추가함
		if(this.buffer.length() != 0) {
			
			if(this.status == Status.VALUE || this.status == Status.MINUS) {
				
				this.map.put(this.key, this.buffer.toString());
				this.consumer.accept(map);
				
			} else if(this.status == Status.INTEGER) {
				
				this.map.put(this.key, Integer.parseInt(this.buffer.toString()));
				this.consumer.accept(map);
				
			} else if(this.status == Status.DOUBLE) {
				
				this.map.put(this.key, Double.parseDouble(this.buffer.toString()));
				this.consumer.accept(map);
			} 
		}
		
		// 종료 상태인지 확인
		if(this.status.isEndStatus() == false) {
			throw new IllegalStateException("invalid end status: " + this.status);
		}
		
		// 초기화 수행
		this.map = new HashMap<>();
		this.status = Status.START;
		this.buffer = new StringBuilder("");
	}
}
