package com.jutools.logfmt;

import java.util.HashMap;
import java.util.Map;

import com.jutools.StringUtil;

/**
 * Logfmt 유틸리티 클래스
 * 
 * @author jmsohn
 */
public class LogfmtUtil {
	
	/**
	 * Logfmt 포맷의 로그를 맵 형태로 반환
	 * 
	 * @param log Logfmt 포맷의 로그
	 * @return 파싱된 맵
	 */
	public static Map<String, Object> toMap(String log) throws Exception {

		// Logfmt 로그를 파싱한 맵
		Map<String, Object> map = new HashMap<>();
		
		if(StringUtil.isBlank(log) == true) {
			return map;
		}
		
		// 파서 생성
		LogfmtParser parser = new LogfmtParser((rowMap) -> {
			// 한 줄이 파싱 되고 나면 해당 로우의 정보를 추가함
			map.putAll(rowMap);
		});
		
		// 한 줄이 끝났음을 표시하기 위해 리턴을 추가
		log = (log.endsWith("\n") == true)?log:log + "\n";
		
		// 파싱을 위해 피드
		parser.feed(log);
		
		// 종료 처리 수행
		parser.finalize();
		
		return map;
	}
	
	/**
	 * Logfmt 로그 문자열로 출력
	 * 
	 * @param map 변환할 맵
	 * @return 로그 문자열
	 */
	public static String toString(Map<String, Object> map) {
		
		StringBuilder builder = new StringBuilder("");
		
		for(String key: map.keySet()) {
			
			if(builder.length() != 0) {
				builder.append(" ");
			}
			
			// key 추가
			builder
				.append(key)
				.append("=");
			
			// value 추가
			String value = map.get(key).toString();
			
			if(value.matches(".*[\\s\"].*") == true) {
				
				value = value.replace("\"", "\\\"");
				
				builder
					.append("\"")
					.append(value)
					.append("\"");
				
			} else {
				builder.append(value);
			}
		}
		
		return builder.toString();
	}
}
