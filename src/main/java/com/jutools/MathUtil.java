package com.jutools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

import com.jutools.mathexp.MathExp;
import com.jutools.mathexp.MathResult;

import lombok.Getter;

/**
 * 산술 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class MathUtil {
	
	/**
	 * 표준 단위 접두사<br>
	 * ※ https://en.wikipedia.org/wiki/Metric_prefix 참고
	 * 
	 * @author jmsohn
	 */
	private enum UnitPrefix {
		
		q(0.000000000000000000000000000001),
		r(0.000000000000000000000000001),
		y(0.000000000000000000000001),
		z(0.000000000000000000001),
		a(0.000000000000000001),
		f(0.000000000000001),
		p(0.000000000001),
		n(0.000000001),
		μ(0.000001),
		m(0.001),
		c(0.01),
		d(0.1),
		none(1.0),
		da(10.0),
		h(100.0),
		k(1000.0),
		M(1000000.0),
		G(1000000000.0),
		T(1000000000000.0),
		P(1000000000000000.0),
		E(1000000000000000000.0),
		Z(1000000000000000000000.0),
		Y(1000000000000000000000000.0),
		R(1000000000000000000000000000.0),
		Q(1000000000000000000000000000000.0);
		
		// -----------
		
		/** 인수 */
		@Getter
		private double factor;
		
		/**
		 * 생성자
		 * 
		 * @param factor 인수
		 */
		UnitPrefix(double factor) {
			this.factor = factor;
		}
		
		/**
		 * 접두사의 이름을 반환
		 * 
		 * @return 접두사의 이름
		 */
		String getName() {
			if(this == none) {
				return "";
			} else {
				return name();
			}
		}
		
		/**
		 * 단위 접두사 문자열에 해당하는 단위 접두사를 반환
		 * 
		 * @param prefixStr 단위 접두사 문자열
		 * @return 단위 접두사
		 */
		static UnitPrefix get(String prefixStr) {

			try {
				
				if(StringUtil.isBlank(prefixStr) == true) {
					return UnitPrefix.none;
				} else {
					return UnitPrefix.valueOf(prefixStr);
				}
				
			} catch(Exception ex) {
				return null;
			}
		}
		
		/**
		 * 특정 index의 단위 접두사 반환
		 * 
		 * @param index 가져올 index
		 * @return 단위 접두사 
		 */
		static UnitPrefix get(int index) {
			if(UnitPrefix.values().length < index) {
				return null;
			} else {
				return UnitPrefix.values()[index];
			}
		}
		
		
		/**
		 * 주어진 값에 적합한 단위 접두어를 반환<br>
		 * 천단위 접두어만 반환
		 * 
		 * @param value 주어진 값
		 * @return 단위 접두어
		 */
		static UnitPrefix getPrefix(double value) {
			
			// 
			value = Math.abs(value);
			
			// 천의 자리수 계산
			int index = (int)Math.floor(Math.log10(value)/3);
			
			// 접두사 c(0.01), d(0.1)를 제외
			index += (index < 0)?-2:0;
			
			// 접두사 da(10), h(100)를 제외
			index += (index > 0)?2:0;
			
			// index를 가장 작은 접두사 위치인 
			index += UnitPrefix.none.ordinal();
			
			//
			index = (index < 0)?0:index;
			index = (index > UnitPrefix.size())?UnitPrefix.size():index;
			
			return UnitPrefix.get(index);
		}
		
		/**
		 * 단위 접두사 문자열이 존재하는지 여부 반환
		 * 
		 * @param prefixStr 단뮈 접두사 문자열
		 * @return 단위 접두사 문자열 존재 여부, 존재할 경우 true
		 */
		static boolean contains(String prefix) {
			if(UnitPrefix.get(prefix) == null) {
				return false;
			} else {
				return true;
			}
		}
		
		/**
		 * unit prefix의 개수 반환
		 * 
		 * @return unit prefix의 개수
		 */
		static int size() {
			return UnitPrefix.values().length;
		}
	}

	/**
	 * Byte 단위 접두사<br>
	 * ※ https://en.wikipedia.org/wiki/Binary_prefix 참조
	 * 
	 * @author jmsohn
	 */
	private enum BytePrefix {
		
		none(1.0),
		Ki(1024.0),
		Mi(1048576.0),
		Gi(1073741824.0),
		Ti(1099511627776.0),
		Pi(1125899906842624.0),
		Ei(1152921504606846976.0),
		Zi(1180591620717411303424.0),
		Yi(1208925819614629174706176.0);
		
		// -----------
		/** 인수 */
		@Getter
		private double factor;
		
		/**
		 * 생성자
		 * 
		 * @param factor 인수
		 */
		BytePrefix(double factor) {
			this.factor = factor;
		}
		
		/**
		 * 접두사의 이름을 반환
		 * 
		 * @return 접두사의 이름
		 */
		String getName() {
			if(this == none) {
				return "";
			} else {
				return name();
			}
		}
		
		/**
		 * 단위 접두사 문자열에 해당하는 단위 접두사를 반환
		 * 
		 * @param prefixStr 단위 접두사 문자열
		 * @return 단위 접두사
		 */
		static BytePrefix get(String prefixStr) {
			
			try {
				
				if(StringUtil.isBlank(prefixStr) == true) {
					return BytePrefix.none;
				} else {
					return BytePrefix.valueOf(prefixStr);
				}
				
			} catch(Exception ex) {
				return null;
			}
		}
		
		/**
		 * 특정 index의 단위 접두사 반환
		 * 
		 * @param index 가져올 index
		 * @return 단위 접두사 
		 */
		static BytePrefix get(int index) {
			if(BytePrefix.values().length < index) {
				return null;
			} else {
				return BytePrefix.values()[index];
			}
		}
		
		/**
		 * 주어진 값에 적합한 단위 접두어를 반환<br>
		 * 천단위 접두어만 반환
		 * 
		 * @param value 주어진 값
		 * @return 단위 접두어
		 */
		static BytePrefix getPrefix(double value) {
			
			// 
			value = Math.abs(value);
			
			// 천의 자리수 계산
			int index = (int)Math.floor(Math.log10(value)/Math.log10(1024));
			
			//
			index = (index < 0)?0:index;
			index = (index > BytePrefix.size())?BytePrefix.size():index;
			
			return BytePrefix.get(index);
		}
		
		/**
		 * 단위 접두사 문자열이 존재하는지 여부 반환
		 * 
		 * @param prefixStr 단뮈 접두사 문자열
		 * @return 단위 접두사 문자열 존재 여부, 존재할 경우 true
		 */
		static boolean contains(String prefix) {
			if(BytePrefix.get(prefix) == null) {
				return false;
			} else {
				return true;
			}
		}
		
		/**
		 * unit prefix의 개수 반환
		 * 
		 * @return unit prefix의 개수
		 */
		static int size() {
			return BytePrefix.values().length;
		}
	}
	
	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	private enum KoreanPrefix {
		
		만(10000.0),
		억(10000.0 * 10000.0),
		조(10000.0 * 10000.0 * 10000.0),
		경(10000.0 * 10000.0 * 10000.0 * 10000.0),
		해(10000.0 * 10000.0 * 10000.0 * 10000.0 * 10000.0);
		
		// -----------
		/** */
		@Getter
		private double factor;
		
		/**
		 * 생성자
		 * 
		 * @param factor
		 */
		KoreanPrefix(double factor) {
			this.factor = factor;
		}		
	}
	
	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	private enum KoreanSubPrefix {
		
		십(10.0),
		백(100.0),
		천(1000.0);
		
		// -----------
		/** */
		@Getter
		private double factor;
		
		/**
		 * 생성자
		 * 
		 * @param factor
		 */
		KoreanSubPrefix(double factor) {
			this.factor = factor;
		}		

	}
	
	/**
	 * 
	 * @author jmsohn
	 */
	private enum KoreanNum {
		
		일(1.0),
		이(2.0),
		삼(3.0),
		사(4.0),
		오(5.0),
		육(6.0),
		칠(7.0),
		팔(8.0),
		구(9.0);
		
		// -----------
		/** */
		@Getter
		private double value;
		
		/**
		 * 생성자
		 * 
		 * @param factor
		 */
		KoreanNum(double value) {
			this.value = value;
		}		
	}
	
	/**
	 * 단위 접두사(unitPrefix)에 해당하는 factor 값을 반환하는 메소드<br>
	 * 참조)<br>
	 * https://en.wikipedia.org/wiki/Metric_prefix<br>
	 * https://en.wikipedia.org/wiki/Binary_prefix
	 * 
	 * @param unitPrefix 단위 접두사
	 * @return 단위 접두사의 factor 값
	 */
	public static double unitPrefixToFactor(String prefix) throws Exception {
		
		if(BytePrefix.contains(prefix) == true) {
			return BytePrefix.get(prefix).getFactor();
		}
		
		return UnitPrefix.get(prefix).getFactor();
	}
	
	/**
	 * 단위(unit)을 접두사(prefix)와 기본 단위(base)로 분리하는 메소드<br>
	 * ex) "kg" -> {"k", "g"}
	 * 
	 * @param unit 분리할 단위
	 * @return 나누어진 접두사(prefix)와 기본 단위(base)<br>
	 *         배열의 0은 접두사(prefix), prefix가 없을 경우 ""<br>
	 *         배열의 1은 기본 단위(base)<br>
	 */
	public static String[] splitUnitToPrefixAndBase(String unit) throws Exception {
		
		if(unit == null) {
			throw new NullPointerException("unit is null");
		}
		
		unit = unit.trim();	// 공백 제거
		if(unit.isEmpty() == true) {
			return new String[] {"", ""};
		}
		
		// 단위 접두사
		String unitPrefix = "";
		// 기본 단위
		String baseUnit = "";
		
		if(unit.length() > 2 && unit.startsWith("da")) {
			
			// SI 단위계에서 유일한 2자 접두어
			unitPrefix = "da";
			baseUnit = unit.substring(2, unit.length());
			
		} else if(unit.length() > 2 && unit.charAt(1) == 'i') {
			
			// 바이트 단위에서 사용하는 Ki(Kibi), Mi(Mibi) ...
			unitPrefix = unit.substring(0, 2);
			baseUnit = unit.substring(2, unit.length());
			
		} else if(unit.length() > 1) {
			
			// 앞의 1자리만 접두어
			unitPrefix = unit.substring(0, 1);
			baseUnit = unit.substring(1, unit.length());
			
		}
		
		// 접두사가 등록되어 있는 것이 없는 경우
		// 단위(unit) 문자열에 접두사가 없는 것으로 간주함
		if(unitPrefix.isEmpty() == false
				&& UnitPrefix.contains(unitPrefix) == false
				&& BytePrefix.contains(unitPrefix) == false) {
			
			unitPrefix = "";
			baseUnit = unit;
		}
		
		return new String[] {unitPrefix, baseUnit};
	}
	
	/**
	 * 
	 * @author jmsohn 
	 */
	public static class UnitExp {
		
		/** 값 */
		@Getter
		private double value;
		
		/** 접두사 */
		@Getter
		private String prefix;
		
		/**
		 * 생성자
		 * 
		 * @param value
		 * @param prefix
		 */
		UnitExp(double value, String prefix) {
			this.value = value;
			this.prefix = prefix;
		}
		
		/**
		 * 지정된 소수점 이하 자리수(decimalPlaces)에 따라 문자열 변환하여 반환
		 * 
		 * @param decimalPlaces 소수점 이하 자리수
		 * @return 변환된 문자열
		 */
		public String toString(int decimalPlaces) throws Exception {
			
			// 입력값 검증
			if(decimalPlaces < 0) {
				throw new IllegalArgumentException("decimal place must be greater than 0:" + decimalPlaces);
			}
			
			//
			String formatStr = "%." + decimalPlaces + "f" + this.prefix;
			
			//
			BigDecimal bdValue = new BigDecimal(this.value)
									.setScale(decimalPlaces, RoundingMode.DOWN);
			
			//
			return String.format(formatStr, bdValue);
		}
		
		@Override
		public String toString() {
			try {
				return this.toString(2);
			} catch(Exception ex) {
				return ex.toString();
			}
		}
	}

	/**
	 * 주어진 값에 단위 접두어를 추가한 표현식으로 변환하는 메소드<br>
	 * ex) 1234 -> 1.23k
	 * 
	 * @param value 변환할 값
	 * @return 단위 접두어 표현식 객체
	 */
	public static UnitExp toUnitExp(double value) throws Exception {
		
		// 값에 적합한 단위 접두어를 가져옴
		UnitPrefix prefix = UnitPrefix.getPrefix(value);
		
		// 단위 표현식 객체를 생성하여 반환
		// BigDecimal 값을 소수점 이하(ex. 0.001)로 나누려고 하면 오류가 발생함
		// 이를 문자열로 변경후 나누면 문제가 없음
		// 이유는 정확히 모름
		return new UnitExp(value/prefix.getFactor(), prefix.getName());
	}
	
	/**
	 * 주어진 값에 적합한 byte 단위 접두어를 반환
	 * 
	 * @param value 주어진 값
	 * @return byte 단위 접두어
	 */
