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
	
	/** 단위 접두사(unit prefix) 목록 */
	private static HashMap<String, Double> unitPrefixMap;
	
	static {
		
		// 단위 접두사(unit prefix) 목록 초기화
		unitPrefixMap = new HashMap<String, Double>();
		
		// https://en.wikipedia.org/wiki/Metric_prefix
		unitPrefixMap.put("Q", 1000000000000000000000000000000.0);
		unitPrefixMap.put("R", 1000000000000000000000000000.0);
		unitPrefixMap.put("Y", 1000000000000000000000000.0);
		unitPrefixMap.put("Z", 1000000000000000000000.0);
		unitPrefixMap.put("E", 1000000000000000000.0);
		unitPrefixMap.put("P", 1000000000000000.0);
		unitPrefixMap.put("T", 1000000000000.0);
		unitPrefixMap.put("G", 1000000000.0);
		unitPrefixMap.put("M", 1000000.0);
		unitPrefixMap.put("k", 1000.0);
		unitPrefixMap.put("h", 100.0);
		unitPrefixMap.put("da", 10.0);
		unitPrefixMap.put("d", 0.1);
		unitPrefixMap.put("c", 0.01);
		unitPrefixMap.put("m", 0.001);
		unitPrefixMap.put("μ", 0.000001);
		unitPrefixMap.put("n", 0.000000001);
		unitPrefixMap.put("p", 0.000000000001);
		unitPrefixMap.put("f", 0.000000000000001);
		unitPrefixMap.put("a", 0.000000000000000001);
		unitPrefixMap.put("z", 0.000000000000000000001);
		unitPrefixMap.put("y", 0.000000000000000000000001);
		unitPrefixMap.put("r", 0.000000000000000000000000001);
		unitPrefixMap.put("q", 0.000000000000000000000000000001);
		
		// https://en.wikipedia.org/wiki/Binary_prefix
		unitPrefixMap.put("Ki", 1024.0);
		unitPrefixMap.put("Mi", 1048576.0);
		unitPrefixMap.put("Gi", 1073741824.0);
		unitPrefixMap.put("Ti", 1099511627776.0);
		unitPrefixMap.put("Pi", 1125899906842624.0);
		unitPrefixMap.put("Ei", 1152921504606846976.0);
		unitPrefixMap.put("Zi", 1180591620717411303424.0);
		unitPrefixMap.put("Yi", 1208925819614629174706176.0);
		
	}
	
	/**
	 * 단위(unit)을 접두사(prefix)와 기본 단위(base)로 분리하는 메소드
	 * 
	 * @param unit 분리할 단위
	 * @return 나누어진 접두사(prefix)와 기본 단위(base)<br>
	 *         배열의 0은 접두사(prefix), prefix가 없을 경우 ""<br>
	 *         배열의 1은 기본 단위(base)<br>
	 */
	public static String[] devideUnitToPrefixAndBase(String unit) throws Exception {
		
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
		if(unitPrefix.isEmpty() == false && unitPrefixMap.containsKey(unitPrefix) == false) {
			unitPrefix = "";
			baseUnit = unit;
		}
		
		return new String[] {unitPrefix, baseUnit};
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
	public static double unitPrefixToFactor(String unitPrefix) throws Exception {
		
		if(unitPrefixMap != null && unitPrefixMap.containsKey(unitPrefix) == true) {
			return unitPrefixMap.get(unitPrefix);
		} else if(unitPrefix != null && unitPrefix.trim().equals("") == true) {
			return 1;
		} else {
			throw new Exception("requested unit prefix(" + unitPrefix + ") is not found");
		}
	}
	
	/**
	 * 
	 * @author jmsohn 
	 */
	public static class UnitExp {
		
		/** */
		@Getter
		private double value;
		
		/** */
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
		 * 
		 * @param decimalPlaces
		 * @return
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
	 * 주어진 값에 적합한 byte 단위 접두어를 반환
	 * 
	 * @param value 주어진 값
	 * @return 단위 접두어
	 */
	private static String getUnitPrefix(BigDecimal value) {
		
		// 입력값 검증
		if(value == null) {
			throw new NullPointerException("value is null.");
		}
		
		value = value.abs();
		
		if(value.compareTo(new BigDecimal(0.000000000000000000000000001)) < 0) {
			return "q";
		}
		if(value.compareTo(new BigDecimal(0.000000000000000000000001)) < 0) {
			return "r";
		}
		if(value.compareTo(new BigDecimal(0.000000000000000000001)) < 0) {
			return "y";
		}
		if(value.compareTo(new BigDecimal(0.000000000000000001)) < 0) {
			return "z";
		}
		if(value.compareTo(new BigDecimal(0.000000000000001)) < 0) {
			return "a";
		}
		if(value.compareTo(new BigDecimal(0.000000000001)) < 0) {
			return "f";
		}
		if(value.compareTo(new BigDecimal(0.000000001)) < 0) {
			return "p";
		}
		if(value.compareTo(new BigDecimal(0.000001)) < 0) {
			return "n";
		}
		if(value.compareTo(new BigDecimal(0.001)) < 0) {
			return "μ";
		}
		if(value.compareTo(new BigDecimal(1.0)) < 0) {
			return "m";
		}
		if(value.compareTo(new BigDecimal(1000.0)) < 0) {
			return "";
		}
		if(value.compareTo(new BigDecimal(1000000.0)) < 0) {
			return "k";
		}
		if(value.compareTo(new BigDecimal(1000000000.0)) < 0) {
			return "M";
		}
		if(value.compareTo(new BigDecimal(1000000000000.0)) < 0) {
			return "G";
		}
		if(value.compareTo(new BigDecimal(1000000000000000.0)) < 0) {
			return "T";
		}
		if(value.compareTo(new BigDecimal(1000000000000000000.0)) < 0) {
			return "P";
		}
		if(value.compareTo(new BigDecimal(1000000000000000000000.0)) < 0) {
			return "E";
		}
		if(value.compareTo(new BigDecimal(1000000000000000000000000.0)) < 0) {
			return "Z";
		}
		if(value.compareTo(new BigDecimal(1000000000000000000000000000.0)) < 0) {
			return "Y";
		}
		if(value.compareTo(new BigDecimal(1000000000000000000000000000000.0)) < 0) {
			return "R";
		}
		
		return "Q";
	}
	
	/**
	 * 주어진 값에 단위 접두어를 추가한 표현식으로 변환하는 메소드<br>
	 * ex) 1234 -> 1.23M
	 * 
	 * @param value 변환할 값
	 * @return 단위 접두어 표현식 객체
	 */
	public static UnitExp toUnitExp(BigDecimal value) throws Exception {
		
		// 입력값 검증
		if(value == null) {
			throw new NullPointerException("value is null.");
		}
		
		// 값에 적합한 단위 접두어를 가져옴
		String prefix = getUnitPrefix(value);
		
		// 단위 접두어에 해당하는 factor 값을 가져옴
		double factor = unitPrefixToFactor(prefix);
		
		// 단위 표현식 객체를 생성하여 반환
		// BigDecimal 값을 소수점 이하(ex. 0.001)로 나누려고 하면 오류가 발생함
		// 이를 문자열로 변경후 나누면 문제가 없음
		// 이유는 정확히 모름
		return new UnitExp(value.divide(new BigDecimal(Double.toString(factor))).doubleValue(), prefix);
	}
	
	/**
	 * 주어진 값에 적합한 byte 단위 접두어를 반환
	 * 
	 * @param value 주어진 값
	 * @return byte 단위 접두어
	 */
	private static String getByteUnitPrefix(BigDecimal value) {
		
		// 입력값 검증
		if(value == null) {
			throw new NullPointerException("value is null.");
		}
		
		value = value.abs();
		
		if(value.compareTo(new BigDecimal(1024.0)) < 0) {
			return "";
		}
		if(value.compareTo(new BigDecimal(1048576.0)) < 0) {
			return "Ki";
		}
		if(value.compareTo(new BigDecimal(1073741824.0)) < 0) {
			return "Mi";
		}
		if(value.compareTo(new BigDecimal(1099511627776.0)) < 0) {
			return "Gi";
		}
		if(value.compareTo(new BigDecimal(1125899906842624.0)) < 0) {
			return "Ti";
		}
		if(value.compareTo(new BigDecimal(1152921504606846976.0)) < 0) {
			return "Pi";
		}
		if(value.compareTo(new BigDecimal(1180591620717411303424.0)) < 0) {
			return "Ei";
		}
		if(value.compareTo(new BigDecimal(1208925819614629174706176.0)) < 0) {
			return "Zi";
		}
		
		return "Yi";
	}
	
	/**
	 * 주어진 값에 byte 단위 접두어를 추가한 표현식으로 변환하는 메소드<br>
	 * ex) 1024 -> 1.00Mi
	 * 
	 * @param value 주어진 값
	 * @return 단위 접두어 표현식 객체
	 */
	public static UnitExp toByteUnitExp(BigDecimal value) throws Exception {
		
		// 입력값 검증
		if(value == null) {
			throw new NullPointerException("value is null.");
		}
		
		// 값에 적합한 byte 단위 접두어를 가져옴
		String prefix = getByteUnitPrefix(value);
		
		// byte 단위 접두어에 해당하는 factor 값을 가져옴
		double factor = unitPrefixToFactor(prefix);
		
		// 단위 표현식 객체를 생성하여 반환
		return new UnitExp(value.divide(new BigDecimal(factor)).doubleValue(), prefix);
	}
	
	/**
	 * 주어진 값(long 형)에 byte 단위 접두어를 추가한 표현식으로 변환하는 메소드<br>
	 * ex) 1024 -> 1.00Mi
	 * 
	 * @param value 주어진 값(long 형)
	 */
	public static UnitExp toByteUnitExp(long value) throws Exception {
		return toByteUnitExp(new BigDecimal(value));
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

}
