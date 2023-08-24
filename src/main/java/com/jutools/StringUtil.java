package com.jutools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 문자열 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class StringUtil {
	
	/** html entity 변환 맵(replaceHtmlEntity) */ 
	private static Map<Character, String> htmlEntityMap;
	/** 유효한 확장자 모음 */
	private static String[] defaultValidExts;
	
	static {
		
		/* html entity 변환 맵 초기화 */ 
		htmlEntityMap = new HashMap<Character, String>();
		
		htmlEntityMap.put('&', "&amp;");
		htmlEntityMap.put('<', "&lt;");
		htmlEntityMap.put('>', "&gt;");
		htmlEntityMap.put('"', "&quot;");
		htmlEntityMap.put('\'', "&#x27;");
		htmlEntityMap.put('/', "&#x2F;");
		htmlEntityMap.put('(', "&#x28;");
		htmlEntityMap.put(')', "&#x29;");
		
		/* 유효한 확장자 모음 초기화 */
		defaultValidExts = new String[] {

			// 텍스트 파일 확장자
			".txt", ".rtf",
	
			// 엑셀 파일 확장자
			".csv", ".xls", ".xlsx", ".xlt", ".xltx", ".xltm", ".xlw",
	
			// 파워포인트 파일 확장자
			".ppt", ".pptx",
	
			// 워드 파일 확장자
			".doc", ".docx", ".docm", ".dot", ".dotx", ".dotm",
	
			// 아래한글 파일 확장자
			".hwp", ".hwpx",
	
			// pdf 파일 확장자
			".pdf",
	
			// 이미지 파일 확장자
			".jpg", ".jpeg", ".tiff", ".gif", ".bmp", ".png",
	
			// 동영상 파일 확장자
			".mp3", ".mp4", ".mov", ".wmv", ".avi", ".mpeg"
		};
	}
	
	/**
	 * 주어진 문자열에 대한 이스케이프 처리
	 * 
	 * @param str 주어진 문자열
	 * @return 이스케이프 처리된 문자열
	 */
	public static String escape(String str) throws Exception {
		
		// 입력값 검증
		if(str == null) {
			return null;
		}
		
		// 이스케이프 처리된 문자열 변수
		StringBuilder escapedStr = new StringBuilder("");
		
		// 유니코드 임시 저장 변수
		StringBuilder unicodeStr = new StringBuilder(""); 
		
		// 이스케이프 처리를 위한 상태 변수
		// 0: 문자열, 1: 이스케이프 문자,
		// 11:유니코드 1번째 문자, 12:유니코드 2번째 문자, 13:유니코드 3번째 문자, 14:유니코드 4번째 문자 
		int status = 0;
		
		for(int index = 0; index < str.length(); index++) {
			
			char ch = str.charAt(index);
			
			if(status == 0) {
				
				if(ch == '\\') {
					status = 1;
				} else {
					escapedStr.append(ch);
				}
				
			} else if(status == 1) {
				
				// 상태를 일반 문자열 상태로 설정
				// 먼저 상태를 변경하는 이유는 Unicode 시작시 상태가 10으로 변경하기 때문에
				// 마지막에 상태를 변경하면 안됨
				status = 0;
				
				if(ch == '0') {
					escapedStr.append('\0'); // ASCII 0 추가
				} else if(ch == 'b') {
					escapedStr.append('\b');
				} else if(ch == 'f') {
					escapedStr.append('\f');
				} else if(ch == 'n') {
					escapedStr.append('\n');
				} else if(ch == 'r') {
					escapedStr.append('\r');
				} else if(ch == 't') {
					escapedStr.append('\t');
				} else if(ch == 'u') {
					// Unicode 시작
					status = 11;
				} else {
					// 없을 경우 해당 문자를 그냥 추가함
					// ex) \' 인경우 '를 추가
					escapedStr.append(ch);
				}
				
			} else if(status >= 10 && status <= 14) {
				
				// ch가 16진수 값(0-9, A-F, a-f) 인지 확인
				if(isHex(ch) == false) {
					throw new Exception("unicode value is invalid:" + ch);
				}
				
				// unicode 버퍼에 ch추가
				unicodeStr.append(ch);

				// 상태값을 하나 올림
				// ex) 10:유니코드 시작 -> 11:유니코드 1번째 문자
				status++;
				
				// Unicode escape가 종료(status가 15 이상)되면
				// Unicode를 추가하고, 상태를 일반문자열 상태로 변경함
				if(status >= 15) {
					
					char unicodeCh = (char)Integer.parseInt(unicodeStr.toString(), 16);
					escapedStr.append(unicodeCh);
					
					unicodeStr.delete(0, unicodeStr.length());
					status = 0;
				}
				
			} else {
				throw new Exception("Unexpected status: " + status);
			}
		} // End of for
		
		return escapedStr.toString();
	}
	
	/**
	 * 주어진 문자(ch)가 16진수 값(0-9, A-F, a-f) 인지 확인
	 * 
	 * @param ch 검사할 문자
	 * @return 16진수 값 여부(16진수 값일 경우 true, 아닐 경우 false)
	 */
	private static boolean isHex(char ch) {
		return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
	}
	
	/**
	 * "호스트:포트" 형태의 문자열을 {"호스트", "포트"} 문자열 배열로 파싱하여 반환
	 * 
	 * @param hostStr "호스트:포트" 형태의 문자열
	 * @return {"호스트", "포트"} 문자열 배열
	 */
	public static String[] parseHostPort(String hostStr) throws Exception {
		
		String hostPatternStr = "(?<hostname>[a-zA-Z0-9\\-\\_]+(\\.[a-zA-Z0-9\\-\\_]+)*)\\:(?<port>[0-9]+)";
		
		Pattern hostPattern = Pattern.compile(hostPatternStr);
		Matcher hostMatcher = hostPattern.matcher(hostStr);
		
		if(hostMatcher.matches() == false) {
			throw new Exception("host string is not matched:" + hostStr);
		}
		
		String hostName = hostMatcher.group("hostname");
		String port = hostMatcher.group("hostname");
		
		return new String[] {hostName, port};
	}
	
	/**
	 * 문자열의 html 엔터티(<>& 등 -> &amp;lt;&amp;gt;&amp;amp; 등)를 변경 
	 * 
	 * @param contents 문자열
	 * @return 변경된 문자열
	 */
	public static String replaceHtmlEntity(String contents) throws Exception {
		
		if(contents == null || contents.isEmpty() == true) {
			return contents;
		}
		
		StringBuilder replacedContents = new StringBuilder("");
		for(int index = 0; index < contents.length(); index++) {
			
			char ch = contents.charAt(index);
			
			if(htmlEntityMap.containsKey(ch) == true) {
				replacedContents.append(htmlEntityMap.get(ch));
			} else {
				replacedContents.append(ch);
			}
		}
		
		return replacedContents.toString();
	}
	
	/**
	 * 문자열(contents) 내에 "(\r)\n" -> "&lt;br&gt;\r\n"로 변경하는 메소드  
	 * 
	 * @param contents 문자열
	 * @return 대체된 문자열
	 */
	public static String replaceEnterToBr(String contents) throws Exception {
		
		if(contents == null) {
			throw new NullPointerException("contents is null");
		}
		
		return contents.replaceAll("(\\r)?\\n", "<br>\r\n");
	}
	
	/**
	 * 파일명이 유효한지 검증하는 메소드<br>
	 * 유효할 경우 true
	 * 
	 * @param fileName 검사할 파일명
	 * @param length 파일명의 최대 길이
	 * @param validExts 유효한 확장자 목록
	 * @return 파일명의 유효성 여부
	 */
	public static boolean isValidFileName(String fileName, int length, String... validExts) throws Exception {

		// 파일명이 null 일경우 false 반환
		if(fileName == null) {
			return false;
		}

		// 유효한 확장자가 없으면 false 반환
		if(validExts == null || validExts.length == 0) {
			return false;
		}

		// 파일명에 null(\0)가 있는 경우 false 반환
		// null을 중간에 삽입하여 확장자 체크를 우회하는 방법을 차단함
		// 정상 사용자가 파일명에 null을 넣을 이유가 없음
		if(hasNull(fileName) == true) {
			return false;
		}
		
		// 파일명이 주어진 길이보다 길 경우 false 반환
		// Overflow 방법등을 사전 차단
		// 단, length가 음수일 경우 체크하지 않음
		if(length >= 0 && fileName.length() > length) {
			return false;
		}
		
		// 확장자 체크
		// 유효한 확장자가 있는지 확인
		// 파일명과 확장자명을 뒤집어서 체크
		// 만일 유효한 확장자가 있다면, 위치는 0이 될 것임
		String rFileName = reverse(fileName);
		
		String[] rValidExts = new String[validExts.length];
		for(int index = 0; index < validExts.length; index++) {
			rValidExts[index] = reverse(validExts[index]);
		}
		
		for(int loc: find(rFileName, true, rValidExts)) {
			if(loc == 0) {
				return true;
			}
		}
		
		// 유효한 확장자 목록에 없으면 false를 반환
		return false;
	}
	
	/**
	 * 파일명이 유효한지 검증하는 메소드<br>
	 * 유효할 경우 true
	 * 
	 * @param fileName 검사할 파일명
	 * @param validExts 유효한 확장자 목록
	 * @return 파일명의 유효성 여부
	 */
	public static boolean isValidFileName(String fileName, String... validExts) throws Exception {
		return isValidFileName(fileName, -1, validExts);
	}
	
	/**
	 * 파일명이 유효한지 검증하는 메소드<br>
	 * 유효한 확장자 모음(StringUtil.defaultValidExts)에 있는 확장자인지 검사함<br>
	 * 유효할 경우 true
	 * 
	 * @param fileName 검사할 파일명
	 * @param length 파일명의 최대 길이
	 * @return 파일명의 유효성 여부
	 */
	public static boolean isValidFileName(String fileName, int length) throws Exception {
		return isValidFileName(fileName, length, defaultValidExts);
	}
	
	/**
	 * 파일명이 유효한지 검증하는 메소드<br>
	 * 유효한 확장자 모음(StringUtil.defaultValidExts)에 있는 확장자인지 검사함<br>
	 * 유효할 경우 true
	 * 
	 * @param fileName 검사할 파일명
	 * @return 파일명의 유효성 여부
	 */
	public static boolean isValidFileName(String fileName) throws Exception {
		return isValidFileName(fileName, -1);
	}
	
	/**
	 * 문자열 내에 null(\0)가 포함 여부 반환<br>
	 * 포함되어 있을 경우 true
	 * 
	 * @param contents 문자열
	 * @return null(\0) 포함 여부
	 */
	public static boolean hasNull(String contents) throws Exception {
		
		if(contents == null) {
			throw new Exception("contents is null");
		}
		
		for(int index = 0; index < contents.length(); index++) {
			char ch = contents.charAt(index);
			if(ch == '\0') {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 두 문자가 동일한지 비교<br>
	 * 대소문자 구분 여부를 확인하여 두 문자를 비교함
	 * 
	 * @param ch1 비교할 문자 1 
	 * @param ch2 비교할 문자 2
	 * @param ignoreCase 대소문자 구분 여부(true-구분하지 않음, false-구분함)
	 * @return 동일 여부
	 */
	public static boolean isEqualChar(char ch1, char ch2, boolean ignoreCase) {
		
		if(ignoreCase == false) {
			return ch1 == ch2;
		} else {
			return Character.toLowerCase(ch1) == Character.toLowerCase(ch2);
		}
	}
	
	/**
	 * 문자열 내 여러 문자열을 검색하는 메소드<br>
	 * -> 문자열을 한번만 읽어 수행 속도 향상 목적
	 * 
	 * @param contents 문자열
	 * @param ignoreCase 대소문자 구분 여부(true - 구분하지 않음, false - 구분함)  
	 * @param findStrs 검색할 문자열들
	 * @return 최초로 발견된 위치 목록(못찾은 경우 -1)
	 */
	public static int[] find(String contents, boolean caseSensitivity, String... findStrs) throws Exception {
		
		// 입력값 검증
		if(contents == null) {
			throw new NullPointerException("contents is null");
		}
		
		if(findStrs == null) {
			throw new NullPointerException("findStrs is null");
		}
		
		if(findStrs.length == 0) {
			return new int[]{};
		}
		
		// 검색 문자열들에 대한 정보 객체 변수 선언 및 초기화 수행
		ArrayList<FindStr> findStrObjs = new ArrayList<FindStr>(findStrs.length);
		for(int index = 0; index < findStrs.length; index++) {
			findStrObjs.add(new FindStr(findStrs[index], caseSensitivity));
		}
		
		// 대상 문자열을 한문자씩 읽어서 검색 수행
		for(int index = 0; index < contents.length(); index++) {
			
			char ch = contents.charAt(index);
			
			// 검색 문자열 별로 검색
			for(FindStr findStrObj: findStrObjs) {
				findStrObj.process(index, ch);
			}
		}
		
		// 검색 결과를 반환하기 위해 int 배열 형태로 변경
		int[] findLocs = new int[findStrObjs.size()];
		for(int index = 0; index < findLocs.length; index++) {
			findLocs[index] = findStrObjs.get(index).getFindLoc();
		}
		
		// 검색 결과 반환
		return findLocs;
	}
	
	/**
	 * find 메소드에서 사용할 검색 정보 클래스 
	 * 
	 * @author jmsohn
	 */
	@Data
	private static class FindStr {
		
		/** 검색해야할 문자열 */
		private String findStr;
		/** 대소문자 구분 여부(true-구분하지 않음, false-구분함) */
		private boolean ignoreCase;
		
		/** 최초 일치 위치 */
		private int findLoc;
		/**
		 * 검색 중인 문자열 위치 정보
		 * key - 일치 시작 위치, value - 문자열 내에 현재까지 일치하는 위치
		 */
		private Map<Integer, Integer> pins;
		
		/**
		 * 생성자
		 * 
		 * @param findStr 검색해야할 문자열
		 * @param ignoreCase 대소문자 구분 여부(true-구분하지 않음, false-구분함)
		 */
		FindStr(String findStr, boolean ignoreCase) throws Exception {
			
			if(findStr == null) {
				throw new NullPointerException("findStr is null");
			}
			
			this.setFindStr(findStr);
			this.setIgnoreCase(ignoreCase);
			this.setFindLoc(-1);	// 못찾은 경우 -1
			this.setPins(new HashMap<Integer, Integer>());
		}
		
		/**
		 * 입력된 문자에 대해 검색 수행
		 * -> 한문자씩 확인 작업 수행
		 * 
		 * @param index 검색 대상 문자열내에 현재 위치
		 * @param ch 입력된 문자
		 */
		void process(int index, char ch) {
			
			// 이미 찾은 경우 더이상 검색을 수행하지 않음
			if(this.getFindLoc() != -1) {
				return;
			}

			// 문자가 일치하지 않는 경우
			// pin에서 삭제할 대상 목록
			Set<Integer> toRemove = new HashSet<Integer>();
			
			// 각 pin 들에 대해 주어진 문자(ch)와 검색 중인 문자(findCh) 일치 여부를 확인
			for(int startIndex: this.getPins().keySet()) {
				
				int findIndex = this.getPins().get(startIndex);
				char findCh = this.getFindStr().charAt(findIndex);
				
				if(StringUtil.isEqualChar(ch, findCh, this.ignoreCase) == true) {
					
					findIndex++;
					
					// 문자열 일치하는 경우
					// -> findLoc 설정 후 종료
					// 문자열 일치하지 않는 경우
					// -> 하나 증가된 findIndex를 startIndex에 설정
					if(findIndex >= this.getFindStr().length()) {
						this.setFindLoc(startIndex);
						return;
					} else {
						this.getPins().put(startIndex, findIndex);
					}
					
				} else {
					
					// 문자가 일치하지 않을 경우 삭제 대상에 추가
					// 여기에서 삭제하면 for 문이 돌고 있는 중에 대상에 변화가 생겨 오류가 발생
					toRemove.add(startIndex);
				}
			}
			
			// pin 목록에서 pin 삭제
			for(Integer key: toRemove) {
				this.getPins().remove(key);
			}
			
			// 최초 문자와 일치하는 경우 새로운 pin 생성
			if(ch == this.getFindStr().charAt(0)) {
				this.getPins().put(index, 1);
			}
		}
	}
	
	/**
	 * 문자열 내에 검색할 문자열이 하나라도 있는지 확인하는 메소드
	 * 
	 * @param contents 문자열
	 * @param ignoreCase 대소문자 구분 여부(true - 구분하지 않음, false - 구분함)
	 * @param findStrs 검색할 문자열들
	 * @return 문자열 내에 검색할 문자열이 하나라도 있는지 여부
	 */
	public static boolean containsAny(String contents, boolean ignoreCase, String... findStrs) throws Exception {
		
		int[] indexes = find(contents, ignoreCase, findStrs);
		
		for(int index: indexes) {
			
			// 문자열이 있는 경우 true를 반환
			if(index >= 0) {
				return true;
			}
		}
		
		// 검색된 문자열이 없는 경우 false를 반환
		return false;
	}
	
	/**
	 * 여러 문자열을 구분자(delimiter)를 넣어 이어 붙히는 메소드
	 * 
	 * @param delimiter 문자열 목록 사이에 추가할 구분자(null 일경우 "")
	 * @param strs 문자열들
	 * @return 이어 붙힌 문자열
	 */
	public static String join(String delimiter, String... strs) throws Exception {
		
		// 입력값 검증
		if(delimiter == null) {
			delimiter = "";
		}
		
		if(strs == null) {
			return "";
		}
		
		// 문자열을 이어 붙히기 위한 StringBuilder 변수
		StringBuilder joinStr = new StringBuilder("");
		
		// 각 문자열들을 이어 붙힘
		for(int index = 0; index < strs.length; index++) {
			
			if(strs[index] == null) {
				throw new NullPointerException("strs array has null element at " + index);
			}
			
			// 문자열을 이어 붙힘
			// 단 마지막 문자열의 뒤에는 구분자(delimiter)를 붙히지 않음
			joinStr.append(strs[index]);
			if(index + 1 != strs.length) {
				joinStr.append(delimiter);
			}
		}
		
		return joinStr.toString();
	}
	
	/**
	 * 여러 객체의 toString()한 문자열을 구분자(delimiter)를 넣어 이어 붙히는 메소드
	 * 
	 * @param delimiter 구분자
	 * @param iter 여러 객체를 담고 있는 iterator
	 * @return 이어 붙힌 문자열
	 */
	public static String join(String delimiter, Iterable<?> iter) throws Exception {
		
		// iterator에서 문자열 목록을 만듦
		ArrayList<String> strs = new ArrayList<>();
		
		iter.forEach(obj -> {
			strs.add(obj.toString());
		});
		
		return join(delimiter, strs.stream().toArray(String[]::new));
	}
	
	/**
	 * int 배열을 구분자(delimiter)를 넣어 문자열로 만드는 메소드
	 * 
	 * @param delimiter int 목록 사이에 추가할 구분자(null 일경우 "")
	 * @param array 문자열로 변환할 int 배열
	 * @return 변환된 문자열
	 */
	public static String join(String delimiter, int... array) {
		
		if(delimiter == null) {
			delimiter = "";
		}
		
		if(array == null) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder();
		
		for(int index = 0; index < array.length; index++) {
			
			if(index != 0) {
				builder.append(delimiter);
			}
			
			builder.append(array[index]);
		}
		
		return builder.toString();
	}
	
	/**
	 * 문자열의 길이를 반환하는 메소드<br>
	 * -> 주어진 문자열이 null 일 경우 0을 반환함
	 * 
	 * @param str 문자열
	 * @return 문자열의 길이
	 */
	public static int length(String str) {
		
		if(str == null) {
			return 0;
		}
		
		return str.length();
	}

	/**
	 * 문자열이 비어 있는지 반환하는 메소드<br>
	 * -> 주어진 문자열이 null 일 경우 true를 반환함
	 * 
	 * @param str 문자열
	 * @return 문자열이 비어 있는지 여부
	 */
	public static boolean isEmpty(String str) {
		
		if(str == null) {
			return true;
		}
		
		return str.isEmpty();
	}

	/**
	 * 문자열을 역전 시켜 반환하는 메소드<br>
	 * ex) abc -> cba 
	 * 
	 * @param str 역전시킬 문자열
	 * @return 역전된 문자열
	 */
	public static String reverse(String str) throws Exception {
		
		if(str == null) {
			throw new NullPointerException("str is null");
		}
		
		return new StringBuilder(str).reverse().toString();
	}
	
	/**
	 * 문자열을 구분자 문자에 의해 나눔, 설정에 따라 나누어진 문자열에 trim, escape 처리함<br>
	 * 단, 문자열에 구분자가 escape 되어 있으면 구분하지 않음<br>
	 * ex) delimiter: ',' 이고<br>
	 *     str: "Test\, 입니다., 두번째 문장" 이면,<br>
	 *     "Test\, 입니다.", " 두번째 문장" 로 분리함
	 * 
	 * @param str 문자열
	 * @param delimiter 구분자
	 * @param isTrim 나누어진 문자열을 trim할 것인지 여부
	 * @param isEscape 나누어진 문자열을 escape할 것인지 여부
	 * @return 나누어진 문자열 목록
	 */
	public static String[] split(String str, char delimiter, boolean isTrim, boolean isEscape) throws Exception {
		
		// 입력값 검증
		if(str == null) {
			throw new NullPointerException("str is null");
		}
		
		// 나누어진 문자열들을 보관하는 변수 
		ArrayList<String> splitedStrs = new ArrayList<String>();
		// 문자열을 나누기 위한 임시 변수
		StringBuilder splitedStrBuffer = new StringBuilder("");
		// 이전 문자가 escape 문자였는지 여부
		boolean isEscapeChar = false;
		
		// 문자열을 끝까지 순회함
		for(int index = 0; index < str.length(); index++) {
			
			char ch = str.charAt(index);
			
			// escape 되지 않은 구분자일 경우, splitedStr을 splitedStrs에 추가함
			// 아닐 경우, splitedStr에 현재 문자를 추가
			if(ch == delimiter && isEscapeChar == false) {
				
				String splitedStr = splitedStrBuffer.toString();
				
				// trim 처리
				if(isTrim == true) {
					splitedStr = splitedStr.trim();
				}
				
				// escape 처리
				if(isEscape == true) {
					splitedStr = escape(splitedStr);
				}
				
				splitedStrs.add(splitedStr);
				splitedStrBuffer.setLength(0); // splitedStr의 내용을 모두 지움
				
			} else {
				splitedStrBuffer.append(ch);
			}
			
			// escape 문자이면 true, 아니면 false
			isEscapeChar = (ch == '\\')?true:false;
		}
		
		// 문자열의 모든 문자에 대해 종료되었을때,
		// splitedStr의 내용을 splitedStrs에 추가함
		String splitedStr = splitedStrBuffer.toString();
		// trim 처리
		if(isTrim == true) {
			splitedStr = splitedStr.trim();
		}
		// escape 처리
		if(isEscape == true) {
			splitedStr = escape(splitedStr);
		}
			
		splitedStrs.add(splitedStr);
		
		return splitedStrs.toArray(new String[splitedStrs.size()]);
	}

	/**
	 * 문자열을 구분자 문자에 의해 나눔, 나누어진 문자열에 trim, escape 처리함<br>
	 * 단, 문자열에 구분자가 escape 되어 있으면 구분하지 않음<br>
	 * ex) delimiter: ',' 이고<br>
	 *     str: "Test\, 입니다., 두번째 문장" 이면,<br>
	 *     "Test\, 입니다.", "두번째 문장" 로 분리함
	 * 
	 * @param str 문자열
	 * @param delimiter 구분자
	 * @return 나누어진 문자열 목록
	 */
	public static String[] split(String str, char delimiter) throws Exception {
		return split(str, delimiter, true, true);
	}

	/**
	 * 주어진 문자열을 delimiter로 나눈 후 특정 위치들의 문자열들을 선택하여 반환<br>
	 * ex) str: test1\ttest2\ttest3, delimiter: \t, locs: {0, 2, 1}<br>
	 *     -> {"test1", "test3", "test2"} 가 반환됨
	 * 
	 * @param str 문자열
	 * @param delimiter 나눌 문자열
	 * @param locs 선택할 위치 목록
	 * @return 선택된 문자열 목록 
	 */
	public static String[] pick(String str, String delimiter, int[] locs) throws Exception {
		
		// 입력값 검증
		if(isEmpty(delimiter) == true) {
			throw new Exception("delimiter is empty");
		}
		
		if(locs == null) {
			throw new NullPointerException("locs is null");
		}
		
		// 선택된 문자열 목록 변수
		String[] picks = new String[locs.length];
		if(isEmpty(str) == true) {
			return picks;
		}
		
		// delimiter로 분해
		String[] splitedStrs = str.split(delimiter);
		
		for(int index = 0; index < locs.length; index++) {
			
			// 현재 loc 값
			int loc = locs[index];
			
			// loc 값이 0 보다 작거나 나누어진 문자열의 수보다 크면
			// 다음 loc 값을 찾음
			if(loc < 0 || loc >= splitedStrs.length) {
				continue;
			}
			
			picks[index] = splitedStrs[loc];
		}
		
		return picks;
	}

	/**
	 * 주어진 문자열을 delimiter로 나눈 후 특정 위치의 문자열을 선택하여 반환
	 * ex) str: test1\ttest2\ttest3, delimiter: \t, loc: 1<br>
	 *     -> "test2" 가 반환됨
	 * 
	 * @param str 문자열
	 * @param delimiter 나눌 문자열
	 * @param loc 선택할 위치
	 * @return 선택된 문자열
	 */
	public static String pick(String str, String delimiter, int loc) throws Exception {
		return pick(str, delimiter, new int[]{loc})[0];
	}
	
	/**
	 * wildcard 패턴 문자열과 일치 여부 반환
	 * 
	 * @param str wildcard 패턴에 맞는지 검사할 문자열
	 * @param wildcardPattern wildcard 패턴 문자열
	 * @return wildcard 패턴 문자열과 일치 여부
	 */
	public static boolean matchWildcard(String str, String wildcardPattern) throws Exception {
		return WildcardPattern.create(wildcardPattern).match(str).isMatch();
	}
	
	/**
	 * wildcard 패턴 문자열과 일치 여부 반환
	 * 
	 * @param str wildcard 패턴에 맞는지 검사할 문자열
	 * @param wildcardPattern wildcard 패턴 문자열
	 * @param ignoreCase 대소문자 구별 여부(true-구별하지 않음, false-구별함)
	 * @return wildcard 패턴 문자열과 일치 여부
	 */
	public static boolean matchWildcard(String str, String wildcardPattern, boolean ignoreCase) throws Exception {
		return WildcardPattern.create(wildcardPattern, ignoreCase).match(str).isMatch();
	}
	
	/**
	 * wildcard pattern 클래스<br>
	 * ex) *abc*, ??.txt
	 * 
	 * @author jmsohn
	 */
	public static class WildcardPattern {
		
		/** wildcard pattern 문자열 */
		private String pattern;
		/** 대소문자 구별 여부(true-구별하지 않음, false-구별함) */
		private boolean ignoreCase;
		
		// wildcard match시에만 사용
		/** 매치된 문자열 목록 */
		private ArrayList<String> groups;
		/** 매치된 문자열의 시작 위치 목록 */
		private ArrayList<Integer> starts;
		/** 매치된 문자열의 길이 목록 */
		private ArrayList<Integer> lengths;
		
		/**
		 * 생성자
		 * 
		 * @param pattern wildcard pattern 문자열
		 */
		private WildcardPattern(String pattern, boolean ignoreCase) throws Exception {
			this.pattern = StringUtil.escape(pattern);
			this.ignoreCase = ignoreCase;
		}
		
		/**
		 * 생성 메소드
		 * 
		 * @param pattern wildcard pattern 문자열
		 * @param ignoreCase 대소문자 구별 여부
		 * @return 생성된 wildcard pattern 클래스
		 */
		public static WildcardPattern create(String pattern, boolean ignoreCase) throws Exception {
			return new WildcardPattern(pattern, ignoreCase); 
		}
		
		/**
		 * 생성 메소드
		 * 
		 * @param pattern wildcard pattern 문자열
		 * @return 생성된 wildcard pattern 클래스
		 */
		public static WildcardPattern create(String pattern) throws Exception {
			return new WildcardPattern(pattern, false); 
		}
		
		/**
		 * 문자열을 설정된 wildcard pattern에 매치함
		 * 
		 * @param str 문자열
		 * @return 매치 결과
		 */
		public WildcardMatcher match(String str) {

			// 초기화 실행
			this.groups = new ArrayList<String>();
			this.starts = new ArrayList<Integer>();
			this.lengths = new ArrayList<Integer>();
			
			// 매치 수행
			boolean match = this.match(str, 0, 0);
			
			// WildcardMatcher 생성 후 반환
			return WildcardMatcher.builder()
					.match(match)
					.groups(this.groups)
					.starts(this.starts)
					.lengths(this.lengths)
					.build();
		}
		
		/**
		 * 문자열이 wildcard pattern 매치 수행<br>
		 * 매치된 문자열 목록(groups), 시작 위치 목록(starts), 길이 목록(lengths)를 설정함
		 * 
		 * @param str 문자열
		 * @param strStart 문자열의 읽기 시작 위치
		 * @param wStart wildcard pattern 문자열의 시작 위치
		 * @return 매치 여부
		 */
		private boolean match(String str, int strStart, int wStart) {
			
			// 입력값 검증
			if(str == null) {
				return false;
			}
			
			if(this.pattern == null) {
				return false;
			}
			
			// 패턴을 분리
			// ex) wildcardPattern="abc*xyz?def", wStart=3 이면
			//     "*" 수량자(lowerSize, upperSize)와 "xyz"(패턴문자열)를 분리해냄
			
			// 패턴을 문자열을 저장하기 위한 변수
			StringBuilder patternBuilder = new StringBuilder();
			// wildcard 패턴 문자열 내에서의 위치 변수
			int wPos = wStart;
			// 상태 변수 - 0: 수량자 상태, 1: 패턴 문자열
			int status = 0;
			// 수량자의 최소 크기
			int lowerSize = 0;
			// 수량자의 최대 크기
			int upperSize = 0;
			
			while(wPos < this.pattern.length()) {
				
				char ch = this.pattern.charAt(wPos);
				
				if(status == 0) {
					
					if(ch == '*') {
						
						upperSize = Integer.MAX_VALUE;
						
					} else if(ch == '?') {
						
						lowerSize++;
						
					} else {
						
						patternBuilder.append(ch);
						status = 1;
					}
					
				} else {
					
					if(ch == '*' || ch == '?') {
						break;
					} else {
						patternBuilder.append(ch);
					}
				}
				
				wPos++;
			}
			
			// 최소 크기(lowerSize)가 최대 크기(upperSize) 보다 크면
			// 최대 크기를 최소 크기로 설정함
			if(lowerSize > upperSize) {
				upperSize = lowerSize;
			}
			
			String pattern = patternBuilder.toString();
			
			// 패턴 문자열 매칭 수행
			// ex) 상기 과정에서 분리해낸 "xyz"가 있는지 찾음
			//     즉, wildcardPattern="abc*xyz?def", str="abcdefxyz1def", strStart=3 이면
			//     "defxyz"까지 매칭함
			
			// 매칭이 시작된 지점 위치 변수
			int mark = 0;
			// 패턴 문자열 내에서 매칭 중인 위치 변수
			int pos = 0;
			// 매치 여부 변수
			boolean isMatch = false;
			// 패턴 문자열에 매치되기 이전의 문자의 개수
			// ex) 상기 예의 "defxyz"에서 "def"의 개수 즉 3을 저장
			int count = 0;
			
			if(pattern.length() != 0) {
				
				// 수량자가 있고 뒤에 패턴 문자열이 있는 경우
				
				int index = strStart;
				
				while(index < str.length()) {
					
					char ch = str.charAt(index);
					
					// 읽은 문자와 패턴 문자열의 문자와 동일한 지 확인
					if(StringUtil.isEqualChar(ch, pattern.charAt(pos), this.ignoreCase) == true) {
						// 패턴 문자열의 문자와 일치하는 경우
						
						// 첫번재 패턴 문자열과 일치하는 경우
						// 현재 위치를 mark에 저장
						if(pos == 0) {
							mark = index;
						}
						
						// 패턴 문자열의 위치를 다음(오른쪽) 위치로 하나 이동
						pos++;
						
						// 만일, 모든 패턴 문자열이 일치하면 중지함
						if(pos == pattern.length()) {
							isMatch = true;
							break;
						}
						
					} else {
						// 패턴 문자열의 문자와 일치하지 않는 경우
						
						// 패턴 문자열과 일부 일치했던 적이 있는 경우
						// mark 위치로 돌린 다음 다시 검사하도록 함
						if(pos != 0) {
							index = mark;
						}
						
						pos = 0;
					}
					
					index++;
				}
				
				// 패턴 문자열 이전의 문자의 수 계산
				count = mark - strStart;
				
				// 문자열이 매치되지 않으면 false 반환
				if(isMatch == false) {
					return false;
				}
				
				// 수량자와 패턴이외에 매칭된 문자 개수와 비교하여 적합하지 않으면 false 반환
				if(count < lowerSize || count > upperSize) {
					return false;
				}
				
				// 수량자가 없는 경우를 제외하고, 매치된 문자열에 관한 정보를 추가함
				// upperSize == 0일 경우 수량자가 없는 경우임
				if(upperSize != 0) {
					this.groups.add(str.substring(strStart, mark));
					this.starts.add(strStart);
					this.lengths.add(count);
				}
				
				// 다음 문자열 매치 검사를 위한 재귀 호출
				return match(str, index+1, wPos);
				
			} else {
				
				// 수량자만 있는 경우
				// 이 경우는 패턴의 마지막에서만 발생함
				// ex) wildcardPattern = "abc*" 일 경우 "*"
				
				// 문자의 수 계산
				count = str.length() - strStart;
				
				// 수량자와 패턴이외에 매칭된 문자 개수와 비교하여 적합하지 않으면 false 반환
				if(count < lowerSize || count > upperSize) {
					return false;
				} else {
					
					// 수량자가 없는 경우를 제외하고, 매치된 문자열에 관한 정보를 추가함
					// upperSize == 0일 경우 수량자가 없는 경우임
					if(upperSize != 0) {
						this.groups.add(str.substring(strStart, str.length()));
						this.starts.add(strStart);
						this.lengths.add(count);
					}
					
					return true;
				}
			}
		}
		
	}
	
	/**
	 * wildcard pattern 매칭 결과 클래스
	 * 
	 * @author jmsohn
	 */
	@Getter
	public static class WildcardMatcher {
		
		/** 매치 여부 */
		private boolean match;
		/** 매치된 문자열 목록 */
		private ArrayList<String> groups;
		/** 매치된 문자열의 시작 위치 목록 */
		private ArrayList<Integer> starts;
		/** 매치된 문자열의 길이 목록 */
		private ArrayList<Integer> lengths;
		
		/**
		 * 생성자
		 * 
		 * @param match 매치 여부
		 * @param groups 매치된 문자열
		 * @param starts 매치된 문자열의 시작 위치 목록
		 * @param lengths 매치된 문자열의 길이 목록
		 */
		@Builder
		public WildcardMatcher(boolean match, ArrayList<String> groups, ArrayList<Integer> starts, ArrayList<Integer> lengths) {
			this.match = match;
			this.groups = groups;
			this.starts = starts;
			this.lengths = lengths;
		}
	}
	
	/**
	 * 문자열 객체의 내용을 수정하는 메소드<br>
	 * - 문자열은 불변(immutable)으로 내용을 수정할 수 없음<br>
	 *   보안상 문제가 되는 문자열도 메모리상에 남아 있음<br>
	 *   이에, 문자열의 사용 이후 보안 문자를 메모리상에서 완전히 삭제 또는 마스킹하는 방법을 제공함<br>
	 * - 이 메소드를 사용할 경우 illegal access warning이 발생함
	 *   이는 java 실행시 "--illegal-access=warn" 옵션을 추가하여 방지할 수 있음
	 * 
	 * @param str 수정할 문자열
	 * @param toStr 변경할 문자열
	 */
	public static void changeStr(String str, String toStr) throws Exception {
		
		// 입력값 검증
		if(str == null) {
			throw new NullPointerException("str is null");
		}
		
		if(toStr == null) {
			throw new NullPointerException("toStr is null");
		}
		
		// 문자열 필드에 변경할 문자열을 강제 설정
		char[] buffer = new char[toStr.length()];
		toStr.getChars(0, toStr.length(), buffer, 0);
		
		TypeUtil.setField(str, "value", buffer);
	}
	
	/**
	 * 문자열 내에 ${변수} 목록을 반환하는 메소드
	 *
	 * @param str 변수가 포함된 문자열
	 * @return 변수 목록
	 */
	public static String replaceVars(String str, Map<String, String> values) {

		// 재구성 문자열 변수
		StringBuilder replacedStr = new StringBuilder();
		// ${변수}의 변수 명을 담을 임시 변수
		StringBuilder var = new StringBuilder();

		// 상태 변수, false: 일반 문자열 상태, true: 변수명 상태
		boolean isVar = false;
		for (int index = 0; index < str.length(); index++) {  // 한 문자씩

			char ch = str.charAt(index);

			if (isVar == true) {
				// 변수 상태

				if (ch == '}') {
					// 변수 종료
                    
					// 변수에 설정된 값으로 추가
					replacedStr.append(values.get(var.toString()));
                        
					// 변수명 초기화
					var.setLength(0);
                    
					// 일반 문자열 상태로 상태 변경 
					isVar = false;

				} else if(
						(ch >= 'a' && ch <= 'z') ||
						(ch >= 'A' && ch <= 'Z') ||
						(ch >= '0' && ch <= '9') ||
						ch == '_'
					) {
                	
					// 변수명 내
					// 변수명에 문자 추가
					var.append(ch);

				} else {
                	
					// 변수명이 아닌 문자가 들어올 경우
                	
					// 현재까지 임시저장한 문자열은 일반 문자열이므로
					// 재구성 문자열에 추가함
					replacedStr.append("${").append(var);
                	
					// 한칸 뒤로 이동하여 다시 파싱(pushback)
					index--;

					// 변수명 초기화
					var.setLength(0);
                	
					// 일반 문자열 상태로 변환
					isVar = false;
				}
                
			} else {
            	
				// 일반 문자열 상태
				if (ch == '$' && str.charAt(index+1) == '{' && index+1 < str.length()) {
                	
					// "${" 가 들어온 경우 변수명 상태로 변환
					isVar = true;
					index++;
					
				} else {
					replacedStr.append(ch);
				}
			}
		}
		
		// 파싱 중인 변수명이 있으면 추가함
		if(var.length() != 0) {
			replacedStr.append("${").append(var);
		}
        
		return replacedStr.toString();
	}
	
	/**
	 * 문자열 내에 ${변수} 목록을 반환하는 메소드
	 *
	 * @param str 변수가 포함된 문자열
	 * @return 변수 목록
	 */
	public static ArrayList<String> findAllVars(String str) {

		// 변수명을 순서대로 담을 목록
		ArrayList<String> vars = new ArrayList<>();
		// ${변수}의 변수 명을 담을 임시 변수
		StringBuilder var = new StringBuilder();

		// 상태 변수, false: 일반 문자열 상태, true: 변수명 상태
		boolean isVar = false;
		for (int index = 0; index < str.length(); index++) {  // 한 문자씩

			char ch = str.charAt(index);
            
			if (isVar == true) {
				// 변수 상태

				if (ch == '}') {
					// 변수 종료

					// 변수 목록에 추가
					vars.add(var.toString());
                        
					// 변수명 초기화
					var.setLength(0);

					// 일반 문자열 상태로 상태 변경 
					isVar = false;
                    
				} else if(
						(ch >= 'a' && ch <= 'z') ||
						(ch >= 'A' && ch <= 'Z') ||
						(ch >= '0' && ch <= '9') ||
						ch == '_'
					) {
					// 변수명 내
					// 변수명에 문자 추가
					var.append(ch);

				} else {
                	
					// 변수명이 아닌 문자가 들어올 경우
					// 변수명 초기화
					var.setLength(0);
					isVar = false;
				}

			} else {
            	
				// 일반 문자열 상태
				if (ch == '$' && str.charAt(index+1) == '{' && index+1 < str.length()) {

					// "${" 가 들어온 경우 변수명 상태로 변환
					isVar = true;
					index++;
				}
			}
		}
        
		return vars;
    }
	
	/**
	 * target 문자열 내에 패턴(pattern)이 나타난 곳까지의 문자열과<br>
	 * 패턴이 나타난 이후의 문자열을 분리하여 반환<br>
	 * 만일, target 문자열 내에 패턴이 나타나지 않으면 target 문자열만 반환함<br>
	 * <pre>
	 * ex) target: "test1 > test2> test3",
	 *     pattern: "\\s>\\s" 이면
	 *     결과: {"test1", "test2> test3"}
	 *     
	 *     taget: "test1 > test2"
	 *     pattern: "\\sA\\s" 이면
	 *     결과: {"test1 > test2"}
	 * </pre>
	 * 
	 * @param target 대상 문자열
	 * @param pattern 나눌 패턴
	 * @return 분리된 문자열 배열
	 */
	public static String[] splitFirst(String target, String pattern) throws Exception {
		
		// 입력값 검증
		if(target == null) {
			throw new NullPointerException("target is null.");
		}
		
		if(pattern == null) {
			throw new NullPointerException("pattern is null");
		}
		
		// target 문자열에 패턴 적용
		Pattern patternP = Pattern.compile(pattern);
		Matcher patternM = patternP.matcher(target);
		
		// 만일 패턴을 찾지 못했을 경우,
		// target 문자열을 배열에 담아 반환
		if(patternM.find() == false) {
			return new String[] {target};
		}
		
		// 패턴이 target 문자열 내에 있는 경우
		// target 문자열에서 처음부터 패턴이 나타나는 곳까지 문자열과
		// 패턴이 나타난 이후 부터 문자열의 끝까지 나눈 문자열을 반환함
		int start = patternM.start();
		int end = patternM.end();
		
		return new String[] {
				target.substring(0, start),
				target.substring(end, target.length())
			};
		
	} // End of splitFirst
}