//	private static String getByteUnitPrefix(BigDecimal value) {
//		
//		// 입력값 검증
//		if(value == null) {
//			throw new NullPointerException("value is null.");
//		}
//		
//		value = value.abs();
//		
//		if(value.compareTo(new BigDecimal(1024.0)) < 0) {
//			return "";
//		}
//		if(value.compareTo(new BigDecimal(1048576.0)) < 0) {
//			return "Ki";
//		}
//		if(value.compareTo(new BigDecimal(1073741824.0)) < 0) {
//			return "Mi";
//		}
//		if(value.compareTo(new BigDecimal(1099511627776.0)) < 0) {
//			return "Gi";
//		}
//		if(value.compareTo(new BigDecimal(1125899906842624.0)) < 0) {
//			return "Ti";
//		}
//		if(value.compareTo(new BigDecimal(1152921504606846976.0)) < 0) {
//			return "Pi";
//		}
//		if(value.compareTo(new BigDecimal(1180591620717411303424.0)) < 0) {
//			return "Ei";
//		}
//		if(value.compareTo(new BigDecimal(1208925819614629174706176.0)) < 0) {
//			return "Zi";
//		}
//		
//		return "Yi";
//	}
	
	/**
	 * 주어진 값에 byte 단위 접두어를 추가한 표현식으로 변환하는 메소드<br>
	 * ex) 1024 -> 1.00Ki
	 * 
	 * @param value 주어진 값
	 * @return 단위 접두어 표현식 객체
	 */
	public static UnitExp toByteUnitExp(double value) throws Exception {
		
		// 값에 적합한 byte 단위 접두어를 가져옴
		BytePrefix prefix = BytePrefix.getPrefix(value);
		
		// 단위 표현식 객체를 생성하여 반환
		return new UnitExp(value/prefix.getFactor(), prefix.getName());
	}
	
	/**
	 * 주어진 값(long 형)에 byte 단위 접두어를 추가한 표현식으로 변환하는 메소드<br>
	 * ex) 1024 -> 1.00Ki
	 * 
	 * @param value 주어진 값(long 형)
	 */
	public static UnitExp toByteUnitExp(long value) throws Exception {
		return toByteUnitExp((double)value);
	}
	
	/**
	 * 수학 수식 문자열을 계산하여 반환하는 메소드
	 * 
	 * @param exp 계산할 수학 수식 문자열
	 * @return 계산 결과
	 */
	public static double calculate(String exp) throws Exception {
		return MathExp.calculate(exp);
	}
	
	/**
	 * 수학 수식 문자열을 계산하여 반환하는 메소드
	 * 
	 * @param exp 계산할 수학 수식 문자열
	 * @param values 변수 목록
	 * @return 계산 결과
	 */
	public static double calculate(String exp, HashMap<String, Object> values) throws Exception {
		return MathExp.calculate(exp, values);
	}
	
	/**
	 * 수학 수식 문자열을 계산하여 반환하는 메소드<br>
	 * -> 단위가 있는 경우 단위까지 반환
	 * 
	 * @param exp 계산할 수학 수식 문자열
	 * @return 계산 결과
	 */
	public static MathResult calculateWithUnit(String exp) throws Exception {
		return MathExp.calculateWithUnit(exp);
	}
	
	/**
	 * 수학 수식 문자열을 계산하여 반환하는 메소드<br>
	 * -> 단위가 있는 경우 단위까지 반환
	 * 
	 * @param exp 계산할 수학 수식 문자열
	 * @param values 변수 목록
	 * @return 계산 결과
	 */
	public static MathResult calculateWithUnit(String exp, HashMap<String, Object> values) throws Exception {
		return MathExp.calculateWithUnit(exp);
	}
	
	/**
	 * 천자리 콤마 추가
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(int value) {
		DecimalFormat df = new DecimalFormat("###,###");
		return df.format(value);
	}
	
	/**
	 * 천자리 콤마 추가
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(long value) {
		DecimalFormat df = new DecimalFormat("###,###");
		return df.format(value);
	}

	/**
	 * 천자리 콤마 추가<br>
	 * 소수점 3자리까지 표시
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(float value) {
		DecimalFormat df = new DecimalFormat("###,###.###");
		return df.format(value);
	}
	
	/**
	 * 천자리 콤마 추가<br>
	 * 소수점 3자리까지 표시
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(double value) {
		DecimalFormat df = new DecimalFormat("###,###.###");
		return df.format(value);
	}
	
	/**
	 * 주어진 숫자(double)가 양,음,0 부호 반환<br>
	 * 양수 이면, 1<br>
	 * 음수 이면, -1<br>
	 * 0 이면, 0
	 * 
	 * @param value 검사할 숫자
	 * @return 부호
	 */
	public static int sign(double value) {
		
		int sign = 0;
		
		if(value > 0) {
			sign = 1;
		} else if(value < 0) {
			sign = -1;
		}
		
		return sign;
	}
	
	/**
	 * 주어진 숫자(float)가 양,음,0 부호 반환<br>
	 * 양수 이면, 1<br>
	 * 음수 이면, -1<br>
	 * 0 이면, 0
	 * 
	 * @param value 검사할 숫자
	 * @return 부호
	 */
	public static int sign(float value) {
		
		int sign = 0;
		
		if(value > 0) {
			sign = 1;
		} else if(value < 0) {
			sign = -1;
		}
		
		return sign;
	}
	
	/**
	 * 주어진 숫자(int)가 양,음,0 부호 반환<br>
	 * 양수 이면, 1<br>
	 * 음수 이면, -1<br>
	 * 0 이면, 0
	 * 
	 * @param value 검사할 숫자
	 * @return 부호
	 */
	public static int sign(int value) {
		
		int sign = 0;
		
		if(value > 0) {
			sign = 1;
		} else if(value < 0) {
			sign = -1;
		}
		
		return sign;
	}

	/**
	 * 주어진 숫자(long)가 양,음,0 부호 반환<br>
	 * 양수 이면, 1<br>
	 * 음수 이면, -1<br>
	 * 0 이면, 0
	 * 
	 * @param value 검사할 숫자
	 * @return 부호
	 */
	public static int sign(long value) {
		
		int sign = 0;
		
		if(value > 0) {
			sign = 1;
		} else if(value < 0) {
			sign = -1;
		}
		
		return sign;
	}
	
	/**
	 * n 팩토리얼(n!) 반환
	 * 
	 * @param n 팩토리얼 계산할 자연수
	 * @return 팩토리얼 결과
	 */
	public static BigDecimal factorial(int n) throws Exception {
		
		if(n <= 0) {
			throw new IllegalArgumentException("n must be greater than 0:" + n);
		}
		
		if(n == 1) {
			return new BigDecimal(1);
		} else {
			return factorial(n-1).multiply(new BigDecimal(n));
		}
	}
	
	/**
	 * 순열 경우의 수(nPr)
	 * 
	 * @param n 총 개수
	 * @param r 뽑는 수
	 * @return 순열 경우의 수
	 */
	public static BigDecimal permutation(int n, int r) throws Exception {
		
		if(n <= 0) {
			throw new IllegalArgumentException("n must be greater than 0:" + n);
		}
		
		if(r <= 0) {
			throw new IllegalArgumentException("r must be greater than 0:" + r);
		}
		
		if(n < r) {
			throw new IllegalArgumentException("n(" + n +") must be greater than r(" + r + ")");
		}
		
		return factorial(n).divide(factorial(n - r));
	}
	
	/**
	 * 조합 경우의 수(nCr)
	 * 
	 * @param n 총 개수
	 * @param r 뽑는 수
	 * @return 조합 경우의 수
	 */
	public static BigDecimal combination(int n, int r) throws Exception {
		return permutation(n, r).divide(factorial(r));
	}
}
