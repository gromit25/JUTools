package com.jutools;

/**
 * 숫자 한글 표기 클래스
 * 
 * @author jmsohn
 */
public class KoreanNumExp {
	
	/** 숫자 0의 한글 표현 */
	private static String ZERO_KOREAN_EXP = "영";
	/** 음수 의 한글 표현 */
	private static String MINUS_KOREAN_EXP = "마이너스 ";
	
	/**
	 * 한글 숫자 1~9
	 * 
	 * @author jmsohn
	 */
	private enum KoreanNum {
		일,
		이,
		삼,
		사,
		오,
		육,
		칠,
		팔,
		구;
	}
	
	/**
	 * 천단위
	 * 
	 * @author jmsohn
	 */
	private enum KoreanUnit {
		십,
		백,
		천;
	}
	
	/**
	 * 만단위
	 * 
	 * @author jmsohn
	 */
	private enum KoreanTenThousandUnit {
		만,
		억,
		조,
		경; // long 값의 최대 범위까지
	}
	
	/**
	 * 숫자 위치(pos)의 숫자(n)의 천단위 한글 표현을 반환<br>
	 * 숫자 위치는 0부터 시작임<br>
	 * 예를 들어) 123의 1의 pos는 2, 3의 pos 는 0임
	 * 
	 * @param pos 숫자 자리 위치
	 * @param ch 특정 위치의 숫자의 문자
	 * @return 변환된 한글 표현
	 */
	private static String makeKoreanExpByThousand(int pos, char ch) throws Exception {
		
		// 입력값 검증
		if(pos < 0 || pos > 20) {
			throw new Exception("pos must be between 1 to 20:" + pos);
		}
		
		// 만일 숫자가 '0' 이면, 빈문자열을 반환
		if(ch == '0') {
			return ""; 
		}
		
		// 특정 위치의 숫자의 문자를 숫자로 변환
		int n = ch - '0';
		if(n < 0 || n > 9) {
			throw new Exception("ch must be between '0' to '9':" + ch);
		}
		
		// 한글 표현 변수
		StringBuilder koreanExp = new StringBuilder("");
		
		// 만 이하 단위의 위치
		int unitPos = pos % 4;
		
		// 숫자의 한글 표현을 추가
		// 단, 천,백,십에는 일(1)을 추가하지 않음
		// -> 일천(X), 천(O)
		if(n != 1 || unitPos == 0) {
			koreanExp.append((KoreanNum.values()[n-1]).name());
		}
		
		// 만 이하 단위 표현을 가져옴
		if(unitPos != 0) {
			koreanExp.append((KoreanUnit.values()[unitPos - 1]).name());
		}
		
		return koreanExp.toString();
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"<br>
	 *     n: 1500010, space: " " -> "백오십만 십"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @param space 만단위 띄어쓸 문자열
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(long n, String space) throws Exception {
		
		// 값이 0 일 경우,
		if(n == 0) {
			return ZERO_KOREAN_EXP;
		}
		
		// 만단위 띄어쓰기가 null 일 경우, 스페이스("")로 치환
		if(space == null) {
			space = "";
		}
		
		// 한글 표현식을 담을 변수
		StringBuilder koreanExp = new StringBuilder("");
		
		// 음수 부호 읽기 추가
		if(n < 0) {
			koreanExp.append(MINUS_KOREAN_EXP);
		}
		
		// 양수로 변환(abs), 음수라면 sign이 -1 이 되기 때문에 음수 * -1 이 되어 양수가 됨
		// 한글로 변환하기 위함
		n = MathUtil.sign(n) * n;
		
		// 문자열로 변환하여 오른쪽부터 숫자 위치에 따라 변환함
		// ex) "123" 일 경우, 첫번째 '1'과 위치 2을 "일백" 으로 변환 
		String nStr = Long.toString(n);
		int length = nStr.length();
		
		for(int pos = 0; pos < length; pos++) {
			
			// 숫자 자리 위치, ex) 123 의 1은 2번째 자리임
			int digitPos = length - 1 - pos;
			
			// 천단위로 변환 추가
			koreanExp.append(makeKoreanExpByThousand(digitPos , nStr.charAt(pos)));
			
			// 만단위 표현 추가
			if(digitPos % 4 == 0) {
				int tenThousandUnitPos = digitPos / 4;
				if(tenThousandUnitPos != 0) {
					koreanExp
						.append((KoreanTenThousandUnit.values()[tenThousandUnitPos - 1]).name())
						.append(space);
				}
			}
		}
		
		return koreanExp.toString();
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(long n) throws Exception {
		return toKorean(n, "");
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"<br>
	 *     n: 1500010, space: " " -> "백오십만 십"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @param space 만단위 띄어쓸 문자열
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(int n, String space) throws Exception {
		return toKorean((long)n, space);
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(int n) throws Exception {
		return toKorean(n, "");
	}
	
	/**
	 * 주어진 문자열을 숫자로 변환
	 * ex) n: "오만육천칠백팔십구", space: "" -> 56789<br>
	 *     n: "백오십만 십", space: " " -> 1500010 
	 * 
	 * @param koreanExp 변환할 숫자
	 * @param space 만단위 띄어쓴 문자열 
	 * @return 변환된 숫자
	 */
	public static long toLong(String koreanExp, String space) throws Exception {
		return -1;
	}
}
