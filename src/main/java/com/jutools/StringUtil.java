package com.jutools;

/**
 * 문자열 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class StringUtil {
	
	/**
	 * 주어진 문자열에 대한 이스케이프 처리
	 * 
	 * @param str 주어진 문자열
	 * @return 이스케이프 처리된 문자열
	 */
	public static String escape(String str) {
		
		// 입력값 검증
		if(str == null) {
			return null;
		}
		
		// 이스케이프 처리된 문자열 버프 변수
		StringBuffer buffer = new StringBuffer("");
		
		// 이스케이프 처리를 위한 상태 변수
		// 0: 문자열, 1: 이스케이프 문자 
		int status = 0;
		
		for(int index = 0; index < str.length(); index++) {
			
			char ch = str.charAt(index);
			
			if(status == 0) {
				
				if(ch == '\\') {
					status = 1;
				} else {
					buffer.append(ch);
				}
				
			} else {
				
				if(ch == 't') {
					buffer.append('\t');
				} else if(ch == 'r') {
					buffer.append('\r');
				} else if(ch == 'n') {
					buffer.append('\n');
				} else {
					buffer.append(ch);
				}
				
				status = 0;
			}
		} // End of for
		
		return buffer.toString();
	}
}
