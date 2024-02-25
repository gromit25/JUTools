package com.jutools;

import java.math.BigDecimal;
import java.math.MathContext;
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
	
	private static String[] enlargePrefixList = {"", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q"};
	private static String[] reducePrefixList  = {"", "m", "μ", "n", "p", "f", "a", "z", "y", "r", "q"};
	
	private static String[] bitPrefixList = {"", "Ki", "Mi", "Gi", "Ti", "Pi", "Ei", "Zi", "Yi"};
	
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
				return "";
			}
		}
	}
	
	/**
	 * 
	 * @param value
	 */
	public static UnitExp toUnitExp(double value) throws Exception {
		
		//
		String prefix = null;
		
		// 값의 자리수 계산
		int valueDigit = (int)(Math.floor(Math.log10(Math.abs(value))/3.0));
		
		if(valueDigit >= 0) {
			
			if(valueDigit > enlargePrefixList.length - 1) {
				valueDigit = enlargePrefixList.length - 1;
			}
			
			prefix = enlargePrefixList[valueDigit];
			
		} else {
			
			if(-valueDigit > reducePrefixList.length - 1) {
				valueDigit = (-reducePrefixList.length) + 1;
			}
			
			prefix = reducePrefixList[-valueDigit];
		}
		
		//
		double factor = unitPrefixToFactor(prefix);
		
		//
		if(factor != 0) {
			return new UnitExp(value / factor, prefix);
		} else {
			return new UnitExp(value, prefix);
		}
	}
	
	/**
	 * 
	 * @param value
	 */
	public static UnitExp toBitUnitExp(double value) throws Exception {
		
		// 입력값 검증
		if(value < 0) {
			throw new IllegalArgumentException("value must be greater than 0:" + value);
		}
		
		// 값의 자리수 계산
		int valueDigit = (int)(Math.floor(Math.log10(value)/3.0102999566398119521373889472449));
		//int valueDigit = (int)(Math.floor(Math.log10(value)/Math.log10(1024.0)));
		
		BigDecimal t1 = new BigDecimal(Math.log10(value));
		BigDecimal t2 = new BigDecimal(3.010299956639812091196972687612287700176239013671875);
		
		//MathContext mc = new MathContext(100);
		//BigDecimal t3 = t1.divide(t2, mc);
		BigDecimal t3 = t2.multiply(new BigDecimal(8));
		
		System.out.println("DEBUG 101:" + t1);
		System.out.println("DEBUG 102:" + t2);
		System.out.println("DEBUG 103:" + t3);
		
		if(valueDigit > bitPrefixList.length - 1) {
			valueDigit = bitPrefixList.length - 1;
		}
		
		//
		String prefix = bitPrefixList[valueDigit];
		
		//
		double factor = unitPrefixToFactor(prefix);
		
		//
		if(factor != 0) {
			return new UnitExp(value / factor, prefix);
		} else {
			return new UnitExp(value, prefix);
		}
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
