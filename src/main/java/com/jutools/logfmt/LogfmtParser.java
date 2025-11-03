package com.jutools.logfmt;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

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
	static enum Status {
		
		START(true),
		KEY(false),
		SET(true),
		STRING(false),
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

	
	/**
	 * 생성자
	 * 
	 * @param reader
	 */
	public LogfmtParser() throws Exception {
		
	}
	
	/**
	 * Logfmt 메시지 파싱 수행
	 * 
	 * @param reader 메시지 Reader 객체
	 * @return 파싱 맵 객체
	 */
	public Map<String, Object> parse(Reader reader) throws Exception {
		
		// 입력값 검증
		if(reader == null) {
			throw new IllegalArgumentException("'reader' is null.");
		}
		
		// 파싱 결과 저장 맵
		Map<String, Object> map = new HashMap<>();
		
		// 파싱 상태 변수
		Status status = Status.START;
		
		// 임시 저장 변수
		StringBuilder buffer = new StringBuilder("");
		
		// 맵에 저장할 키 변수
		String key = null;
		
		// 리더에서 한문자씩 일어와서 파싱 수행
		int read = reader.read();
		while(read != -1) {
			
			// 즉시 종료 상태이면 중지
			if(status == Status.END || status == Status.ERROR) {
				break;
			}
			
			char ch = (char)read;
			
			// 각 상태별 읽은 데이터 처리
			switch(status) {
			
			case START:
				
				if(ch == '\n') {
					
					status = Status.END;
					
				} else if(ch != ' ' && ch != '\t' && ch != '\r') {
					
					buffer.append(ch);
					status = Status.KEY;
				}
				
				break;
				
			case KEY:

				if(ch == '=') {
					
					key = buffer.toString();
					buffer.delete(0, buffer.length());
					
					status = Status.SET;
					
				} else if(ch == '\n') {
					
					status = Status.ERROR;
					
				} else {
					
					buffer.append(ch);
				}
				
				break;
				
			case SET:
				
				if(ch == '"') {
					
					status = Status.STRING;
					
				} else if(ch >= '0' && ch <= '9') {
					
					buffer.append(ch);
					status = Status.INTEGER;
					
				} else if(ch == '\n') {
					
					status = Status.END;
					
				} else if(ch == '\r' || ch == ' ' || ch == '\t'){
					// Do nothing
				} else {
					
					buffer.append(ch);
					status = Status.VALUE;
				}
				
				break;
				
			case STRING:
				
				if(ch == '"') {
					
					// 맵 추가 및 버퍼 클리어
					map.put(key, buffer.toString());
					buffer.delete(0, buffer.length());
					
					status = Status.START;
					
				} else {
					
					buffer.append(ch);
				}
				
				break;
				
			case INTEGER:
				
				if(ch >= '0' && ch <= '9') {
					
					buffer.append(ch);
					
				} else if(ch == '.') {
					
					buffer.append(ch);
					status = Status.DOUBLE;
					
				} else if(ch == ' ' || ch == '\t' || ch == '\r') {
					
					// 맵 추가 및 버퍼 클리어
					map.put(key, Integer.parseInt(buffer.toString()));
					buffer.delete(0, buffer.length());
					
					status = Status.START;
					
				} else if(ch == '\n') {
					
					// 맵 추가 및 버퍼 클리어
					map.put(key, Integer.parseInt(buffer.toString()));
					buffer.delete(0, buffer.length());
					
					status = Status.END;
					
				} else {
					
					status = Status.VALUE;
				}
				
				break;
				
			case DOUBLE:
				
				if(ch >= '0' && ch <= '9') {
					
					buffer.append(ch);
					
				} else if(ch == ' ' || ch == '\t' || ch == '\r') {
					
					// 맵 추가 및 버퍼 클리어
					map.put(key, Double.parseDouble(buffer.toString()));
					buffer.delete(0, buffer.length());
					
					status = Status.START;
					
				} else if(ch == '\n') {
					
					// 맵 추가 및 버퍼 클리어
					map.put(key, Double.parseDouble(buffer.toString()));
					buffer.delete(0, buffer.length());
					
					status = Status.END;
					
				} else {
					
					status = Status.VALUE;
				}
				
				break;
				
			case VALUE:
				
				if(ch == ' ' || ch == '\t' || ch == '\r') {
					
					// 맵 추가 및 버퍼 클리어
					map.put(key, buffer.toString());
					buffer.delete(0, buffer.length());
					
					status = Status.START;
					
				} else if(ch == '\n') {
					
					// 맵 추가 및 버퍼 클리어
					map.put(key, buffer.toString());
					buffer.delete(0, buffer.length());
					
					status = Status.END;
					
				} else {
					
					buffer.append(ch);
				}
				
				break;
				
			default:
				throw new Exception("invalid status: " + status);
			}
			
			// 새로운 문자를 읽음
			read = reader.read();
		}
		
		// buffer 에 데이터가 있다면(갑자기 종료된 경우)
		// map 에 추가함
		if(buffer.length() != 0) {
			
			if(status == Status.STRING || status == Status.VALUE) {
				map.put(key, buffer.toString());
			} else if(status == Status.INTEGER) {
				map.put(key, Integer.parseInt(buffer.toString()));
			} else if(status == Status.DOUBLE) {
				map.put(key, Double.parseDouble(buffer.toString()));
			} 
		}
		
		// 종료 상태인지 확인
		if(status.isEndStatus() == false) {
			throw new Exception("invalid end status: " + status);
		}
		
		return map;
	}
	
	/**
	 * Logfmt 문자열 파싱
	 * 
	 * @param log 로그 메시지 문자열
	 * @return 파싱 맵 객체
	 */
	public Map<String, Object> parse(String log) throws Exception {
		
		if(log == null) {
			throw new IllegalArgumentException("'log' is null.");
		}
		
		return this.parse(new StringReader(log));
	}
}
